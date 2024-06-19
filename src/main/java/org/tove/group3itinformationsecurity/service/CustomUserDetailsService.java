package org.tove.group3itinformationsecurity.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.tove.group3itinformationsecurity.model.AppUser;
import org.tove.group3itinformationsecurity.repository.UserRepository;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Collections;

/**
 * CustomUserDetailsService är en implementering av UserDetailsService
 * som används av Spring Security för att ladda användarspecifika data.
 * Denna klass ansvarar för att hämta användarinformation från databasen
 * baserat på användarnamnet (email) och skapa ett UserDetails-objekt som
 * används av Spring Security för autentisering och auktorisering.
 */
 @Service
public class CustomUserDetailsService implements UserDetailsService {

    UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       AppUser appUser = userRepository.findByEmail(username);
       if (appUser == null) {
           throw new UsernameNotFoundException("No user found with username: " + username);
       }
        Collection<? extends GrantedAuthority> authorities = getAuthorities(appUser.getRole());

       return new User(appUser.getEmail(),
               appUser.getPassword(),
               true,
               true,
               true,
               true,
               authorities);
    }
}
