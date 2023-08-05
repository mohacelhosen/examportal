package com.mcu.examportal.service;

import com.mcu.examportal.entity.exam.Question;
import com.mcu.examportal.entity.exam.Quiz;

import java.util.Set;

public interface QuestionService {
    public Question addQuestion(Question question);
    public Question updateQuestion(Question question);
    public Question getQuestion(Integer questionId);
    public Set<Question> questionList();
    public Set<Question> getQuestionOfQuiz(Quiz quiz);
    public Boolean deleteQuestion(Integer questionId);
}
