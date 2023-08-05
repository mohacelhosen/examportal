package com.mcu.examportal.controller;

import com.mcu.examportal.entity.exam.Category;
import com.mcu.examportal.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/api/exam-portal/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<Category> addCategory(@RequestBody Category category){
        Category addCategory = categoryService.addCategory(category);
        log.info("CategoryController::addCategory, ✅");
        return  new ResponseEntity<>(addCategory, HttpStatus.CREATED);
    }
    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategory(@PathVariable Integer categoryId){
        Category category = categoryService.getCategory(categoryId);
        log.info("CategoryController::getCategory, ✅");
        return new ResponseEntity<>(category, HttpStatus.FOUND);
    }
    @GetMapping("/all")
    public ResponseEntity<Set<Category>> getCategories(){
        Set<Category> categories = categoryService.getCategories();
        log.info("CategoryController::getCategories, ✅");
        return  new ResponseEntity<>(categories, HttpStatus.OK);
    }
    @PutMapping("/update")
    public  ResponseEntity<Category> updateCategory(@RequestBody Category category){
        Category updateCategory = categoryService.updateCategory(category);
        log.info("CategoryController::updateCategory, ✅");
        return  new ResponseEntity<>(updateCategory, HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer categoryId){
        Boolean isDeleted = categoryService.deleteCategory(categoryId);
        String msg="Something Wrong!";
        if (isDeleted){
            msg="Delete Successfully";
        }
        log.info("CategoryController::deleteCategory, ✅");
        return  new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
    }


}
