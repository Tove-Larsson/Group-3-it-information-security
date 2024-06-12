package org.tove.group3itinformationsecurity.web;

import jakarta.validation.Valid;
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
import org.tove.group3itinformationsecurity.dto.UserDTO;

@Controller
public class WebController {

    PasswordEncoder passwordEncoder;
    InMemoryUserDetailsManager inMemoryUserDetailsManager;



    public WebController(PasswordEncoder passwordEncoder, InMemoryUserDetailsManager inMemoryUserDetailsManager) {
        this.passwordEncoder = passwordEncoder;
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
    }


    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }
    @GetMapping("/remove_user")
    public String removeUser(Model model) {
        model.addAttribute("user", new UserDTO());
        return "remove_user";
    }

    @PostMapping("/remove_user")
    public String removeUserForm(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult) {

        if (bindingResult.hasFieldErrors("email")) {
            System.out.println("Bindingresult" + bindingResult);
            return "remove_user";
        }

        if (!inMemoryUserDetailsManager.userExists(userDTO.getEmail())) {
            return "remove_user_failed";
        }
        // Add username / email in the html in user_removed
        inMemoryUserDetailsManager.deleteUser(userDTO.getEmail());

        return "remove_user_success";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new UserDTO());
        return "register";
    }

    @PostMapping("/register")
    public String registerForm(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) return "register";

        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        UserDetails user = User.builder()
                .username(userDTO.getEmail())
                .password(encodedPassword)
                .roles("USER")
                .build();
        inMemoryUserDetailsManager.createUser(user);
        // Add username / email in the html in register_success
        model.addAttribute("registeredUser", user);

        return "register_success";

    }

    @GetMapping("/logout_success")
    public String logOutSuccess() {
        return "/logout_success";
    }
}
