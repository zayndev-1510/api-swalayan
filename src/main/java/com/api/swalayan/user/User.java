package com.api.swalayan.user;

import com.api.swalayan.roles.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "tbl_users")
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true, nullable = false,length = 50)
    private String username;
    @Column(nullable = false,length = 50)
    private String password;

    @Column(nullable = false,length = 50,name = "firstname")
    private String firstName;
    @Column(nullable = false,length = 50,name = "lastname")
    private String lastName;
    @Column(nullable = false,length = 100,name = "email",unique = true)
    private String email;
    @Column(nullable = false,length = 12)
    private String phone;
    @Column(nullable = false,length = 100)
    private String address;
    private Roles role;

    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private Timestamp createdAt;
    @UpdateTimestamp
    @Column(nullable = false,updatable = true)
    private Timestamp updatedAt;

    private String token;
    @Column(nullable = false,name = "refresh_token")
    private String refreshToken;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
