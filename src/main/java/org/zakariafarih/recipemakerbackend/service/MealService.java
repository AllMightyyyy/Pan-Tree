package org.zakariafarih.recipemakerbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zakariafarih.recipemakerbackend.dto.MealDTO;
import org.zakariafarih.recipemakerbackend.dto.edamam.Recipe;
import org.zakariafarih.recipemakerbackend.entity.Meal;
import org.zakariafarih.recipemakerbackend.entity.MealPlan;
import org.zakariafarih.recipemakerbackend.exception.RecipeNotFoundException;
import org.zakariafarih.recipemakerbackend.exception.ResourceNotFoundException;
import org.zakariafarih.recipemakerbackend.exception.UnauthorizedException;
import org.zakariafarih.recipemakerbackend.repository.MealPlanRepository;
import org.zakariafarih.recipemakerbackend.repository.MealRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MealService {

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private MealPlanRepository mealPlanRepository;

    @Autowired
    private RecipeService recipeService;

    /**
     * Adds multiple meals to a meal plan in bulk.
     *
     * @param mealPlanId The ID of the meal plan.
     * @param meals      The list of meals to add.
     * @return A list of added MealDTOs.
     */
    @Transactional
    public Mono<List<MealDTO>> addMealsToPlan(Long mealPlanId, List<MealDTO> meals) {
        return Mono.justOrEmpty(mealPlanRepository.findById(mealPlanId))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("MealPlan not found")))
                .flatMapMany(mealPlan -> Flux.fromIterable(meals)
                        .flatMap(mealDTO -> {
                            Meal meal = new Meal();
                            meal.setName(mealDTO.getName());
                            meal.setScheduledAt(mealDTO.getScheduledAt());
                            meal.setMealPlan(mealPlan);

                            if (mealDTO.getRecipeId() != null && !mealDTO.getRecipeId().trim().isEmpty()) {
                                return recipeService.getRecipeById(mealDTO.getRecipeId())
                                        .map(recipe -> {
                                            meal.setRecipeId(recipe.getUri());
                                            meal.setRecipeName(recipe.getLabel());
                                            meal.setRecipeUrl(recipe.getUrl());
                                            return meal;
                                        });
                            }
                            return Mono.just(meal);
                        })
                )
                .collectList()
                .flatMap(mealsToSave -> {
                    List<Meal> savedMeals = mealRepository.saveAll(mealsToSave);
                    return Mono.just(savedMeals.stream()
                            .map(this::mapToDTO)
                            .collect(Collectors.toList()));
                });
    }

    // Update a meal
    @Transactional
    public Mono<MealDTO> updateMeal(Long mealId, MealDTO mealDTO, Long userId) throws Exception {
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new Exception("Meal not found"));

        // Check if the meal belongs to a meal plan owned by the user
        if (!meal.getMealPlan().getUser().getId().equals(userId)) {
            throw new Exception("Unauthorized access to update meal");
        }

        meal.setName(mealDTO.getName());
        meal.setScheduledAt(mealDTO.getScheduledAt());

        // Update recipe if provided
        if (mealDTO.getRecipeId() != null && !mealDTO.getRecipeId().trim().isEmpty()) {
            Mono<Recipe> recipeMono = recipeService.getRecipeById(mealDTO.getRecipeId());
            Recipe recipe = recipeMono.block(); // Blocking here for simplicity; consider reactive handling

            if (recipe != null) {
                meal.setRecipeId(recipe.getUri());
                meal.setRecipeName(recipe.getLabel());
                meal.setRecipeUrl(recipe.getUrl());
            } else {
                throw new Exception("Recipe not found with ID: " + mealDTO.getRecipeId());
            }
        }

        Meal updatedMeal = mealRepository.save(meal);
        return Mono.just(mapToDTO(updatedMeal));
    }

    // Remove a meal from a meal plan
    @Transactional
    public void removeMeal(Long mealId, Long userId) throws Exception {
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new Exception("Meal not found"));

        // Check if the meal belongs to a meal plan owned by the user
        if (!meal.getMealPlan().getUser().getId().equals(userId)) {
            throw new Exception("Unauthorized access to delete meal");
        }

        mealRepository.delete(meal);
    }

    // Mapping from Entity to DTO
    public MealDTO mapToDTO(Meal meal) {
        MealDTO dto = new MealDTO();
        dto.setId(meal.getId());
        dto.setName(meal.getName());
        dto.setRecipeId(meal.getRecipeId());
        dto.setRecipeName(meal.getRecipeName());
        dto.setRecipeUrl(meal.getRecipeUrl());
        dto.setScheduledAt(meal.getScheduledAt());
        return dto;
    }

    // Optionally, add methods to fetch meals by meal plan, etc.
    public Set<MealDTO> getMealsByMealPlanId(Long mealPlanId, Long userId) throws Exception {
        MealPlan mealPlan = mealPlanRepository.findById(mealPlanId)
                .orElseThrow(() -> new Exception("MealPlan not found"));

        if (!mealPlan.getUser().getId().equals(userId)) {
            throw new Exception("Unauthorized access to meal plan");
        }

        return mealPlan.getMeals().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toSet());
    }

    @Transactional
    public Mono<MealDTO> addMealToPlan(Long mealPlanId, MealDTO mealDTO) {
        return Mono.justOrEmpty(mealPlanRepository.findById(mealPlanId))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("MealPlan not found")))
                .flatMap(mealPlan -> {
                    Meal meal = new Meal();
                    meal.setName(mealDTO.getName());
                    meal.setScheduledAt(mealDTO.getScheduledAt());

                    if (mealDTO.getRecipeId() != null && !mealDTO.getRecipeId().trim().isEmpty()) {
                        return recipeService.getRecipeById(mealDTO.getRecipeId())
                                .switchIfEmpty(Mono.error(new RecipeNotFoundException("Recipe not found with ID: " + mealDTO.getRecipeId())))
                                .map(recipe -> {
                                    meal.setRecipeId(recipe.getUri());
                                    meal.setRecipeName(recipe.getLabel());
                                    meal.setRecipeUrl(recipe.getUrl());
                                    return meal;
                                });
                    }
                    return Mono.just(meal);
                })
                .flatMap(meal -> Mono.just(mealRepository.save(meal)))
                .map(this::mapToDTO);
    }
}
