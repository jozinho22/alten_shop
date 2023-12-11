package com.alten.shop.auth;

import com.alten.shop.model.security.Authority;
import com.alten.shop.model.security.AuthorizedUser;
import com.alten.shop.repository.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alten.shop.security.JWTService;

import java.util.*;

@Service
public class AuthService {

    private final UserRepository uRepo;
    private final PasswordEncoder passwordEncoder;
    private final JWTService JWTService;
    private AuthenticationManager authManager;

    @Autowired
    public AuthService(UserRepository uRepo,
                       PasswordEncoder passwordEncoder,
                       JWTService JWTService,
                       AuthenticationManager authManager) {
        this.uRepo = uRepo;
        this.passwordEncoder = passwordEncoder;
        this.JWTService = JWTService;
        this.authManager = authManager;
    }

    public ResponseEntity register(AuthRequest req) {

        if(uRepo.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity.
                    status(409).
                    body(new AuthResponse(null, "This mail is already taken"));
        }

        var user = new AuthorizedUser(
                req.getEmail(),
                passwordEncoder.encode(req.getPassword()),
                List.of(Authority.USER));

        uRepo.save(user);

        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", user.getAuthorities());

        var jwtToken = JWTService.generateToken(claims, user);
        System.out.println(jwtToken);
        
        return ResponseEntity
                .status(200)
                .body(new AuthResponse(jwtToken, "Your token is valid for 24h"));
    }

    public ResponseEntity authenticate(AuthRequest req) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getEmail(),
                        req.getPassword()
                )
        );
        var user = uRepo.findByEmail(req.getEmail());
        if(user.isEmpty()) {
            return ResponseEntity.
                    status(404).
                    body(new AuthResponse(null, "This user does not exist"));
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", user.get().getAuthorities());

        var jwtToken = JWTService.generateToken(claims, user.get());
        System.out.println(jwtToken);
        return ResponseEntity
                .status(200)
                .body(new AuthResponse(jwtToken, "Your token is valid for 24h"));
    }


}
