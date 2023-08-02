package com.mcu.examportal.service.details;

import com.mcu.examportal.entity.UserEntity;
import com.mcu.examportal.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    Logger logger= LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> userInfo = repository.findByEmail(email);
        logger.info("UserDetailsServiceImpl::loadUserByUsername, "+userInfo.toString());
        return userInfo.orElseThrow(() -> new UsernameNotFoundException("user not found " + email));
    }
}
