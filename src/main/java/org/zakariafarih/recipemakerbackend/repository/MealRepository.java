package org.zakariafarih.recipemakerbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zakariafarih.recipemakerbackend.entity.Meal;
import org.zakariafarih.recipemakerbackend.entity.MealPlan;

import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

    List<Meal> findByMealPlan(MealPlan mealPlan);

    // TODO -> Optional: Add methods to find meals by recipeId, name, date, etc.
}
