package com.guaguaupop.guaguaupop.security.jwt;

import com.guaguaupop.guaguaupop.dto.GetSimpleUserDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtUserResponse extends GetSimpleUserDTO {

    private String token;

    @Builder(builderMethodName = "jwtUserResponseBuilder")
    public JwtUserResponse(String username, byte[] profilePhoto, String token) {
        super(username, profilePhoto);
        this.token = token;
    }
}
