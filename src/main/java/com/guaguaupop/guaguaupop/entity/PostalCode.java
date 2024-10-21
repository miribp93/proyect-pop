package com.guaguaupop.guaguaupop.entity;

import com.guaguaupop.guaguaupop.dto.CityDTO;
import com.guaguaupop.guaguaupop.dto.PostalCodeDTO;
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
public class PostalCode implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPostalCode;

    @Column(length = 10, nullable = false, unique = true)
    private String code;

    @ManyToOne
    @JoinColumn(name = "idCity", referencedColumnName = "idCity")
    private CityDTO city;

    public PostalCode(PostalCodeDTO postalCodeDTO) {
        this.idPostalCode = postalCodeDTO.getIdPostalCode();
        this.code = postalCodeDTO.getCode();
        this.city = postalCodeDTO.getCityDTO();
    }
}
