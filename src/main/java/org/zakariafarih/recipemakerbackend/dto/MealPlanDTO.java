package org.zakariafarih.recipemakerbackend.dto;

import java.time.LocalDate;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MealPlanDTO {

    private Long id;

    @NotBlank(message = "Meal plan name is required")
    @Size(max = 100, message = "Meal plan name can be up to 100 characters")
    private String name;

    @Size(max = 500, message = "Description can be up to 500 characters")
    private String description;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private Set<MealDTO> meals;

    public MealPlanDTO() {}

    public MealPlanDTO(Long id, String name, String description, LocalDate startDate, LocalDate endDate, Set<MealDTO> meals) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.meals = meals;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull(message = "Start date is required") LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(@NotNull(message = "Start date is required") LocalDate startDate) {
        this.startDate = startDate;
    }

    public @NotBlank(message = "Meal plan name is required") @Size(max = 100, message = "Meal plan name can be up to 100 characters") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Meal plan name is required") @Size(max = 100, message = "Meal plan name can be up to 100 characters") String name) {
        this.name = name;
    }

    public @Size(max = 500, message = "Description can be up to 500 characters") String getDescription() {
        return description;
    }

    public void setDescription(@Size(max = 500, message = "Description can be up to 500 characters") String description) {
        this.description = description;
    }

    public @NotNull(message = "End date is required") LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(@NotNull(message = "End date is required") LocalDate endDate) {
        this.endDate = endDate;
    }

    public Set<MealDTO> getMeals() {
        return meals;
    }

    public void setMeals(Set<MealDTO> meals) {
        this.meals = meals;
    }
}
