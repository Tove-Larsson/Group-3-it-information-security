package org.tove.group3itinformationsecurity.web;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
import org.tove.group3itinformationsecurity.model.AppUser;
import org.tove.group3itinformationsecurity.repository.UserRepository;
import org.tove.group3itinformationsecurity.service.UserService;
import org.tove.group3itinformationsecurity.utils.MaskingUtils;

import java.util.Objects;

/**
 * Kontrollerklass för hantering av webbinteraktioner och användarhantering.,
 * Innehåller metoder för att navigera och hantera olika sidor och formulär inom applikationen.
 * Använder PasswordEncoder för lösenordskryptering och InMemoryUserDetailsManager för användarhantering i minnet.
 */
@Controller
public class UserManagementController {

    UserService userService;
    //PasswordEncoder passwordEncoder;
    //UserRepository userRepository;


    private static final Logger logger = LoggerFactory.getLogger(UserManagementController.class);

    public UserManagementController(UserService userService) {
        this.userService = userService;
        //this.passwordEncoder = passwordEncoder;
        //this.userRepository = userRepository;
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
//
//        if (!inMemoryUserDetailsManager.userExists(escapedEmail)) {
//            logger.warn("The user with email: " + MaskingUtils.anonymize(userDTO.getEmail()) + " could not be found");
//            return "remove_user_failed";
//        }
//        logger.debug("The action of removing the user with email: " + MaskingUtils.anonymize(userDTO.getEmail()) + " is in process");
//        inMemoryUserDetailsManager.deleteUser(escapedEmail);

        return "remove_user_success";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new UserDTO());
        logger.debug("Going to register user page(only available for admin)");
        return "register";
    }

    @PostMapping("/register")
    public String registerForm(UserDTO userDTO, BindingResult bindingResult) {

        String direction = userService.registerUser(userDTO, bindingResult);
        logger.debug("Registered a new user with email: " + MaskingUtils.anonymize(userDTO.getEmail()));

        return direction;

    }

    @GetMapping("/update_user")
    public String updateUser(Model model) {
        model.addAttribute("user", new UserDTO());
        logger.debug("Going to update user page (only available for admin)");
        return "update_user";
    }

    @PostMapping("/update_user")
    public String updateForm(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors("email")) return "update_user";
        if (bindingResult.hasFieldErrors("password")) return "update_user";
//
//        String escapedEmail = HtmlUtils.htmlEscape(userDTO.getEmail());
//        String escapedPassword = HtmlUtils.htmlEscape(userDTO.getPassword());
//
//        String encodedPassword = passwordEncoder.encode(escapedPassword);

//        if (!inMemoryUserDetailsManager.userExists(escapedEmail)) {
//            logger.warn("The user with email: " + MaskingUtils.anonymize(userDTO.getEmail()) + " could not be found");
//            return "update_user_failed";
//        }
//        // TODO - Vi behöver H2 databasen för att fortsätta här
//        logger.debug("Updates user password for " + MaskingUtils.anonymize(userDTO.getEmail()));

        return "update_user_success";
    }


    @GetMapping("/logout_success")
    public String logOutSuccess() {
        logger.debug("Logged out successfully");
        return "logout_success";
    }
}
