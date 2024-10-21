package com.guaguaupop.guaguaupop.dto;

import lombok.*;
import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CountryDTO implements Serializable {
    private Long idCountry;
    private String nameCountry;
}
