package com.pabcubcru.bookdeal.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class UserFavouriteControllerTests {

    private final String ID_BOOK_1 = "book001";
    private final String ID_BOOK_2 = "book002";
    private final String ID_USER_1 = "test001";

    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithMockUser(value = ID_USER_1, authorities = "user")
    public void testMain() throws Exception {
        this.mockMvc.perform(get("/favourites/0")).andExpect(status().isOk()).andExpect(view().name("Main"));
    }

    @Test
    @WithMockUser(value = ID_USER_1, authorities = "user")
    public void testFindAllByUsername() throws Exception {
        this.mockMvc.perform(get("/favourites/all?page=0"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.books").isNotEmpty());
    }

    @Test
    @WithMockUser(value = ID_USER_1, authorities = "user")
    public void testAddFavouriteBook() throws Exception {
        this.mockMvc.perform(get("/favourites/" + ID_BOOK_2 + "/add")).andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithMockUser(value = ID_USER_1, authorities = "user")
    public void testIsAlreadyFavouriteBook() throws Exception {
        this.mockMvc.perform(get("/favourites/booktest/add")).andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false)).andExpect(jsonPath("$.alreadyAdded").value(true));
    }

    @Test
    @WithMockUser(value = ID_USER_1, authorities = "user")
    public void testDeleteFavouriteBook() throws Exception {
        this.mockMvc.perform(delete("/favourites/" + ID_BOOK_1 + "/delete")).andExpect(status().isOk());
    }

}
