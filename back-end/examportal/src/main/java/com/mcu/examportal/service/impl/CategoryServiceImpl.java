package com.mcu.examportal.service.impl;

import com.mcu.examportal.entity.exam.Category;
import com.mcu.examportal.exception.ResourceNotFoundException;
import com.mcu.examportal.repository.CategoryRepository;
import com.mcu.examportal.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository repository;

    @Override
    public Category addCategory(Category category) {
        log.info("CategoryServiceImpl::addCategory, ⁉️");
        return repository.save(category);
    }

    @Override
    public Category updateCategory(Category category) {
        log.info("CategoryServiceImpl::updateCategory, ⁉️");
        return this.repository.save(category);
    }

    @Override
    public Category getCategory(Integer categoryId) {
        Optional<Category> category = repository.findById(categoryId);
        if (category.isPresent()) {
            log.info("CategoryServiceImpl::getCategory, ⁉️");
            return category.get();
        } else {
            throw new ResourceNotFoundException("Resource Not Found!");
        }
    }

    @Override
    public Set<Category> getCategories() {
        List<Category> categories = this.repository.findAll();
        log.info("CategoryServiceImpl::getCategories, ⁉️");
        return new HashSet<>(categories);
    }

    @Override
    public Boolean deleteCategory(Integer categoryId) {
        Optional<Category> category = repository.findById(categoryId);
        if (category.isPresent()) {
            repository.delete(category.get());
            log.info("CategoryServiceImpl::deleteCategory, ⁉️");
            return true;
        } else {
            throw new ResourceNotFoundException("Category Not Found!, ID::"+categoryId);
        }
    }
}
