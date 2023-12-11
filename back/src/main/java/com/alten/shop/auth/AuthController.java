package com.alten.shop.auth;

import com.alten.shop.model.Product;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRequest req, Errors errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity("Check if your email is valid (....@...)", HttpStatus.BAD_REQUEST);
        }
        AuthResponse authResponse = authService.register(req);
        return ResponseEntity
                .status(authResponse.getHttpStatus())
                .body(authResponse);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> auth(@Valid @RequestBody AuthRequest req, Errors errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity("Check if your email is valid (....@...)", HttpStatus.BAD_REQUEST);
        }

        AuthResponse authResponse = authService.authenticate(req);
        return ResponseEntity
                .status(authResponse.getHttpStatus())
                .body(authResponse);
    }
}
