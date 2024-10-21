package com.guaguaupop.guaguaupop.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.findUserByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException(username + "no encontrado"));
    }

    public UserDetails loadUserById(Long id) throws UsernameNotFoundException{
        return userService.findById(id)
                .orElseThrow(()->new UsernameNotFoundException("Usuario " + id + " no encontrado"));
    }
}
