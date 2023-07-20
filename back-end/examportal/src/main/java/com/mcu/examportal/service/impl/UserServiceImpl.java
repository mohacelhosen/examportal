package com.mcu.examportal.service.impl;

import com.mcu.examportal.entity.UserEntity;
import com.mcu.examportal.exception.UserNotFoundException;
import com.mcu.examportal.exception.UserNotRegisterException;
import com.mcu.examportal.model.UserModel;
import com.mcu.examportal.repository.UserRepository;
import com.mcu.examportal.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {
    private final Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;

//    <----------------- Register User------------------------->
    @Override
    public UserModel registerUser(UserModel userModel) {
        UserEntity userEntity = new UserEntity();
        UserModel userModel2= new UserModel();

        if (userRepository.findByEmail(userModel.getEmail()).isPresent()){
            logger.info("UserServiceImpl:registerUser, Already Exist::"+userRepository.findByEmail(userModel.getEmail()));
            throw new UserNotRegisterException("Already Registered");
        }else{
            if (userModel.getDesignation().equalsIgnoreCase("teacher")){
                userEntity.setRoles("teacher");
            }else {
                userEntity.setRoles("student");
            }
            userEntity.setEnabled(true);
            BeanUtils.copyProperties(userModel, userEntity);
            logger.info("UserServiceImpl:registerUser, before saved::"+userEntity.toString());
            UserEntity savedUser = userRepository.save(userEntity);
            if (savedUser.getId()!=null){
                BeanUtils.copyProperties(savedUser,userModel2);
                userModel2.setDesignation(savedUser.getRoles());
                logger.info("UserServiceImpl:registerUser ✅::"+userModel2.toString());
                return userModel2;
            }else {
                throw new UserNotRegisterException("Fail to Register");
            }
        }
    }

    //    <----------------- get user info ------------------------->
    @Override
    public UserModel getUser(String userEmail) {
        UserModel userModel = new UserModel();
        logger.info("UserServiceImpl:getUser, email::"+userEmail.toString());
        Optional<UserEntity> userEntity = userRepository.findByEmail(userEmail);
        if (userEntity.isPresent()){
            BeanUtils.copyProperties(userEntity.get(), userModel);
            userModel.setDesignation(userEntity.get().getRoles());
            logger.info("UserServiceImpl:getUser, ✅::"+userModel.toString());
            return userModel;
        }else {
            throw new UserNotFoundException("User Not Found!");
        }

    }

    //    <----------------- delete user info------------------------->
    @Override
    public String deleteUser(String email) {
        logger.info("UserServiceImpl:deleteUser, email::"+email.toString());
        Optional<UserEntity> user = userRepository.findByEmail(email);
        Long userId=null;
        if (user.isPresent()){
            userId = user.get().getId();
            userRepository.deleteById(userId);
            logger.info("UserServiceImpl:deleteUser, ✅::");
            return "user Deleted Successfully";
        }else {
            throw new UserNotFoundException("User Not Found!");
        }
    }

    //    <----------------- update user info ------------------------->
    @Override
    public UserModel updateUserInfo(UserModel user) {
        UserEntity newUserEntity= new UserEntity();
        Optional<UserEntity> userEntity = userRepository.findByEmail(user.getEmail());
        if (userEntity.isPresent()){
            logger.info("UserServiceImpl:updateUserInfo, 0️⃣::"+userEntity.toString());
            BeanUtils.copyProperties(user,newUserEntity);
            if (user.getDesignation().equalsIgnoreCase("teacher")){
                newUserEntity.setRoles("teacher");
            }else {
                newUserEntity.setRoles("student");
            }
            newUserEntity.setEnabled(userEntity.get().isEnabled());
            newUserEntity.setId(userEntity.get().getId());
            logger.info("UserServiceImpl:updateUserInfo, newEntity::"+newUserEntity.toString());

            UserEntity updateUser = userRepository.save(newUserEntity);
            UserModel updateModel = new UserModel();
            BeanUtils.copyProperties(updateUser, updateModel);
            updateModel.setDesignation(updateUser.getRoles());
            logger.info("UserServiceImpl:updateUserInfo, ✅::"+updateModel.toString());
            return  updateModel;
        }else {
            throw new UserNotFoundException("User Not Found!");
        }
    }


}
