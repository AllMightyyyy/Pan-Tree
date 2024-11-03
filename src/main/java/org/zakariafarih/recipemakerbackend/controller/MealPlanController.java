package org.zakariafarih.recipemakerbackend.controller;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.zakariafarih.recipemakerbackend.dto.BulkMealDTO;
import org.zakariafarih.recipemakerbackend.dto.MealDTO;
import org.zakariafarih.recipemakerbackend.dto.MealPlanDTO;
import org.zakariafarih.recipemakerbackend.entity.Meal;
import org.zakariafarih.recipemakerbackend.exception.RecipeNotFoundException;
import org.zakariafarih.recipemakerbackend.exception.ResourceNotFoundException;
import org.zakariafarih.recipemakerbackend.repository.MealPlanRepository;
import org.zakariafarih.recipemakerbackend.repository.MealRepository;
import org.zakariafarih.recipemakerbackend.service.MealPlanService;
import org.zakariafarih.recipemakerbackend.service.MealService;
import org.zakariafarih.recipemakerbackend.service.RecipeService;
import org.zakariafarih.recipemakerbackend.service.UserService;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/meal-plans")
public class MealPlanController {

    @Autowired
    private MealPlanService mealPlanService;

    @Autowired
    private UserService userService;

    @Autowired
    private MealService mealService;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private MealPlanRepository mealPlanRepository;

    @Autowired
    private MealRepository mealRepository;

    // Helper method to get current user's ID
    private Long getCurrentUserId(UserDetails userDetails) throws Exception {
        return userService.getUserIdByUsername(userDetails.getUsername());
    }

    // Create a new meal plan
    @PostMapping
    public ResponseEntity<MealPlanDTO> createMealPlan(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody MealPlanDTO mealPlanDTO) {
        try {
            Long userId = getCurrentUserId(userDetails);
            MealPlanDTO createdMealPlan = mealPlanService.createMealPlan(userId, mealPlanDTO);
            return new ResponseEntity<>(createdMealPlan, HttpStatus.CREATED);
        } catch (Exception e) {
            // Handle exceptions appropriately (use custom exceptions and handlers)
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Retrieve all meal plans for the current user
    @GetMapping
    public ResponseEntity<List<MealPlanDTO>> getAllMealPlans(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            Long userId = getCurrentUserId(userDetails);
            List<MealPlanDTO> mealPlans = mealPlanService.getAllMealPlansForUser(userId);
            return new ResponseEntity<>(mealPlans, HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions appropriately
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Retrieve a specific meal plan by ID
    @GetMapping("/{id}")
    public ResponseEntity<MealPlanDTO> getMealPlanById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("id") Long mealPlanId) {
        try {
            Long userId = getCurrentUserId(userDetails);
            MealPlanDTO mealPlan = mealPlanService.getMealPlanById(mealPlanId, userId);
            return new ResponseEntity<>(mealPlan, HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions appropriately
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Update a meal plan
    @PutMapping("/{id}")
    public ResponseEntity<MealPlanDTO> updateMealPlan(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("id") Long mealPlanId,
            @Valid @RequestBody MealPlanDTO mealPlanDTO) {
        try {
            Long userId = getCurrentUserId(userDetails);
            MealPlanDTO updatedMealPlan = mealPlanService.updateMealPlan(mealPlanId, userId, mealPlanDTO);
            return new ResponseEntity<>(updatedMealPlan, HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions appropriately
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete a meal plan
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteMealPlan(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("id") Long mealPlanId) {
        try {
            Long userId = getCurrentUserId(userDetails);
            mealPlanService.deleteMealPlan(mealPlanId, userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            // Handle exceptions appropriately
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Add a meal to a meal plan
    @PostMapping("/{mealPlanId}/meals")
    public ResponseEntity<MealDTO> addMealToMealPlan(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long mealPlanId,
            @Valid @RequestBody MealDTO mealDTO) {
        try {
            Long userId = getCurrentUserId(userDetails);
            // Ensure the meal plan belongs to the user
            MealPlanDTO mealPlan = mealPlanService.getMealPlanById(mealPlanId, userId);
            if (mealPlan == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            MealDTO addedMeal = mealService.addMealToPlan(mealPlanId, mealDTO).block(); // Blocking for simplicity
            return new ResponseEntity<>(addedMeal, HttpStatus.CREATED);
        } catch (Exception e) {
            // Handle exceptions appropriately
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

    /**
     * Maps a Meal entity to a MealDTO.
     *
     * @param meal The Meal entity to map.
     * @return The corresponding MealDTO.
     */
    private MealDTO mapToDTO(Meal meal) {
        return mealService.mapToDTO(meal);
    }

    /**
     * Adds multiple meals to a meal plan in bulk.
     *
     * @param userDetails The authenticated user details.
     * @param mealPlanId  The ID of the meal plan.
     * @param bulkMealDTO The DTO containing the list of meals.
     * @return A ResponseEntity containing the list of added meals.
     */
    @PostMapping("/{mealPlanId}/meals/bulk")
    public ResponseEntity<List<MealDTO>> addMealsBulk(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long mealPlanId,
            @Valid @RequestBody BulkMealDTO bulkMealDTO) {
        try {
            Long userId = getCurrentUserId(userDetails);
            // Ensure the meal plan belongs to the user
            MealPlanDTO mealPlan = mealPlanService.getMealPlanById(mealPlanId, userId);
            if (mealPlan == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            List<MealDTO> addedMeals = (List<MealDTO>) mealService.addMealsToPlan(mealPlanId, bulkMealDTO.getMeals());
            return new ResponseEntity<>(addedMeals, HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (RecipeNotFoundException e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            // Handle other exceptions appropriately
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
