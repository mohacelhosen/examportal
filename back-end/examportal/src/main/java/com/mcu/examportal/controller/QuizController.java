package com.mcu.examportal.controller;

import com.mcu.examportal.entity.exam.Category;
import com.mcu.examportal.entity.exam.Question;
import com.mcu.examportal.entity.exam.Quiz;
import com.mcu.examportal.exception.InvalidRequestException;
import com.mcu.examportal.repository.CategoryRepository;
import com.mcu.examportal.repository.QuizRepository;
import com.mcu.examportal.service.QuizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/api/exam-portal/quiz")
@Slf4j
public class QuizController {
    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private QuizService quizService;

    //    Add the quiz
    @PostMapping("/add")
    public ResponseEntity<Quiz> addQuiz(@RequestBody Quiz quiz) {
        Quiz addQuiz = quizService.addQuiz(quiz);
        log.info("QuizController::addQuiz, ✅");
        return new ResponseEntity<>(addQuiz, HttpStatus.CREATED);
    }

    //    update the quiz
    @PutMapping("/update/{quizId}")
    public ResponseEntity<Quiz> updateQuiz(@RequestBody Quiz quiz, @PathVariable Integer quizId) {
        Optional<Quiz> optionalQuiz = quizRepository.findById(quizId);
        if (optionalQuiz.isPresent()){
            quiz.setQuizId(optionalQuiz.get().getQuizId());
            Quiz addQuiz = quizService.updateQuiz(quiz);
            log.info("QuizController::updateQuiz, ✅");
            return new ResponseEntity<>(addQuiz, HttpStatus.OK);
        }
        throw new InvalidRequestException("Question Id Not found!, ID::"+quizId);
    }

    //    get single quiz info by quiz-id
    @GetMapping("/{quizId}")
    public ResponseEntity<?> getQuiz(@PathVariable Integer quizId) {
        log.info("QuizController::getQuiz,"+quizId);
        Quiz quiz = quizService.getQuiz(quizId);
        log.info("QuizController::getQuiz, ✅"+quiz.toString());
        return new ResponseEntity<>(quiz, HttpStatus.OK);
    }

    //    get all quiz info
    @GetMapping("/all")
    public ResponseEntity<Set<Quiz>> getAllQuiz() {
        Set<Quiz> quizzes = quizService.quizSet();
        log.info("QuizController::getAllQuiz, ✅");
        return new ResponseEntity<>(quizzes, HttpStatus.OK);
    }

    //    delete quiz info by quiz-id
    @DeleteMapping("/{quizId}")
    public ResponseEntity<?> deleteQuiz(@PathVariable Integer quizId) {
        Boolean isDeleted = quizService.deleteQuiz(quizId);
        String msg = "Something Wrong!";
        if (isDeleted) {
            msg = "Delete Successfully";
        }
        log.info("QuizController::deleteCategory, ✅");
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    //    get a Quizzes via categoryId
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> allQuizAccordingToCategory(@PathVariable Integer categoryId) {
        Category category= new Category();
        category.setCategoryId(categoryId);
        Optional<Category> categoryObject = categoryRepository.findById(categoryId);
        category.setQuizSet(categoryObject.get().getQuizSet());
        Set<Quiz> quizOfCategories = this.quizService.getQuizOfCategories(category);
        return new ResponseEntity<>(quizOfCategories, HttpStatus.OK);
    }

}
