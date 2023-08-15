package com.mcu.examportal.service.impl;

import com.mcu.examportal.exception.IncompleteUserInfoException;
import org.apache.commons.lang3.StringUtils;
import com.mcu.examportal.entity.UserEntity;
import com.mcu.examportal.exception.UserNotFoundException;
import com.mcu.examportal.exception.UserNotRegisterException;
import com.mcu.examportal.model.LoginModel;
import com.mcu.examportal.model.UserModel;
import com.mcu.examportal.repository.UserRepository;
import com.mcu.examportal.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private PasswordEncoder encoder;
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //    <----------------- Register User------------------------->
    @Override
    public UserModel registerUser(UserModel userModel) {
        UserEntity userEntity = new UserEntity();
        UserModel userModel2 = new UserModel();

        if (StringUtils.isAnyEmpty(
                userModel.getFirstName(), userModel.getLastName(),
                userModel.getEmail(), userModel.getGender(),
                userModel.getPassword(), userModel.getDesignation())
                || userModel.getDob() == null) {
            throw new IncompleteUserInfoException("Incomplete signup information");
        }

        if (userRepository.findByEmail(userModel.getEmail()).isPresent()) {
            logger.info("UserServiceImpl:registerUser, Already Exist::" + userRepository.findByEmail(userModel.getEmail()));
            throw new UserNotRegisterException("Already Registered");
        } else {
            if (userModel.getDesignation().equalsIgnoreCase("teacher")) {
                userEntity.setRoles("TEACHER");
            } else if (userModel.getDesignation().equalsIgnoreCase("admin")) {
                userEntity.setRoles("ADMIN");
            } else {
                userEntity.setRoles("USER");
            }
            BeanUtils.copyProperties(userModel, userEntity);
            userEntity.setPassword(encoder.encode(userModel.getPassword()));
            logger.info("UserServiceImpl:registerUser, before saved::" + userEntity.toString());
            userEntity.setRandomPassword(generatePassword(8));
            // After setting the roles in userEntity based on userModel.getDesignation()
            userEntity.setUserPhoto("../../../assets/no-avatar.png");
            UserEntity savedUser = userRepository.save(userEntity);


            if (savedUser.getId() != null) {
                BeanUtils.copyProperties(savedUser, userModel2);
                userModel2.setDesignation(savedUser.getRoles());
                logger.info("UserServiceImpl:registerUser ✅::" + userModel2.toString());
                return userModel2;
            } else {
                throw new UserNotRegisterException("Fail to Register");
            }
        }
    }

    //    <----------------- get user info ------------------------->
    @Override
    public UserModel getUser(String userEmail) {
        UserModel userModel = new UserModel();
        logger.info("UserServiceImpl:getUser, email::" + userEmail);
        Optional<UserEntity> userEntity = userRepository.findByEmail(userEmail);
        if (userEntity.isPresent()) {
            BeanUtils.copyProperties(userEntity.get(), userModel);
            userModel.setDesignation(userEntity.get().getRoles());
            logger.info("UserServiceImpl:getUser, ✅::" + userModel.toString());
            return userModel;
        } else {
            throw new UserNotFoundException("User Not Found!");
        }

    }

    //    <----------------- delete user info------------------------->
    @Override
    public String deleteUser(String email) {
        logger.info("UserServiceImpl:deleteUser, email::" + email);
        Optional<UserEntity> user = userRepository.findByEmail(email);
        Long userId = null;
        if (user.isPresent()) {
            userId = user.get().getId();
            userRepository.deleteById(userId);
            logger.info("UserServiceImpl:deleteUser, ✅::");
            return "user Deleted Successfully";
        } else {
            throw new UserNotFoundException("User Not Found!");
        }
    }

    //    <----------------- update user info ------------------------->
    @Override
    public UserModel updateUserInfo(UserModel user) {
        UserEntity newUserEntity = new UserEntity();
        Optional<UserEntity> userEntity = userRepository.findByEmail(user.getEmail());
        if (userEntity.isPresent()) {
            logger.info("UserServiceImpl:updateUserInfo, 0️⃣::" + userEntity.toString());
            BeanUtils.copyProperties(user, newUserEntity);
            if (user.getDesignation().equalsIgnoreCase("teacher")) {
                newUserEntity.setRoles("TEACHER");
            } else if (user.getDesignation().equalsIgnoreCase("admin")) {
                newUserEntity.setRoles("ADMIN");
            } else {
                newUserEntity.setRoles("USER");
            }

            newUserEntity.setId(userEntity.get().getId());
            logger.info("UserServiceImpl:updateUserInfo, newEntity::" + newUserEntity.toString());

            UserEntity updateUser = userRepository.save(newUserEntity);
            UserModel updateModel = new UserModel();
            BeanUtils.copyProperties(updateUser, updateModel);
            updateModel.setDesignation(updateUser.getRoles());
            logger.info("UserServiceImpl:updateUserInfo, ✅::" + updateModel.toString());
            return updateModel;
        } else {
            throw new UserNotFoundException("User Not Found!");
        }
    }

    @Override
    public List<UserModel> allUser() {
        List<UserEntity> entityList = userRepository.findAll();
        List<UserModel> allModel = new ArrayList<>();
        for (UserEntity singleUser : entityList) {
            UserModel tempModel = new UserModel();
            BeanUtils.copyProperties(singleUser, tempModel);
            tempModel.setDesignation(singleUser.getRoles());
            allModel.add(tempModel);
        }
        return allModel;
    }

    @Override
    public boolean login(LoginModel loginInfo) {
        if (loginInfo == null || loginInfo.getEmail() == null || loginInfo.getEmail().equals("") || loginInfo.getPassword() == null || loginInfo.getPassword().equals("")) {
            return false; // Return false for incomplete user info
        }
        logger.info("UserServiceImpl::login, ❓loginInfo found::" + true);

        String email = loginInfo.getEmail();
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);

        if (userEntity.isPresent()) {
            logger.info("UserServiceImpl::login, ❓loginInfo exists::" + true);
            logger.info("UserServiceImpl::login, ❓logging pwd match::" + encoder.matches(loginInfo.getPassword(), userEntity.get().getPassword()));

            // Return false for unsuccessful login
            return encoder.matches(loginInfo.getPassword(), userEntity.get().getPassword()); // Return true for successful login
        } else {
            logger.info("UserServiceImpl::login, ❓loginInfo exists::" + false);
            return false; // User not found
        }
    }



    @Override
    public String forget(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IncompleteUserInfoException("Email can't be null or empty");
        }
        logger.info("UserServiceImpl::forget, ❓request email::" + email);

        Optional<UserEntity> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("Email not found!");
        }

        UserEntity user = userOptional.get();
        logger.info("UserServiceImpl::forget, ❓request email found::" + true);
        logger.info("UserServiceImpl::forget, 👻entity before::" + user.toString());

        // Generate a new random password and set it to the user entity
        String randomPassword = generatePassword(8);
        user.setRandomPassword(randomPassword);

        // Set the encoded version of the new random password as the user's password
        user.setPassword(encoder.encode(randomPassword));

        // Save the updated user entity to the repository
        UserEntity updateUserPassword = userRepository.save(user);
        logger.info("UserServiceImpl:forget, After update password::" + updateUserPassword.toString());
        logger.info("UserServiceImpl::forget, 👻entity after::" + updateUserPassword.toString());
        logger.info("PWD::" + randomPassword);

        return randomPassword;
    }

    public String generatePassword(int len) {
        String capitalLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String smallLetters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialCharacters = "!@#$%^&*_=+-?<>";

        String values = capitalLetters + smallLetters + numbers + specialCharacters;

        Random random = new Random();

        char[] password = new char[len];

        for (int i = 0; i < len; i++) {
            password[i] = values.charAt(random.nextInt(values.length()));
        }

        // Ensure the password contains at least one uppercase letter, one lowercase letter, one digit, and one special character
        password[0] = capitalLetters.charAt(random.nextInt(capitalLetters.length()));
        password[1] = smallLetters.charAt(random.nextInt(smallLetters.length()));
        password[2] = numbers.charAt(random.nextInt(numbers.length()));
        password[3] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));

        String pwd = new String(password);  // Convert the char array to a string
        logger.info("Random password🔑::" + pwd);
        return pwd;
    }



}
