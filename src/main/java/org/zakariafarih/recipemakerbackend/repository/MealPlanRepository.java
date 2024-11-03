package org.zakariafarih.recipemakerbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zakariafarih.recipemakerbackend.entity.MealPlan;
import org.zakariafarih.recipemakerbackend.entity.User;

import java.util.List;

@Repository
public interface MealPlanRepository extends JpaRepository<MealPlan, Long> {

    List<MealPlan> findByUser(User user);

    // TODO -> Optional: Add methods to find meal plans by date range, name, etc.
}
