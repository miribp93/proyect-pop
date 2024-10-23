package com.guaguaupop.guaguaupop.controller;

import com.guaguaupop.guaguaupop.dto.*;
import com.guaguaupop.guaguaupop.entity.User;
import com.guaguaupop.guaguaupop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    //private final ProductService productService;
    //private final ProductoDTOConverter productoDTOConverter;
    private final UserDTOConverter userDTOConverter;

    @PostMapping("/register")
    public ResponseEntity<CreateUserDTO> createUser(@RequestBody CreateUserDTO userDTO) {
        return ResponseEntity.ok(userDTOConverter
                .convertUserToCreateUserDTO(this.userService.createUser(userDTO)));
    }

    @GetMapping("/profile")
    public ResponseEntity<GetUserDTOAdmin> me(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userDTOConverter.convertUserToGetUserDTOProfile(user));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal User user) {
        this.userService.delete(user);
        return ResponseEntity.noContent().build();
    }

    /*
    @GetMapping("/myAds")
    public ResponseEntity<Page<AdDTO>> myProducts(@AuthenticationPrincipal User user, Pageable pageable) {
        Page<AdDTO> myProducts = (this.productService.findByUser(user, pageable))
                .map(product -> adDTOConverter.convertToGetProduct(product, user));
        return ResponseEntity.ok().body(myProducts);
    }

    @GetMapping("/products")
    public ResponseEntity<Page<AdDTO>> otherProducts(@AuthenticationPrincipal User user, Pageable pageable) {
        Page<AdDTO> products = (this.productService.findOthers(user, pageable))
                .map(product -> adDTOConverter.convertToGetProduct(product, product.getUser()));
        return ResponseEntity.ok().body(products);
    }*/
}
