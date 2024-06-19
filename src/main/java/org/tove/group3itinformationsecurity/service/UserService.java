package org.tove.group3itinformationsecurity.service;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.util.HtmlUtils;
import org.tove.group3itinformationsecurity.dto.UserDTO;
import org.tove.group3itinformationsecurity.model.AppUser;
import org.tove.group3itinformationsecurity.repository.UserRepository;

import java.util.Objects;

/**
 * UserService hanterar affärslogiken för användarhantering, inklusive skapande,
 * borttagning och uppdatering av användare (CRUD). Den använder UserRepository för
 * databasinteraktion och PasswordEncoder för att kryptera lösenord.
 */
 @Service
public class UserService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initialDatabasePost() {

        AppUser admin = new AppUser();
        admin.setFirstName("Arne");
        admin.setLastName("Barne");
        admin.setAge(12);
        admin.setEmail("admin");
        admin.setPassword(passwordEncoder.encode("password"));
        admin.setRole("ADMIN");
        userRepository.save(admin);

        AppUser user = new AppUser();
        user.setFirstName("Clark");
        user.setLastName("Kent");
        user.setAge(36);
        user.setEmail("clark@gmail.com");
        user.setPassword(passwordEncoder.encode("lana"));
        user.setRole("USER");
        userRepository.save(user);
    }

    public void registerUser(UserDTO userDTO) {

        String escapedFirstName = HtmlUtils.htmlEscape(userDTO.getFirstName());
        String escapedLastName = HtmlUtils.htmlEscape(userDTO.getLastName());
        String escapedEmail = HtmlUtils.htmlEscape(userDTO.getEmail());
        String escapedPassword = HtmlUtils.htmlEscape(userDTO.getPassword());
        String escapedRole = HtmlUtils.htmlEscape(userDTO.getRole());

        AppUser appUser = new AppUser();
        appUser.setFirstName(escapedFirstName);
        appUser.setLastName(escapedLastName);
        appUser.setAge(userDTO.getAge());
        appUser.setEmail(escapedEmail);
        appUser.setPassword(passwordEncoder.encode(escapedPassword));
        appUser.setRole(escapedRole);

        userRepository.save(appUser);

    }

    public void removeUser(String email) throws UsernameNotFoundException {
        String escapedEmail = HtmlUtils.htmlEscape(email);
        userRepository.delete(userRepository.findByEmail(escapedEmail));
    }

    public boolean userExists(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email) != null;

    }

    public void updatePassword(UserDTO userDTO) {

        AppUser user = userRepository.findByEmail(userDTO.getEmail());
        String escapedPassword = HtmlUtils.htmlEscape(userDTO.getPassword());
        user.setPassword(passwordEncoder.encode(escapedPassword));
        userRepository.save(user);
    }
}
