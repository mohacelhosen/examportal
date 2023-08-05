package com.mcu.examportal.service.impl;

import com.mcu.examportal.entity.exam.Category;
import com.mcu.examportal.repository.CategoryRepository;
import com.mcu.examportal.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository repository;
    @Override
    public Category addCategory(Category category) {
        return repository.save(category);
    }

    @Override
    public Category updateCategory(Category category) {
        return this.repository.save(category);
    }

    @Override
    public Category getCategory(Integer categoryId) {
        repository.findById(categoryId)
        return null;
    }

    @Override
    public Set<Category> getCategories() {
        List<Category> categories = this.repository.findAll();
        return new HashSet<>(categories);
    }

    @Override
    public boolean deleteCategory(Integer categoryId) {
        return false;
    }
}
