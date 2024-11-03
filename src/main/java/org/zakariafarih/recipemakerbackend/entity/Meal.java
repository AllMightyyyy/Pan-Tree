package org.zakariafarih.recipemakerbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "meals")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // e.g., Breakfast, Lunch, Dinner

    private String recipeId; // ID from Edamam Recipe API

    private String recipeName; // Store recipe name for quick reference

    private String recipeUrl; // URL to the recipe

    private LocalDateTime scheduledAt; // When the meal is planned

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_plan_id", nullable = false)
    private MealPlan mealPlan;

    public Meal() {}

    public Meal(String name, String recipeId, String recipeName, String recipeUrl, LocalDateTime scheduledAt) {
        this.name = name;
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.recipeUrl = recipeUrl;
        this.scheduledAt = scheduledAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeUrl() {
        return recipeUrl;
    }

    public void setRecipeUrl(String recipeUrl) {
        this.recipeUrl = recipeUrl;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public MealPlan getMealPlan() {
        return mealPlan;
    }

    public void setMealPlan(MealPlan mealPlan) {
        this.mealPlan = mealPlan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meal meal = (Meal) o;
        return Objects.equals(id, meal.id) && Objects.equals(name, meal.name) && Objects.equals(recipeId, meal.recipeId) && Objects.equals(recipeName, meal.recipeName) && Objects.equals(recipeUrl, meal.recipeUrl) && Objects.equals(scheduledAt, meal.scheduledAt) && Objects.equals(mealPlan, meal.mealPlan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, recipeId, recipeName, recipeUrl, scheduledAt, mealPlan);
    }
}
