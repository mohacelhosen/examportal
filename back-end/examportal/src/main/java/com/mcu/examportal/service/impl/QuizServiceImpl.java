package com.mcu.examportal.service.impl;

import com.mcu.examportal.entity.exam.Quiz;
import com.mcu.examportal.exception.ResourceNotFoundException;
import com.mcu.examportal.repository.QuizRepository;
import com.mcu.examportal.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
@Service
public class QuizServiceImpl implements QuizService {
    @Autowired
    private QuizRepository quizRepository;
    @Override
    public Quiz addQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    @Override
    public Quiz updateQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    @Override
    public Quiz getQuiz(Integer quizId) {
        Optional<Quiz> quiz = quizRepository.findById(quizId);
        if (quiz.isPresent()) {
            return quiz.get();
        } else {
            throw new ResourceNotFoundException("This Quiz Not Found!, ID::"+quizId);
        }
    }

    @Override
    public Set<Quiz> quizSet() {
        return new LinkedHashSet<>(quizRepository.findAll());
    }

    @Override
    public Boolean deleteQuiz(Integer quizId) {
        Optional<Quiz> quiz = quizRepository.findById(quizId);
        if (quiz.isPresent()) {
            quizRepository.delete(quiz.get());
            return true;
        } else {
            throw new ResourceNotFoundException("This Quiz Not Found!, ID::"+quizId);
        }
    }
}
