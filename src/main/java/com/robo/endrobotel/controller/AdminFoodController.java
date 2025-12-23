package com.robo.endrobotel.controller;

import com.robo.endrobotel.domain.Food;
import com.robo.endrobotel.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/foods")
@RequiredArgsConstructor
public class AdminFoodController {

    private final FoodRepository foodRepository;
    private Food food;

    @PostMapping
    public Food create(@RequestBody Food food) {
        this.food = food;
        return foodRepository.save(food);
    }

    @PutMapping("/{id}")
    public Food update(@PathVariable Long id, @RequestBody Food food) {
        Food existing = foodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Food not found"));

        existing.setName(food.getName());
        return foodRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        foodRepository.deleteById(id);
    }

    @GetMapping
    public List<Food> getAll() {
        return foodRepository.findAll();
    }
}
