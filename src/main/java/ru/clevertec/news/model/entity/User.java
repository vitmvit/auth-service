package ru.clevertec.news.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import ru.clevertec.news.model.dto.RoleName;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Entity(name = "users")
@EqualsAndHashCode(of = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;

    private String password;

    @Enumerated(EnumType.STRING)
    private RoleName role;

    public User(String login, String password, RoleName role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }
}