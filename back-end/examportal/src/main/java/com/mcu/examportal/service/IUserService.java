package com.mcu.examportal.service;

import com.mcu.examportal.model.UserModel;

public interface IUserService {
    public UserModel registerUser(UserModel userModel);
    public UserModel getUser(String userEmail);

    public String deleteUser(String email);
    public UserModel updateUserInfo(UserModel user);
}
