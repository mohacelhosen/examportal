package com.mcu.examportal.controller;

import com.mcu.examportal.entity.exam.Quiz;
import com.mcu.examportal.service.QuizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/api/exam-portal/quiz")
@Slf4j
public class QuizController {
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
    @PutMapping("/update")
    public ResponseEntity<Quiz> updateQuiz(@RequestBody Quiz quiz) {
        Quiz addQuiz = quizService.updateQuiz(quiz);
        log.info("QuizController::updateQuiz, ✅");
        return new ResponseEntity<>(addQuiz, HttpStatus.OK);
    }

    //    get single quiz info by quiz-id
    @GetMapping("/{quizId}")
    public ResponseEntity<?> getQuiz(@PathVariable Integer quizId) {
        Quiz quiz = quizService.getQuiz(quizId);
        log.info("QuizController::getQuiz, ✅");
        return new ResponseEntity<>(quiz, HttpStatus.FOUND);
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
        return new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
    }


}
