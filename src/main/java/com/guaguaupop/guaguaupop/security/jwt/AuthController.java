package com.guaguaupop.guaguaupop.security.jwt;

import com.guaguaupop.guaguaupop.dto.LoginUserDTO;
import com.guaguaupop.guaguaupop.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    //  public JwtUserResponse login(@Validated @RequestBody LoginRequest loginRequest) { -> sustituyo por RequestUserDTO
    @PostMapping("/auth/login")
    public JwtUserResponse login(@Validated @RequestBody LoginUserDTO loginUserDTO) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserDTO.getUsername(),
                        loginUserDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(auth);

        User user = (User) auth.getPrincipal();

        String jwtToken = jwtTokenUtil.generateToken(auth);

        return convertUserEntityAndTokenToJwtUserResponse(user, jwtToken);
    }

    private JwtUserResponse convertUserEntityAndTokenToJwtUserResponse(User user, String jwtToken) {
        return JwtUserResponse.jwtUserResponseBuilder()
                .username(user.getUsername())
                .profilePhoto(user.getProfilePhoto())
                .token(jwtToken)
                .build();
    }

}
