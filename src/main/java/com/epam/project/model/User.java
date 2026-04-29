package com.epam.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;

    private String firstName;

    private String lastName;

    private String username;

    @ToString.Exclude
    private String password;

    private Boolean isActive;
}
