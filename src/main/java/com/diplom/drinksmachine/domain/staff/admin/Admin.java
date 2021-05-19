package com.diplom.drinksmachine.domain.staff.admin;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
@Data
@Table(name = "staff")
public class Admin implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    private String firstName;
    private String secondName;
    private String thirdName;

    private String username;
    private String password;
    private boolean active;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "staff_role", joinColumns = @JoinColumn(name = "staff_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    public Admin() {}

    public Admin(String firstName,
                 String secondName,
                 String thirdName,
                 String username,
                 String password
    ) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.thirdName = thirdName;
        this.username = username;
        this.password = password;
        this.active = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
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
        return isActive();
    }
}
