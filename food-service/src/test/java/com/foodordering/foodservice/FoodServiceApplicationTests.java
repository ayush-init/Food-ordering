package com.foodordering.foodservice;

import com.foodordering.foodservice.model.Food;
import com.foodordering.foodservice.repository.FoodRepository;
import com.foodordering.foodservice.service.FoodService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FoodServiceApplicationTests {

    @Mock
    private FoodRepository foodRepository;

    @InjectMocks
    private FoodService foodService;

    @Test
    void testFoodCrudOperations() {
        Food food = new Food("Cheese Pizza", 350.0, "Pizza", true, 1L);
        food.setFoodId(1L);

        when(foodRepository.findById(1L)).thenReturn(Optional.of(food));
        when(foodRepository.findAll()).thenReturn(List.of(food));

        Optional<Food> found = foodService.getFoodById(1L);
        assertTrue(found.isPresent());
        assertEquals("Cheese Pizza", found.get().getFoodName());

        List<Food> foods = foodService.getAllFoods();
        assertEquals(1, foods.size());

        when(foodRepository.existsById(1L)).thenReturn(true);
        boolean deleted = foodService.deleteFood(1L);
        assertTrue(deleted);
        verify(foodRepository, times(1)).deleteById(1L);
    }
}
