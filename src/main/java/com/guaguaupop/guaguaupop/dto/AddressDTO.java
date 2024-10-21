package com.guaguaupop.guaguaupop.dto;

import lombok.*;
import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddressDTO implements Serializable {
    private Long idAddress;
    private String street;
    private String postalCode;
    private Integer phone;
    private CityDTO cityDTO;
}
