package com.robo.endrobotel.service;

import com.robo.endrobotel.domain.Food;
import com.robo.endrobotel.repository.FoodRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodService {
    private final FoodRepository foodRepository;

    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }


    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }

}
