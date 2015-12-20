package com.example.model;

import java.util.List;

public class Category {

    private int id;
    private String name;
    private String description;
    private String image;
    private List<Dish> listDishes;

    public Category() {
    }

    public Category(int id) {
        this.id = id;
    }
    
    public Category(int id, String name, String description, boolean isEnable, String image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public List<Dish> getListDishes() {
        return listDishes;
    }

    public void setListDishes(List<Dish> listDishes) {
        this.listDishes = listDishes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Category{" + "id=" + id + ", name=" + name + '}';
    }

}
