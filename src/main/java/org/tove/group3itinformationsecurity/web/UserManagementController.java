package org.tove.group3itinformationsecurity.web;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.tove.group3itinformationsecurity.dto.UserDTO;
import org.tove.group3itinformationsecurity.service.UserService;
import org.tove.group3itinformationsecurity.utils.MaskingUtils;

import java.util.Objects;

/**
 * Denna klass skapar endppints som hanterar HTTP-förfrågningar för användarhantering,
 * inklusive sidor och formulär för att registrera, ta bort och uppdatera användare.
 */
 @Controller
public class UserManagementController {

    UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserManagementController.class);

    public UserManagementController(UserService userService) {
        this.userService = userService;
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

        if (bindingResult.hasFieldErrors("email")) return "remove_user";

        try {
            userService.removeUser(userDTO);
            logger.debug("The action of removing the user with email: " + MaskingUtils.anonymize(userDTO.getEmail()) + " is in process");
            return "remove_user_success";
        } catch (UsernameNotFoundException e) {
            logger.warn("Something went wrong ", e);
            return "remove_user_failed";
        }
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new UserDTO());
        logger.debug("Going to register user page(only available for admin)");
        return "register";
    }

    @PostMapping("/register")
    public String registerForm(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) return "register";
        if (!Objects.equals(userDTO.getRole(), "USER")) return "register";
        if (userService.userExists(userDTO.getEmail())) {
            logger.warn("Register failed, the user already exists: " + MaskingUtils.anonymize(userDTO.getEmail()));
            return "register_failed";
        }

        userService.registerUser(userDTO);
        logger.debug("Registered a new user with email: " + MaskingUtils.anonymize(userDTO.getEmail()));

        return "register_success";

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

        try {
            userService.updatePassword(userDTO);
            logger.debug("Updates user password for " + MaskingUtils.anonymize(userDTO.getEmail()));
            return "update_user_success";

        } catch (UsernameNotFoundException e) {
            // Not masking this one since the user does not exist and confirm input in logger
            logger.warn("Update failed, the user does not exist: " + userDTO.getEmail());
            return "update_user_failed";
        }
    }

    @GetMapping("/logout_success")
    public String logOutSuccess() {
        logger.debug("Logged out successfully");
        return "logout_success";
    }
}
