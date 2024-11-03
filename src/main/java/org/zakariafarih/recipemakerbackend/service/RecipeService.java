package org.zakariafarih.recipemakerbackend.service;

import jakarta.persistence.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.zakariafarih.recipemakerbackend.config.EdamamConfig;
import org.zakariafarih.recipemakerbackend.dto.edamam.Recipe;
import org.zakariafarih.recipemakerbackend.dto.edamam.RecipeSearchFilters;
import org.zakariafarih.recipemakerbackend.dto.edamam.RecipeSearchResponse;

import org.springframework.http.HttpStatus;
import org.zakariafarih.recipemakerbackend.exception.RecipeNotFoundException;
import reactor.core.publisher.Mono;

@Service
public class RecipeService {

    private final WebClient webClient;
    private final EdamamConfig edamamConfig;

    @Autowired
    public RecipeService(WebClient webClient, EdamamConfig edamamConfig) {
        this.webClient = webClient;
        this.edamamConfig = edamamConfig;
    }

    /**
     * Searches for recipes based on the provided query and filters.
     *
     * @param query  The search query (e.g., "chicken").
     * @param filters Additional filters like diet, health labels, etc.
     * @return A Mono containing the search response.
     */
    public Mono<RecipeSearchResponse> searchRecipes(String query, RecipeSearchFilters filters) {
        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/recipes/v2")
                            .queryParam("type", "public")
                            .queryParam("q", query)
                            .queryParam("app_id", edamamConfig.getAppId())
                            .queryParam("app_key", edamamConfig.getAppKey());

                    // Apply optional filters
                    if (filters.isBeta()) {
                        uriBuilder.queryParam("beta", filters.isBeta());
                    }
                    if (filters.getDiet() != null && !filters.getDiet().isEmpty()) {
                        filters.getDiet().forEach(diet -> uriBuilder.queryParam("diet", diet));
                    }
                    if (filters.getHealth() != null && !filters.getHealth().isEmpty()) {
                        filters.getHealth().forEach(health -> uriBuilder.queryParam("health", health));
                    }
                    if (filters.getCuisineType() != null && !filters.getCuisineType().isEmpty()) {
                        filters.getCuisineType().forEach(cuisine -> uriBuilder.queryParam("cuisineType", cuisine));
                    }
                    if (filters.getMealType() != null && !filters.getMealType().isEmpty()) {
                        filters.getMealType().forEach(meal -> uriBuilder.queryParam("mealType", meal));
                    }
                    if (filters.getDishType() != null && !filters.getDishType().isEmpty()) {
                        filters.getDishType().forEach(dish -> uriBuilder.queryParam("dishType", dish));
                    }
                    if (filters.getExcluded() != null && !filters.getExcluded().isEmpty()) {
                        filters.getExcluded().forEach(exclude -> uriBuilder.queryParam("excluded", exclude));
                    }
                    if (filters.getCo2EmissionsClass() != null && !filters.getCo2EmissionsClass().isEmpty()) {
                        uriBuilder.queryParam("co2EmissionsClass", filters.getCo2EmissionsClass());
                        // Ensure 'beta=true' is set if using co2EmissionsClass
                        if (!filters.isBeta()) {
                            uriBuilder.queryParam("beta", true);
                        }
                    }
                    if (filters.getField() != null && !filters.getField().isEmpty()) {
                        filters.getField().forEach(field -> uriBuilder.queryParam("field", field));
                    }

                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(RecipeSearchResponse.class);
    }

    /**
     * Retrieves a specific recipe by its ID.
     *
     * @param recipeId The recipe ID (hash part after '#recipe_').
     * @return A Mono containing the recipe.
     */
    public Mono<Recipe> getRecipeById(String recipeId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/recipes/v2/{id}")
                        .queryParam("type", "public")
                        .queryParam("app_id", edamamConfig.getAppId())
                        .queryParam("app_key", edamamConfig.getAppKey())
                        .build(recipeId))
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), clientResponse ->
                        Mono.error(new RecipeNotFoundException("Recipe not found with ID: " + recipeId)))
                .onStatus(status -> status.is5xxServerError(), clientResponse ->
                        Mono.error(new RuntimeException("Edamam API server error")))
                .bodyToMono(Recipe.class);
    }
}
