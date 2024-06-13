package org.tove.group3itinformationsecurity.web;

import ch.qos.logback.core.encoder.Encoder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WebControllerTest {

    @Autowired
    private MockMvc mvc;

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private InMemoryUserDetailsManager userDetailsManagerWithUser() {
        InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager();

        UserDetails user = User.builder()
                .username("gleissman@gmail.com")
                .password(passwordEncoder().encode("java23"))
                .roles("USER")
                .build();
        userDetailsService.createUser(user);
        return userDetailsService;
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




}