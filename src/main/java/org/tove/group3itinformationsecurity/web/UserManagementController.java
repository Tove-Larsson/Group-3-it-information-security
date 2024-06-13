package org.tove.group3itinformationsecurity.web;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.util.HtmlUtils;
import org.tove.group3itinformationsecurity.dto.UserDTO;
import org.tove.group3itinformationsecurity.utils.MaskingUtils;

/**
 * Kontrollerklass för hantering av webbinteraktioner och användarhantering.,
 * Innehåller metoder för att navigera och hantera olika sidor och formulär inom applikationen.
 * Använder PasswordEncoder för lösenordskryptering och InMemoryUserDetailsManager för användarhantering i minnet.
 */
@Controller
public class UserManagementController {


    PasswordEncoder passwordEncoder;

    InMemoryUserDetailsManager inMemoryUserDetailsManager;

    private static final Logger logger = LoggerFactory.getLogger(UserManagementController.class);

    public UserManagementController(PasswordEncoder passwordEncoder, InMemoryUserDetailsManager inMemoryUserDetailsManager) {
        this.passwordEncoder = passwordEncoder;
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
    }

    @GetMapping("/")
    public String firstPage() {
        logger.debug("Going to first page");
        return "index";
    }

    @GetMapping("/admin")
    public String admin() {
        logger.debug("Going to admin page");
        return "admin";
    }

    @GetMapping("/remove_user")
    public String removeUser(Model model) {
        model.addAttribute("user", new UserDTO());
        logger.debug("Going to remove user page (only available for admin)");
        return "remove_user";
    }

    @PostMapping("/remove_user")
    public String removeUserForm(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult) {

        if (bindingResult.hasFieldErrors("email")) {
            System.out.println("Bindingresult: " + bindingResult);
            return "remove_user";
        }

        String escapedEmail = HtmlUtils.htmlEscape(userDTO.getEmail());

        if (!inMemoryUserDetailsManager.userExists(escapedEmail)) {
            logger.warn("The user with email: " + MaskingUtils.anonymize(userDTO.getEmail()) + " could not be found");
            return "remove_user_failed";
        }
        // Add username / email in the html in user_removed
        logger.debug("The action of removing the user with email: " + MaskingUtils.anonymize(userDTO.getEmail()) + " is in process");
        inMemoryUserDetailsManager.deleteUser(escapedEmail);

        return "remove_user_success";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new UserDTO());
        logger.debug("Going to register user page(only available for admin)");
        return "register";
    }

    @PostMapping("/register")
    public String registerForm(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) return "register";

        String escapedEmail = HtmlUtils.htmlEscape(userDTO.getEmail());
        String escapedPassword = HtmlUtils.htmlEscape(userDTO.getPassword());

        String encodedPassword = passwordEncoder.encode(escapedPassword);

        UserDetails user = User.builder()
                .username(escapedEmail)
                .password(encodedPassword)
                .roles("USER")
                .build();
        inMemoryUserDetailsManager.createUser(user);
        // Add username / email in the html in register_success
        // model.addAttribute("registeredUser", user);

        logger.debug("Registered a new user with email: " + MaskingUtils.anonymize(userDTO.getEmail()));

        return "register_success";

    }

    @GetMapping("/logout_success")
    public String logOutSuccess() {
        logger.debug("Logged out successfully");
        return "/logout_success";
    }
}
