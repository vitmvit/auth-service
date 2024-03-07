package ru.clevertec.news.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.clevertec.news.model.dto.RoleName;

import java.util.Collection;
import java.util.List;

import static ru.clevertec.news.constant.Constant.*;

/**
 * /* Модель пользователя
 */
@Table()
@Entity(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;

    private String password;

    @Enumerated(EnumType.STRING)
    private RoleName role;

    /**
     * /* Конструктор с параметрами.
     *
     * @param login    логин пользователя
     * @param password пароль пользователя
     * @param role     роль пользователя
     */
    public User(String login, String password, RoleName role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    /**
     * /* {@inheritDoc}
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == RoleName.ADMIN) {
            return List.of(new SimpleGrantedAuthority(ADMIN_ROLE), new SimpleGrantedAuthority(USER_ROLE));
        }
        if (this.role == RoleName.JOURNALIST) {
            return List.of(new SimpleGrantedAuthority(JOURNALIST_ROLE), new SimpleGrantedAuthority(USER_ROLE));
        }
        if (this.role == RoleName.SUBSCRIBER) {
            return List.of(new SimpleGrantedAuthority(SUBSCRIBER_ROLE), new SimpleGrantedAuthority(USER_ROLE));
        }
        return List.of(new SimpleGrantedAuthority(USER_ROLE));
    }

    @Override
    public String getUsername() {
        return login;
    }

    /**
     /* {@inheritDoc}
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     /* {@inheritDoc}
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     /* {@inheritDoc}
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}