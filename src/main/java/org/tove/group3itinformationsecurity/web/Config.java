package org.tove.group3itinformationsecurity.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.tove.group3itinformationsecurity.service.CustomUserDetailsService;

/**
 * Konfigurationsklass för appens inställningar kring säkerhet och hantering av användare.
 * Klassen innehåller en PasswordEncoder som krypterar lösenord, en SecurityFilterChain där vi skapar
 * regler för åtkomst till olika endpoints samt så skapar vi en user av rollen Admin som sparas InMemory
 */
@EnableWebSecurity
@Configuration
public class Config {

    CustomUserDetailsService customUserDetailsService;

    public Config(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//  @Bean
//  public AuthenticationProvider authenticationProvider() {
//      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//      authProvider.setUserDetailsService(customUserDetailsService);
//      authProvider.setPasswordEncoder(passwordEncoder());
//      return authProvider;
//  }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
//                .csrf(AbstractHttpConfigurer::disable) LÄGG TILLBAKA VID TEST
                .authorizeHttpRequests(auth ->
                        auth

                                .requestMatchers(
                                        "/register",
                                        "/register_success",
                                        "/admin",
                                        "/remove_user",
                                        "/remove_user_success",
                                        "/remove_user_failed").hasRole("ADMIN")
                                .anyRequest().authenticated()

                )
                // .httpBasic(Customizer.withDefaults()) LÄGG TILLBAKA VID TEST

                // TA BORT VID TEST
                .formLogin(formLogin ->
                        formLogin
                                .defaultSuccessUrl("/", true)
                                .failureUrl("/login?error=true")
                                .permitAll()
                )

                .logout(logout ->
                        logout
                                .logoutUrl("/perform_logout")
                                .logoutSuccessUrl("/logout_success")
                                .permitAll()
                );

        return http.build();

    }

}
