package com.alten.shop.config;

import com.alten.shop.model.security.Authority;
import com.alten.shop.repository.security.UserRepository;
import com.alten.shop.security.JWTAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JWTAuthFilter JWTAuthFilter;
    private final AuthenticationProvider authProvider;
    private final UserRepository uRepo;

    public SecurityConfig(
            JWTAuthFilter JWTAuthFilter,
            AuthenticationProvider authProvider,
            UserRepository uRepo
    ) {
        this.JWTAuthFilter = JWTAuthFilter;
        this.authProvider = authProvider;
        this.uRepo = uRepo;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/auth/authenticate").permitAll()
                        .requestMatchers("/auth/register").hasAuthority(Authority.ADMIN.name())
                        .requestMatchers("/api/**").authenticated())
                .httpBasic(withDefaults())
                .sessionManagement(policy ->
                        policy.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .addFilterBefore(JWTAuthFilter, UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();

    }

}
