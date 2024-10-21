package com.guaguaupop.guaguaupop.entity;

import com.guaguaupop.guaguaupop.dto.CountryDTO;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Builder
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Country implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long idCountry;

    @Column(length = 100, nullable = false)
    private String nameCountry;

    public Country(CountryDTO countryDTO) {
        this.idCountry = countryDTO.getIdCountry();
        this.nameCountry = countryDTO.getNameCountry();
    }
}
