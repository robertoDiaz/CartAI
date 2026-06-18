package com.bikemmerce.commerce.application.usecases.security.commands;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record CreateRoleCommand(

        @NotBlank(message = "Name is mandatory")
        String name,

        @NotNull
        Set<String> permissions

) {
}
