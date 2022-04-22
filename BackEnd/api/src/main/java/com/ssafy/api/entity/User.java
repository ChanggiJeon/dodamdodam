package com.ssafy.api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user",
        indexes = {
                @Index(columnList = "id"),
                @Index(columnList = "user_id"),
                @Index(columnList = "name"),
                @Index(columnList = "password"),
                @Index(columnList = "birthday"),
                @Index(columnList = "fcm_token"),
                @Index(columnList = "refresh_token"),
                @Index(columnList = "authority")
        })
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Setter
    @Column(name = "user_id", nullable = false, unique = true, length = 20)
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
    @Column(name = "fcm_token")
    private String fcmToken;

    @Setter
    @Nullable
    @Column(name = "refresh_token")
    private String refreshToken;

    @Setter
    @Column(nullable = false, columnDefinition = "varchar(20)")
    private String authority;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton((GrantedAuthority) () -> authority);
    }

    public void updatePassword(String password) {
        this.password = password;
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
