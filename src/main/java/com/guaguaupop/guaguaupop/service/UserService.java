package com.guaguaupop.guaguaupop.service;

import com.guaguaupop.guaguaupop.dto.CreateUserDTO;
import com.guaguaupop.guaguaupop.entity.*;
import com.guaguaupop.guaguaupop.exception.NewUserWithDifferentPasswordsException;
import com.guaguaupop.guaguaupop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService extends BaseService<User, Long, UserRepository> {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findUserByUsername(String username){
        return this.repository.findByUsername(username);
    }

    public User createUser(CreateUserDTO  createUserDto) {
        if (createUserDto.getPassword().contentEquals(createUserDto.getPassword2())) {
            User user = User.builder()
                    .username(createUserDto.getUsername())
                    .password(passwordEncoder.encode(createUserDto.getPassword()))
                    .name(createUserDto.getName())
                    .lastName1(createUserDto.getLastName1())
                    .lastName2(createUserDto.getLastName2())
                    .email(createUserDto.getEmail())
                    .phone(createUserDto.getPhone())
                    .street(createUserDto.getStreet())
                    .city(createUserDto.getCity())
                    .postalCode(createUserDto.getPostalCode())
                    .profilePhoto(createUserDto.getProfilePhoto())
                    .build();
            return save(user);
        } else {
            throw new NewUserWithDifferentPasswordsException();
        }
    }

    public Optional<User> getUser(Long id) {
        return this.repository.findById(id);
    }
}
