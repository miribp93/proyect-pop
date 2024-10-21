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
public class UserService extends BaseService<User, Long, UserRepository>{

    private final PasswordEncoder passwordEncoder;

    public Optional<User> findUserByUsername(String username){
        return this.repository.findByUsername(username);
    }

    public User createUser(CreateUserDTO newUser) {
        if (newUser.getPassword().contentEquals(newUser.getPassword2())) {
            User user = User.builder()
                    .username(newUser.getUsername())
                    .password(passwordEncoder.encode(newUser.getPassword()))
                    .name(newUser.getName())
                    .lastName1(newUser.getLastName1())
                    .lastName2(newUser.getLastName2())
                    .email(newUser.getEmail())
                    .phone(newUser.getPhone())
                    .address(new Address(newUser.getAddressDTO())) // Pasa el DTO
                    .city(new City(newUser.getCityDTO())) // Pasa el DTO
                    .country(new Country(newUser.getCountryDTO())) // Pasa el DTO
                    .postalCode(new PostalCode(newUser.getPostalCodeDTO())) // Pasa el DTO
                    .profilePhoto(newUser.getProfilePhoto())
                    .build();
            return save(user);
        } else {
            throw new NewUserWithDifferentPasswordsException();
        }
    }
}
