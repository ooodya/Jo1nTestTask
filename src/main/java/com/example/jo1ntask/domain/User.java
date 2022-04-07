package com.example.jo1ntask.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "testUser")
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(exclude = "id")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String login;
    @NotBlank
    private String password;
    private String description;

    public User(String firstName, String lastName, String login, String password, String description) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.password = password;
        this.description = description;
    }
}

