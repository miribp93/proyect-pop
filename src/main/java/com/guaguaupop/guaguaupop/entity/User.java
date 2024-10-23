package com.guaguaupop.guaguaupop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"user\"")
@Entity
public class User implements UserDetails {

    private static final long serialVersionUID = -2450942177410990788L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long idUser;

    @Column(name = "username", unique = true)
    private String username;

    private String password;
    private String password2;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "lastName1", nullable = false)
    private String lastName1;

    @Column(name = "lastName2", nullable = false)
    private String lastName2;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone", nullable = false, length = 15)
    private Integer phone;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "postalCode", nullable = false)
    @Pattern(regexp = "\\d{5}", message = "El código postal debe tener 5 dígitos")
    private Integer postalCode;

    @Lob
    @Column
    private byte[] profilePhoto;

    @OneToMany(mappedBy = "user")
    private List<Ad> services;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<UserRole> userRole;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userRole.stream().map(ur -> new SimpleGrantedAuthority("ROLE_" + ur.name())).collect(Collectors.toList());
    }

    @CreatedDate
    private LocalDateTime localDateTime;

    @Builder.Default
    private LocalDateTime lastPasswordChangeAt = LocalDateTime.now();

    //

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    /*@ManyToOne
    @JoinColumn(name = "idAddress", referencedColumnName = "idAddress")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "idCity", referencedColumnName = "idCity")
    private City city;

    @ManyToOne
    @JoinColumn(name = "idCountry", referencedColumnName = "idCountry")
    private Country country;

    @ManyToOne
    @JoinColumn(name = "idPostalCode", referencedColumnName = "idPostalCode")
    private PostalCode postalCode;

    @OneToMany(mappedBy = "sender")
    private List<Message> messagesSent;

    @OneToMany(mappedBy = "receiver")
    private List<Message> messagesReceived;

    @OneToMany(mappedBy = "user")
    private List<Ad> products;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }*/


}
