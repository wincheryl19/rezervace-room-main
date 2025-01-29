package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Konfigurační třída pro nastavení Spring Security.
 * Zajišťuje autentizaci, autorizaci a správu přístupu k různým částem aplikace.
 */
@Configuration
@EnableWebSecurity
public class SpringSecurity {

    private final UserDetailsService userDetailsService;

    // Konstruktor pro injektování UserDetailsService, který načítá uživatele z databáze.
    public SpringSecurity(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Bean pro šifrování hesel pomocí BCrypt.
     * Používá se při registraci uživatelů a při autentizaci.
     */
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Konfigurace zabezpečení aplikace:
     * - Nastavuje přístupové politiky k URL adresám.
     * - Přizpůsobuje přihlašovací a odhlašovací mechanismy.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Vypíná CSRF ochranu (pouze pokud je to bezpečné).
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/register/**", "/index", "/", "/login/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")  // Přístup na /admin pouze pro ADMIN
                .requestMatchers("/reservations/**").hasAnyRole("ADMIN", "USER")  // Přístup na /reservations pro USER
                .anyRequest().authenticated()  // Všechny ostatní žádosti vyžadují přihlášení
            )
            .formLogin(form -> form
                .loginPage("/login") // Stránka pro přihlášení.
                .loginProcessingUrl("/login") // URL pro zpracování přihlášení.
                .successHandler(customSuccessHandler()) // Přesměrování po úspěšném přihlášení.
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // URL pro odhlášení.
                .logoutSuccessUrl("/index") // Přesměrování po úspěšném odhlášení.
                .permitAll()
            );

        return http.build();
    }

    /**
     * Přizpůsobené přesměrování po úspěšném přihlášení.
     * Rozlišuje mezi administrátory (přesměruje na /admin) a běžnými uživateli (/reservations).
     */
    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return (request, response, authentication) -> {
            authentication.getAuthorities().forEach(auth -> {
                try {
                    if ("ROLE_ADMIN".equals(auth.getAuthority())) {
                        response.sendRedirect("/admin");
                    } else {
                        response.sendRedirect("/reservations");
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Chyba při přesměrování po přihlášení", e);
                }
            });
        };
    }

    /**
     * Nastavení autentizace:
     * - Používá UserDetailsService pro načítání uživatelů z databáze.
     * - Hesla jsou šifrována pomocí BCrypt.
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
    }
}
