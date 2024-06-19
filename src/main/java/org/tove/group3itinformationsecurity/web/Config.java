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
import org.tove.group3itinformationsecurity.service.CustomUserDetailsService;

/**
 * Config-klassen konfigurerar Spring Security för applikationen.
 * Denna klass aktiverar webbsäkerhet, definierar en passwordEncoder
 * och sätter upp securityFilterChain för att hantera autentisering
 * och auktorisering av HTTP-förfrågningar.
 */
@EnableWebSecurity
@Configuration
public class Config {

    CustomUserDetailsService customUserDetailsService;

    public Config(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }
    /**
     * Denna Bean returnerar en PasswordEncoder av typen Bcrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Konfigurerar säkerhetsinställningar för HTTP-förfrågningar.
     * Vi bestämmer i denna vilken roll som har åtkomst till vissa endpoints.
     * Vi använder sedan Springs inbyggda formLogin för att konfigurera inloggning i appen.
     * På slutet använder vi oss av Springs logOut för att konfigurera utloggning ur appen.
     * <br><br>
     * Vid testning:
     * Disablea csrf som per default alltid är aktiverad.
     * Använda oss av httpbasic i stället för formLogin.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth

                                .requestMatchers(
                                        "/register",
                                        "/admin",
                                        "/remove_user",
                                        "/update_user").hasRole("ADMIN")
                                .anyRequest().authenticated()

                )
                // .httpBasic(Customizer.withDefaults())

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
