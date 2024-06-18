package org.tove.group3itinformationsecurity.service;

import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tove.group3itinformationsecurity.model.AppUser;
import org.tove.group3itinformationsecurity.repository.UserRepository;

@Service
public class UserService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        super();
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initialDatabasePost() {

        AppUser admin = new AppUser();
        admin.setEmail("admin");
        admin.setPassword(passwordEncoder.encode("password"));
        admin.setRole("ADMIN");
        userRepository.save(admin);

        AppUser user = new AppUser();
        user.setFirstName("Clark");
        user.setLastName("Kent");
        user.setAge(36);
        user.setEmail("clark.kent@smallvilehigh.com");
        user.setPassword(passwordEncoder.encode("lanalang"));
        user.setRole("USER");
        userRepository.save(user);
    }
}
