package com.Carrot.CR_Model;

import com.Carrot.Role.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Setter
@Builder
public class CarrotUser implements UserDetails {
    private String id;
    private String password;
    private String phone;
    private String nickName;
    private long temperature;
    private String image;
    private Role role;

    public CarrotUser(String id, String password, String phone, String nickName, long temperature, String image, Role role) {
        this.id = id;
        this.password = password;
        this.phone = phone;
        this.nickName = nickName;
        this.temperature = temperature;
        this.image = image;
        this.role = role;
    }

    public String toString() {
        return "{" +
                "\"id\" : " + "\""+id+"\"," +
                "\"password\" : " + "\""+password+"\"," +
                "\"phone\" : " + "\""+phone+"\"," +
                "\"nickName\" : " + "\""+nickName+"\"," +
                "\"temperature\" : " + "\""+temperature+"\"," +
                "\"image\" : " + "\""+image+"\"," +
                "\"role\" : " + "\""+role+"\"" +
                "}";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return null;
    }

    @Override
    public String getUsername() {
        return nickName;
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
