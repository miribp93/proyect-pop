package com.guaguaupop.guaguaupop.entity;

import com.guaguaupop.guaguaupop.dto.CityDTO;
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
public class City implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long idCity;

    @Column(length = 100, nullable = false)
    private String nameCity;

    @ManyToOne
    @JoinColumn(name = "idCountry", referencedColumnName = "idCountry")
    private Country country;

    public City(CityDTO cityDTO) {
        this.idCity = cityDTO.getIdCity();
        this.nameCity = cityDTO.getName();    }
}
