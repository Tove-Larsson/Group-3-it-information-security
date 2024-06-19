package org.tove.group3itinformationsecurity.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.tove.group3itinformationsecurity.model.AppUser;
import org.tove.group3itinformationsecurity.repository.UserRepository;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserManagementControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setInitialUser() {
        AppUser user = new AppUser();
        user.setFirstName("Clark");
        user.setLastName("Kent");
        user.setAge(36);
        user.setEmail("clark@gmail.com");
        user.setPassword(passwordEncoder.encode("lana"));
        user.setRole("USER");
        userRepository.save(user);
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
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
                        .param("firstName", "Lex")
                        .param("lastName", "Luther")
                        .param("email", "lex@gmail.com")
                        .param("password", "password")
                        .param("role", "USER"))
                .andExpect(status().isOk())
                .andExpect(view().name("register_success"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void registerUserFailed() throws Exception {
        this.mvc.perform(post("/register")
                        .param("firstName", "someName")
                        .param("lastName", "someName")
                        .param("email", "clark@gmail.com")
                        .param("password", "password")
                        .param("role", "USER"))
                .andExpect(status().isOk())
                .andExpect(view().name("register_failed"));

    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void removeUserSuccess() throws Exception {

        this.mvc.perform(post("/remove_user")
                        .param("email", "clark@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("remove_user_success"));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void removeUserFailed() throws Exception {

        this.mvc.perform(post("/remove_user")
                        .param("email", "test@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("remove_user_failed"));
    }
}