package com.guaguaupop.guaguaupop.controller;

import com.guaguaupop.guaguaupop.dto.CreateUserDTO;
import com.guaguaupop.guaguaupop.dto.GetSimpleUserDTO;
import com.guaguaupop.guaguaupop.dto.UserDTOConverter;
import com.guaguaupop.guaguaupop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserDTOConverter userDTOConverter;

    @PostMapping("/")
    public GetSimpleUserDTO createUser(@RequestBody CreateUserDTO newUser){
        return userDTOConverter.convertUserToGetUserDTO(userService.createUser(newUser));
    }

}
