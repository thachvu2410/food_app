package com.cybersoft.food_project.service;

import com.cybersoft.food_project.entity.CategoryEntity;
import com.cybersoft.food_project.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImp implements CategoryService{
    @Autowired
    CategoryRepository categoryRepository;

    public List<CategoryEntity> getExplorerCategory(){
        return categoryRepository.getExplorer();
    }
}
