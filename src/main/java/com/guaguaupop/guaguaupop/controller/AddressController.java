package com.guaguaupop.guaguaupop.controller;

import com.guaguaupop.guaguaupop.dto.AddressDTO;
import com.guaguaupop.guaguaupop.entity.Address;
import com.guaguaupop.guaguaupop.repository.AddressRepository;
import com.guaguaupop.guaguaupop.service.AddressService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Builder
@RestController
@Slf4j
@RequestMapping(value = "/api/address")
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class AddressController {

    private final AddressService addressService;
    private final AddressRepository addressRepository;

    @GetMapping
    public List<Address> getAddress() {
        return addressService.getAddresses();
    }

    @GetMapping("/{idAddress}")
    public Address findById(@PathVariable Long idAddress){
        return addressRepository.findById(idAddress).orElse(null);
    }

    @PostMapping("/findAddress")
    Page<Address> find(@RequestBody AddressDTO addressDTO, Pageable pageable){

        return addressService.searchAddress(addressDTO, pageable);

    }

    @PostMapping("/newAdress")
    public Address newAddress(@RequestBody Address address) {
        address = addressService.newAddress(address);
        return address;
    }

    @PutMapping("/{idAddress}")
    public Address updateAddress(@PathVariable Long idAddress, @RequestBody Address address) throws Exception {
        address = addressService.updateAddress(address);
        return address;
    }

    @DeleteMapping("/delete/{idAddress}")
    public ResponseEntity<String> remove(@PathVariable Long idAddress){
        try {
            Address address = addressRepository.getReferenceById(idAddress);
            addressService.remove(idAddress);
            log.info("Se ha borrado la dirección " + address.getStreet());
            return new ResponseEntity<>("Borrado", HttpStatus.OK);
        } catch (Exception e){
            log.error("Error al intentar eliminar la dirección");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al intentar eliminar la dirección");
        }
    }
}
