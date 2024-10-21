package com.guaguaupop.guaguaupop.dto;

import com.guaguaupop.guaguaupop.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserDTOConverter {

    public GetSimpleUserDTO convertUserToGetUserDTO(User user){
        return GetSimpleUserDTO.builder()
                .username(user.getUsername())
                .profilePhoto(user.getProfilePhoto()).build();

    }
}
