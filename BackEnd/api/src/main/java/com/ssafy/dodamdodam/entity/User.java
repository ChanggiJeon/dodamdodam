package com.ssafy.dodamdodam.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class User extends BaseEntity implements UserDetails {

    @Column(nullable = false, unique = true, length = 20)
    private String userId;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false)
    private String password;

    @Nullable
    private LocalDateTime birthday;

    @Nullable
    private String fcmToken;

    @Nullable
    private String refreshToken;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
