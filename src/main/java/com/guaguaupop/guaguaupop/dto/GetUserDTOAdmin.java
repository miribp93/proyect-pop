package com.guaguaupop.guaguaupop.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetUserDTOAdmin {

    private String username;
    private String password;
    private String name;
    private String lastName1;
    private String lastName2;
    private String email;
    private Integer phone;
    private Long addressId;
    private Long cityId;
    private Long countryId;
    private Long postalCodeId;
    private byte[] profilePhoto;
}
