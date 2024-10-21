package com.guaguaupop.guaguaupop.dto;

import lombok.*;
import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CityDTO implements Serializable {
    private Long idCity;
    private String name;
    private CountryDTO countryDTO;
}
