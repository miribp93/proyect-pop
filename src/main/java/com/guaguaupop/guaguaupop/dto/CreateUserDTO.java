package com.guaguaupop.guaguaupop.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDTO {

    private String username;
    private String password;
    private String password2;
    private String name;
    private String lastName1;
    private String lastName2;
    private String email;
    private Integer phone;
    private AddressDTO addressDTO;
    private CityDTO cityDTO;
    private CountryDTO countryDTO;
    private PostalCodeDTO postalCodeDTO;
    private byte[] profilePhoto;
}
