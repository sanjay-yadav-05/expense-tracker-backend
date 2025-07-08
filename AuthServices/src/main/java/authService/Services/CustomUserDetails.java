package authService.Services;

import authService.Entities.UserInfo;
import authService.Entities.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails extends UserInfo implements UserDetails {

    private final String userName;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(UserInfo userDetails){
        this.userName = userDetails.getUsername();
        this.password = userDetails.getPassword();
        List<GrantedAuthority> auth = new ArrayList<>();
        for(UserRole role : userDetails.getRoles()){
            auth.add(new SimpleGrantedAuthority(role.getName().toUpperCase()));
        }
        this.authorities = auth;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return authorities;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public boolean isAccountNonExpired(){
        return true;
    }

    public boolean  isAccountNonLocked(){
        return true;
    }

    public boolean isCredentialsNonExpired(){
        return true;
    }

    public boolean isEnabled(){
        return true;
    }
}
