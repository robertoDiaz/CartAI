package com.bikemmerce.commerce.domain.model.security;

import com.bikemmerce.commerce.domain.model.security.value.objects.Email;
import com.bikemmerce.commerce.domain.model.security.value.objects.UserId;
import lombok.NonNull;

import java.util.Set;

public record User(@NonNull UserId userId, @NonNull String name, @NonNull Email email, @NonNull String passwordHash, @NonNull Set<Role> roles) {

}
