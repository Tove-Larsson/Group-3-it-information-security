package org.tove.group3itinformationsecurity.web;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WebControllerTest {

    @Autowired
    private MockMvc mvc;

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



}