package com.mcu.examportal.controller;

import com.mcu.examportal.entity.UserEntity;
import com.mcu.examportal.exception.InvalidRequestException;
import com.mcu.examportal.exception.TokenValidationException;
import com.mcu.examportal.model.LoginModel;
import com.mcu.examportal.model.Token;
import com.mcu.examportal.model.UserModel;
import com.mcu.examportal.repository.UserRepository;
import com.mcu.examportal.service.IUserService;
import com.mcu.examportal.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/exam-portal")
public class UserRestController {
    Logger logger = LoggerFactory.getLogger(UserRestController.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private IUserService userService;

    //    <--------------------- register the user ------------------->
    @PostMapping("/auth/signup")
    public ResponseEntity<UserModel> signUp(@RequestBody UserModel userModel) {
        logger.info("UserRestController::signUp, 🤖Model=>" + userModel.toString());
        UserModel registerUser = userService.registerUser(userModel);
        logger.info("UserRestController:signUp, 🤖model::" + registerUser.toString());

        // Generate the JWT token with roles information
        String jwtToken = jwtService.generateToken(registerUser.getEmail());
        logger.info("UserRestController::signup, ⭕Token::" + jwtToken);
        return new ResponseEntity<>(registerUser, HttpStatus.CREATED);
    }

    //    <-------------------- Find user by Email ------------------>
    @GetMapping("/find/{userEmail}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN','TEACHER')")
    public ResponseEntity<UserModel> findUserByEmail(@PathVariable String userEmail) {
        UserModel user = userService.getUser(userEmail);
        logger.info("✅UserRestController:findUserByEmail, user::" + user.toString());
        return new ResponseEntity<>(user, HttpStatus.FOUND);
    }

    //    <-------------------- update user information ------------------>
    @PutMapping("/update")
    public ResponseEntity<UserModel> updateUserInfo(@RequestBody UserModel user) {
        UserModel updateUserInfo = userService.updateUserInfo(user);
        logger.info("✅UserRestController:updateUserInfo, model::" + user.toString());
        return new ResponseEntity<>(updateUserInfo, HttpStatus.OK);
    }

    //    <-------------------- delete user information ------------------>
    @DeleteMapping("/delete/{userEmail}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN','TEACHER')")
    public ResponseEntity<String> deleteUser(@PathVariable String userEmail) {
        String message = userService.deleteUser(userEmail);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    //    <------------------------ get all user info --------------------->

    @GetMapping("/users/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserModel>> allUsers() {
        List<UserModel> userModels = userService.allUser();
        logger.info("✅UserRestController:allUsers");
        return new ResponseEntity<>(userModels, HttpStatus.OK);
    }

    //    <---------------------- login -------------------------->
    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginModel loginInfo) {
        boolean login = userService.login(loginInfo);
        Map<String, String> response = new HashMap<>();

        if (login) {
            response.put("message", "Login Successful😇");
            response.put("token", jwtService.generateToken(loginInfo.getEmail()));
        } else {
            response.put("message", "Login Fail😵");
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    //    <----------- forget password ---------->
    @GetMapping("/forget/{email}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN','TEACHER')")
    public ResponseEntity<String> forgetPassword(@PathVariable String email) {
        String forget = userService.forget(email);
        return new ResponseEntity<>(forget, HttpStatus.OK);
    }


    @PostMapping("/auth/authenticate")
    public String authenticateAndGetToken(@RequestBody LoginModel authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getEmail());
        } else {
            throw new UsernameNotFoundException("Invalid email or password!");
        }

    }

    @PostMapping("/auth/token")
    public ResponseEntity<Boolean> tokenValidationUrl(@RequestBody Token token) {
        try {
            Boolean validateToken = false; // Default value for failed validation
            String userEmail = jwtService.extractUserEmail(token.getToken());
            if (userEmail != null) {
                Optional<UserEntity> user = userRepository.findByEmail(userEmail);
                if (user.isPresent()) {
                    validateToken = jwtService.validateToken(token.getToken(), user.get());
                }
            }
            return new ResponseEntity<>(validateToken, HttpStatus.OK);
        } catch (TokenValidationException e) {
            // Handle token validation exception here
            // Return a false response or appropriate error response
            logger.info("⁉️"+e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.OK);
        } catch (Exception e) {
            // Handle any other exceptions that might occur
            // Return an error response or appropriate error status
            logger.info("⁉️"+e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
