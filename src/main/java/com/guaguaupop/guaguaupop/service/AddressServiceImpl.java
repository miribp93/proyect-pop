package com.guaguaupop.guaguaupop.service;

import com.guaguaupop.guaguaupop.dto.AddressDTO;
import com.guaguaupop.guaguaupop.entity.Address;
import com.guaguaupop.guaguaupop.repository.AddressRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AddressServiceImpl implements AddressService{

    private AddressRepository addressRepository;

    @Override
    public List<Address> getAddresses() {
        return addressRepository.findAll();
    }

    @Override
    public Page<Address> searchAddress(AddressDTO filter, Pageable pageable) {
        Specification<Address> specification = Specification.where(null);

        if (filter.getPostalCode() != null && !filter.getPostalCode().isEmpty()){
            specification = specification.and((root, query, criteriaBuilder)
                    ->criteriaBuilder.like(root.get("postalCode"), "%" + filter.getPostalCode() + "%"));
        }

        if (filter.getCityDTO() != null){
            Long idCity = filter.getCityDTO().getIdCity();
            specification = specification.and(((root, query, criteriaBuilder)
                    -> criteriaBuilder.equal(root.get("city").get("idCity"), idCity) ));
        }

        return addressRepository.findAll(specification, pageable);
    }

    @Override
    public Address newAddress(Address address) {
        return null;
    }

    @Override
    public Address updateAddress(Address address) throws Exception {
        return null;
    }

    @Override
    public void remove(Long id) {

    }
}
