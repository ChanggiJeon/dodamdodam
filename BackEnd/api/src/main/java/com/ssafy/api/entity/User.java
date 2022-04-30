package com.ssafy.api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userPk;

    @Setter
    @Column(nullable = false, unique = true, length = 20)
    private String userId;

    @Column(nullable = false, length = 10)
    private String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // 읽기 X
    @Column(nullable = false)
    private String password;

    @Nullable
    @Setter
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthday;

    @Setter
    @Nullable
    private String fcmToken;

    @Setter
    @Nullable
    private String refreshToken;

    @Setter
    @Column(nullable = false, columnDefinition = "varchar(20)")
    private String authority;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton((GrantedAuthority) () -> authority);
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return String.valueOf(this.userPk);
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
