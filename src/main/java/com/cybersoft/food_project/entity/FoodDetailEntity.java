package com.cybersoft.food_project.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "food_detail")
public class FoodDetailEntity {
    @Id
    @Column(name = "id_food")
    private int idFood;

    @Column(name = "description")
    private String description;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "rating")
    private float rating;

    @OneToOne
    @JoinColumn(name = "id_food")
    private FoodEntity food;

    public int getIdFood() {
        return idFood;
    }

    public void setIdFood(int idFood) {
        this.idFood = idFood;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public FoodEntity getFood() {
        return food;
    }

    public void setFood(FoodEntity food) {
        this.food = food;
    }
}
