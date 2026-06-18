package com.bikemmerce.commerce.application.usecases.security.commands;

import com.bikemmerce.commerce.domain.model.security.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record CreateUserCommand(

        @NotBlank(message = "Name is mandatory")
        String name,

        @NotBlank(message = "Email is mandatory")
        String email,

        @NotBlank(message = "Password is mandatory")
        String password,

        @NotNull
        Set<Role> roles

) {
}
