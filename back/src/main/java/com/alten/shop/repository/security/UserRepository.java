package com.alten.shop.repository.security;

import com.alten.shop.model.security.AuthorizedUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<AuthorizedUser, Long> {

    Optional<AuthorizedUser> findByEmail(String email);
}
