package org.zakariafarih.recipemakerbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.zakariafarih.recipemakerbackend.dto.edamam.Recipe;
import org.zakariafarih.recipemakerbackend.dto.edamam.RecipeSearchFilters;
import org.zakariafarih.recipemakerbackend.dto.edamam.RecipeSearchResponse;
import org.zakariafarih.recipemakerbackend.service.RecipeService;

import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService){
        this.recipeService = recipeService;
    }

    @GetMapping("/search")
    public Mono<ResponseEntity<RecipeSearchResponse>> searchRecipes(
            @RequestParam(required = false, name = "q") String query,
            @RequestParam(required = false) Boolean beta,
            @RequestParam(required = false) List<String> diet,
            @RequestParam(required = false) List<String> health,
            @RequestParam(required = false) List<String> cuisineType,
            @RequestParam(required = false) List<String> mealType,
            @RequestParam(required = false) List<String> dishType,
            @RequestParam(required = false) List<String> excluded,
            @RequestParam(required = false) String co2EmissionsClass,
            @RequestParam(required = false) List<String> field
    ){
        // Validate the required parameter 'q' if no other parameters are specified
        if ((query == null || query.trim().isEmpty()) &&
                (diet == null || diet.isEmpty()) &&
                (health == null || health.isEmpty()) &&
                (cuisineType == null || cuisineType.isEmpty()) &&
                (mealType == null || mealType.isEmpty()) &&
                (dishType == null || dishType.isEmpty()) &&
                (excluded == null || excluded.isEmpty()) &&
                (co2EmissionsClass == null || co2EmissionsClass.isEmpty()) &&
                (field == null || field.isEmpty())) {
            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null));
        }

        RecipeSearchFilters filters = new RecipeSearchFilters();
        filters.setBeta(beta != null ? beta : false);
        filters.setDiet(diet);
        filters.setHealth(health);
        filters.setCuisineType(cuisineType);
        filters.setMealType(mealType);
        filters.setDishType(dishType);
        filters.setExcluded(excluded);
        filters.setCo2EmissionsClass(co2EmissionsClass);
        filters.setField(field);

        return recipeService.searchRecipes(query, filters)
                .map(response -> ResponseEntity.ok(response))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    /**
     * Retrieves a specific recipe by its ID.
     *
     * @param id The recipe ID.
     * @return A ResponseEntity containing the recipe details.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Recipe>> getRecipeById(@PathVariable String id){
        return recipeService.getRecipeById(id)
                .map(recipe -> ResponseEntity.ok(recipe))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
}
