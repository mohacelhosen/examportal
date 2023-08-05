package com.mcu.examportal.service;

import com.mcu.examportal.entity.exam.Category;

import java.util.Set;

public interface CategoryService {
    public Category addCategory(Category category);
    public Category updateCategory(Category category);
    public Category getCategory(Integer categoryId);
    public Set<Category> getCategories();
    public Boolean deleteCategory(Integer categoryId);
}
