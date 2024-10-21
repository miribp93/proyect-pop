package com.guaguaupop.guaguaupop.dto;

import lombok.*;
import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostalCodeDTO implements Serializable {
    private Long idPostalCode;
    private String code;
    private CityDTO cityDTO;
}
