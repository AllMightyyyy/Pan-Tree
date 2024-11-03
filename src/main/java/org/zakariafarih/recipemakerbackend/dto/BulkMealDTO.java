package org.zakariafarih.recipemakerbackend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class BulkMealDTO {

    @NotEmpty(message = "Meal list cannot be empty")
    private List<@Valid MealDTO> meals;

    public List<MealDTO> getMeals() {
        return meals;
    }

    public void setMeals(List<MealDTO> meals) {
        this.meals = meals;
    }
}
