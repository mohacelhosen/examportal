package com.mcu.examportal.controller;

import com.mcu.examportal.model.UserModel;
import com.mcu.examportal.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class UserRestController {
    Logger logger = LoggerFactory.getLogger(UserRestController.class);
    @Autowired
    private IUserService userService;

//    <--------------------- register the user ------------------->
    @PostMapping("/signup")
    public ResponseEntity<UserModel> signUp(@RequestBody UserModel userModel){
        UserModel registerUser = userService.registerUser(userModel);
        logger.info("UserRestController:signUp, model::"+registerUser.toString());
        return new ResponseEntity<>(registerUser, HttpStatus.CREATED);
    }

//    <-------------------- Find user by Email ------------------>
    @GetMapping("/find/{userEmail}")
    public  ResponseEntity<UserModel> findUserByEmail(@PathVariable String userEmail){
        UserModel user = userService.getUser(userEmail);
        logger.info("✅UserRestController:findUserByEmail, user::"+user.toString());
        return new ResponseEntity<>(user, HttpStatus.FOUND);
    }

//    <-------------------- update user information ------------------>
    @PutMapping("/update")
    public  ResponseEntity<UserModel> updateUserInfo(@RequestBody UserModel user){
        UserModel updateUserInfo = userService.updateUserInfo(user);
        logger.info("✅UserRestController:updateUserInfo, model::"+user.toString());
        return new ResponseEntity<>(updateUserInfo, HttpStatus.OK);
    }

//    <-------------------- update user information ------------------>
    @DeleteMapping("/delete/{userEmail}")
    public ResponseEntity<String> deleteUser(@PathVariable String userEmail){
        String message = userService.deleteUser(userEmail);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/users/all")
    public ResponseEntity<List<UserModel>> allUsers(){
        List<UserModel> userModels = userService.allUser();
        logger.info("✅UserRestController:allUsers");
        return  new ResponseEntity<>(userModels, HttpStatus.OK);
    }

}
