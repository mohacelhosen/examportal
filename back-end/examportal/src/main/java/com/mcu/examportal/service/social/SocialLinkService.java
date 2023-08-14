package com.mcu.examportal.service.social;

import com.mcu.examportal.entity.SocialLinkEntity;
import com.mcu.examportal.repository.SocialLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SocialLinkService {
    @Autowired
    private SocialLinkRepository socialLinkRepository;
    private boolean save(SocialLinkEntity socialLink){
        SocialLinkEntity save = socialLinkRepository.save(socialLink);
        return save.getUserProfileId() != null;
    }
}
