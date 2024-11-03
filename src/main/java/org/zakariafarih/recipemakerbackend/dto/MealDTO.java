package org.zakariafarih.recipemakerbackend.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class MealDTO {

    private Long id;

    @NotBlank(message = "Meal name is required")
    @Size(max = 50, message = "Meal name can be up to 50 characters")
    private String name; // e.g., Breakfast

    @Size(max = 255, message = "Recipe URL can be up to 255 characters")
    private String recipeUrl;

    private String recipeId; // To link with Edamam API

    private String recipeName; // For quick reference

    private LocalDateTime scheduledAt;

    public MealDTO() {}

    public MealDTO(Long id, String name, String recipeUrl, String recipeId, String recipeName, LocalDateTime scheduledAt) {
        this.id = id;
        this.name = name;
        this.recipeUrl = recipeUrl;
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.scheduledAt = scheduledAt;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "Meal name is required") @Size(max = 50, message = "Meal name can be up to 50 characters") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Meal name is required") @Size(max = 50, message = "Meal name can be up to 50 characters") String name) {
        this.name = name;
    }

    public @Size(max = 255, message = "Recipe URL can be up to 255 characters") String getRecipeUrl() {
        return recipeUrl;
    }

    public void setRecipeUrl(@Size(max = 255, message = "Recipe URL can be up to 255 characters") String recipeUrl) {
        this.recipeUrl = recipeUrl;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }
}
