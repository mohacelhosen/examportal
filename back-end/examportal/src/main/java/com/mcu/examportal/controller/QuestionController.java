package com.mcu.examportal.controller;


import com.mcu.examportal.entity.exam.Question;
import com.mcu.examportal.entity.exam.Quiz;
import com.mcu.examportal.repository.QuestionRepository;
import com.mcu.examportal.repository.QuizRepository;
import com.mcu.examportal.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/api/exam-portal/question")
@Slf4j
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuizRepository quizRepository;

    //    add question
    @PostMapping("/add")
    public ResponseEntity<Question> addQuestion(@RequestBody Question question) {
        Question addedQuestion = questionService.addQuestion(question);
        return new ResponseEntity<>(addedQuestion, HttpStatus.OK);
    }


    //    update the question
    @PutMapping("/update")
    public ResponseEntity<Question> updateQuestion(Question question) {
        Question updateQuestion = questionService.updateQuestion(question);
        return new ResponseEntity<>(updateQuestion, HttpStatus.OK);
    }


    //    get single question via questionId
    @GetMapping("/{questionId}")
    public ResponseEntity<Question> getQuestion(@PathVariable Integer questionId) {
        Question question = questionService.getQuestion(questionId);
        log.info("QuestionController::getQuestion, ✅");
        return new ResponseEntity<>(question, HttpStatus.OK);
    }


    @GetMapping("/questions/all")
    public ResponseEntity<Set<Question>> getAllQuestion() {
        Set<Question> questionSet = questionService.questionList();
        log.info("QuestionController::getAllQuestion, ✅");
        return new ResponseEntity<>(questionSet, HttpStatus.OK);
    }

    //    get a question via quiz
    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<?> allQuestionAccordingToQuiz(@PathVariable Integer quizId) {
        Quiz quiz = new Quiz();
        quiz.setQuizId(quizId);
        Optional<Quiz> quizobject = quizRepository.findById(quizId);
        quiz.setCategoryObject(quizobject.get().getCategoryObject());
        Set<Question> questionListAccordingToQuiz = this.questionService.getQuestionOfQuiz(quiz);
        return new ResponseEntity<>(questionListAccordingToQuiz, HttpStatus.OK);
    }

    //    delete Question info via questionId
    @DeleteMapping("/{questionId}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Integer questionId) {
        Boolean isDeleted = questionService.deleteQuestion(questionId);
        String msg = "Something Wrong!";
        if (isDeleted) {
            msg = "Delete Successfully";
        }
        log.info("QuestionController::deleteQuestion, ✅");
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

}
