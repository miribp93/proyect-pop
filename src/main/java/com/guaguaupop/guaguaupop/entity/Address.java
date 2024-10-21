package com.guaguaupop.guaguaupop.entity;

import com.guaguaupop.guaguaupop.dto.AddressDTO;
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
public class Address implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long idAddress;

    @Column(length = 100)
    private String street;

    @Column(length = 10)
    private String postalCode;

    @Column(length = 12)
    private Integer phone;

    @ManyToOne
    @JoinColumn(name = "idCity", referencedColumnName = "idCity")
    private City city;

    public Address(AddressDTO addressDTO) {
        this.idAddress = addressDTO.getIdAddress();
        this.street = addressDTO.getStreet();
        this.postalCode = addressDTO.getPostalCode();
        this.phone = addressDTO.getPhone();
        this.city = new City(addressDTO.getCityDTO());
    }


}
