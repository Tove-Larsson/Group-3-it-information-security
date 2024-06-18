package org.tove.group3itinformationsecurity.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.tove.group3itinformationsecurity.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        userRepository.findByEmail(username);
        // TODO - ta emot appuser skapa en spring user & returnera
        return null;
    }
}
