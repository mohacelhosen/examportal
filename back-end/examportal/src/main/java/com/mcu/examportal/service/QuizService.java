package com.mcu.examportal.service;

import com.mcu.examportal.entity.exam.Quiz;

import java.util.Set;

public interface QuizService {
    public Quiz addQuiz(Quiz quiz);
    public Quiz updateQuiz(Quiz quiz);
    public Quiz getQuiz(Integer quizId);
    public Set<Quiz> quizSet();
    public Boolean deleteQuiz(Integer quizId);
}
