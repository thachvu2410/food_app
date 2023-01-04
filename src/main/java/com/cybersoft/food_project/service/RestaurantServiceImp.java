package com.cybersoft.food_project.service;

import com.cybersoft.food_project.dto.RestaurantDTO;
import com.cybersoft.food_project.dto.RestaurantDetailDTO;
import com.cybersoft.food_project.entity.RestaurantEntity;
import com.cybersoft.food_project.entity.RestaurantReviewEntity;
import com.cybersoft.food_project.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantServiceImp implements RestaurantService{
    @Autowired
    RestaurantRepository restaurantRepository;
    @Override
    public List<RestaurantDTO> getRestaurant() {
        List<RestaurantDTO> restaurantDTOList = new ArrayList<>();
        List<RestaurantEntity> restaurantEntities = restaurantRepository.findAll();
        // xử lí data [{title: "", image: "", avgRate: ""}]
        for (RestaurantEntity data: restaurantEntities) {
            RestaurantDTO restaurantDTO = new RestaurantDTO();
            restaurantDTO.setTitle(data.getName());
            restaurantDTO.setImage(data.getImage());
            float sumRate = 0;
            float avgRate = 0;
            for (RestaurantReviewEntity review: data.getRestaurantReviews()) {
                sumRate += review.getRate();
            }
            if (data.getRestaurantReviews().size() > 0){
                avgRate = sumRate/(data.getRestaurantReviews().size());
            }
            restaurantDTO.setAvgRate(avgRate);
            restaurantDTOList.add(restaurantDTO);
        }
        return restaurantDTOList;
    }

    @Override
    public RestaurantDetailDTO getDetailRestaurant(int id) {
        // Optional: co hoac ko co cung duoc (du lieu co the bi null)
        Optional<RestaurantEntity> restaurantEntity = restaurantRepository.findById(id);
        RestaurantDetailDTO restaurantDetailDTO = new RestaurantDetailDTO();
        if (restaurantEntity.isPresent()){
            // co gia tri thi xu ly
            restaurantDetailDTO.setTitle(restaurantEntity.get().getName());
            restaurantDetailDTO.setImage(restaurantEntity.get().getImage());
//            restaurantDetailDTO.setDescription("");
            float sumRate = 0;
            float avgRate = 0;
            for (RestaurantReviewEntity data: restaurantEntity.get().getRestaurantReviews()) {
                sumRate += data.getRate();
            }
            if (restaurantEntity.get().getRestaurantReviews().size() > 0){
                avgRate = sumRate/(restaurantEntity.get().getRestaurantReviews().size());
            }
            restaurantDetailDTO.setAvgRate(avgRate);
        }
        return restaurantDetailDTO;
    }
}
