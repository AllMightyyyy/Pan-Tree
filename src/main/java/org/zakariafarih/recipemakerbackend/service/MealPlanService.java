package org.zakariafarih.recipemakerbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zakariafarih.recipemakerbackend.dto.MealPlanDTO;
import org.zakariafarih.recipemakerbackend.entity.MealPlan;
import org.zakariafarih.recipemakerbackend.entity.User;
import org.zakariafarih.recipemakerbackend.repository.MealPlanRepository;
import org.zakariafarih.recipemakerbackend.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MealPlanService {

    @Autowired
    private MealPlanRepository mealPlanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MealService mealService;

    // Create a new meal plan
    @Transactional
    public MealPlanDTO createMealPlan(Long userId, MealPlanDTO mealPlanDTO) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        MealPlan mealPlan = new MealPlan();
        mealPlan.setName(mealPlanDTO.getName());
        mealPlan.setDescription(mealPlanDTO.getDescription());
        mealPlan.setStartDate(mealPlanDTO.getStartDate());
        mealPlan.setEndDate(mealPlanDTO.getEndDate());
        mealPlan.setUser(user);

        // Save meal plan
        MealPlan savedMealPlan = mealPlanRepository.save(mealPlan);

        // Optionally, handle meals if provided
        if (mealPlanDTO.getMeals() != null && !mealPlanDTO.getMeals().isEmpty()) {
            savedMealPlan.getMeals().clear();
            mealPlanDTO.getMeals().forEach(mealDTO -> {
                try {
                    mealService.addMealToPlan(savedMealPlan.getId(), mealDTO);
                } catch (Exception e) {
                    // Handle exceptions (log or rethrow)
                }
            });
        }

        return mapToDTO(savedMealPlan);
    }

    // Retrieve all meal plans for a user
    public List<MealPlanDTO> getAllMealPlansForUser(Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        List<MealPlan> mealPlans = mealPlanRepository.findByUser(user);
        return mealPlans.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Retrieve a specific meal plan by ID
    public MealPlanDTO getMealPlanById(Long mealPlanId, Long userId) throws Exception {
        MealPlan mealPlan = mealPlanRepository.findById(mealPlanId)
                .orElseThrow(() -> new Exception("MealPlan not found"));

        if (!mealPlan.getUser().getId().equals(userId)) {
            throw new Exception("Unauthorized access to meal plan");
        }

        return mapToDTO(mealPlan);
    }

    // Update a meal plan
    @Transactional
    public MealPlanDTO updateMealPlan(Long mealPlanId, Long userId, MealPlanDTO mealPlanDTO) throws Exception {
        MealPlan mealPlan = mealPlanRepository.findById(mealPlanId)
                .orElseThrow(() -> new Exception("MealPlan not found"));

        if (!mealPlan.getUser().getId().equals(userId)) {
            throw new Exception("Unauthorized access to update meal plan");
        }

        mealPlan.setName(mealPlanDTO.getName());
        mealPlan.setDescription(mealPlanDTO.getDescription());
        mealPlan.setStartDate(mealPlanDTO.getStartDate());
        mealPlan.setEndDate(mealPlanDTO.getEndDate());

        // Optionally, handle meals (add/update/remove)
        // This can be expanded based on requirements

        MealPlan updatedMealPlan = mealPlanRepository.save(mealPlan);
        return mapToDTO(updatedMealPlan);
    }

    // Delete a meal plan
    @Transactional
    public void deleteMealPlan(Long mealPlanId, Long userId) throws Exception {
        MealPlan mealPlan = mealPlanRepository.findById(mealPlanId)
                .orElseThrow(() -> new Exception("MealPlan not found"));

        if (!mealPlan.getUser().getId().equals(userId)) {
            throw new Exception("Unauthorized access to delete meal plan");
        }

        mealPlanRepository.delete(mealPlan);
    }

    // Mapping from Entity to DTO
    private MealPlanDTO mapToDTO(MealPlan mealPlan) {
        MealPlanDTO dto = new MealPlanDTO();
        dto.setId(mealPlan.getId());
        dto.setName(mealPlan.getName());
        dto.setDescription(mealPlan.getDescription());
        dto.setStartDate(mealPlan.getStartDate());
        dto.setEndDate(mealPlan.getEndDate());
        dto.setMeals(mealPlan.getMeals().stream()
                .map(mealService::mapToDTO)
                .collect(Collectors.toSet()));
        return dto;
    }
}
