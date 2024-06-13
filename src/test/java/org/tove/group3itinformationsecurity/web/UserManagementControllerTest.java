package org.tove.group3itinformationsecurity.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserManagementControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private InMemoryUserDetailsManager userDetailsManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    
    private void addUserToInMemory(String username, String password, String roles) {
        UserDetails user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(roles)
                .build();
        this.userDetailsManager.createUser(user);
    }

    @Test
    @WithMockUser
    public void indexPageAuthorized() throws Exception {
       this.mvc
                .perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    public void indexPageUnauthorized() throws Exception {
       this.mvc
                .perform(get("/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void registerUserSuccess() throws Exception {
        this.mvc.perform(post("/register")
                        .param("email", "test@test.com")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("register_success"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void registerUserFailed() throws Exception {
        this.mvc.perform(post("/register")
                        .param("email", "testtest.com")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("user", "email"));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void removeUserSuccess() throws Exception {

        String email = "gleissman@gmail.com";

        addUserToInMemory(email, "java23", "USER");

        assertTrue(userDetailsManager.userExists(email));

        this.mvc.perform(post("/remove_user")
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(view().name("remove_user_success"));

        assertFalse(userDetailsManager.userExists(email));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void removeUserFailed() throws Exception {

        this.mvc.perform(post("/remove_user")
                        .param("email", "test@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("remove_user_failed"));

        assertFalse(userDetailsManager.userExists("test@gmail.com"));
    }
}