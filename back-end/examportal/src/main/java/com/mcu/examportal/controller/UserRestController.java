package com.mcu.examportal.controller;

import com.mcu.examportal.model.UserModel;
import com.mcu.examportal.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class UserRestController {
    @Autowired
    private IUserService userService;

//    <--------------------- register the user ------------------->
    @PostMapping("/signup")
    public ResponseEntity<UserModel> signUp(@RequestBody UserModel userModel){
        UserModel registerUser = userService.registerUser(userModel);
        return new ResponseEntity<>(registerUser, HttpStatus.CREATED);
    }

//    <-------------------- Find user by Email ------------------>
    @GetMapping("/{userEmail}")
    public  ResponseEntity<UserModel> findUserByEmail(@PathVariable String userEmail){
        UserModel user = userService.getUser(userEmail);
        return new ResponseEntity<>(user, HttpStatus.FOUND);
    }
}
