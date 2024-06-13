package org.tove.group3itinformationsecurity.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

/*
Konfigurationsklass för appens inställningar kring säkerhet och hantering av användare.
Klassen innehåller en PasswordEncoder som krypterar lösenord, en SecurityFilterChain där vi skapar
regler för åtkomst till olika endpoints samt så skapar vi en user av rollen Admin som sparas InMemory
*/

@EnableWebSecurity
@Configuration
public class Config {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth

                                .requestMatchers(
                                        "/register",
                                        "/register_success",
                                        "/admin",
                                        "remove_user",
                                        "remove_user_success",
                                        "remove_user_failed").hasRole("ADMIN")
                                .anyRequest().authenticated()

                )
                .httpBasic(Customizer.withDefaults())
                /*

                .formLogin(formLogin ->
                        formLogin
                                .defaultSuccessUrl("/", true)
                                .failureUrl("/login?error=true")
                                .permitAll()
                )
                 */
                .logout(logout ->
                        logout
                                .logoutUrl("/perform_logout")
                                .logoutSuccessUrl("/logout_success")
                                .permitAll()
                );

        return http.build();

    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager();

        UserDetails user = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("adminpass"))
                .roles("ADMIN")
                .build();
        userDetailsService.createUser(user);

        return userDetailsService;
    }
}
