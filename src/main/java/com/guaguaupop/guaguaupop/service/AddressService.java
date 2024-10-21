package com.guaguaupop.guaguaupop.service;

import com.guaguaupop.guaguaupop.dto.AddressDTO;
import com.guaguaupop.guaguaupop.entity.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AddressService {

    public List<Address> getAddresses();

    public Page<Address> searchAddress(AddressDTO addressDTO, Pageable pageable);

    public Address newAddress(Address address);

    public Address updateAddress(Address address) throws Exception;

    void remove(Long id);
}
