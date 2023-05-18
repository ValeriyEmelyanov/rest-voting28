package com.example.restvoting28.restaurant.web;

import com.example.restvoting28.AbstractControllerTest;
import com.example.restvoting28.restaurant.RestaurantRepository;
import com.example.restvoting28.restaurant.model.Restaurant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.restvoting28.common.JsonUtil.readValue;
import static com.example.restvoting28.common.JsonUtil.writeValue;
import static com.example.restvoting28.restaurant.web.RestaurantController.READ_PATH;
import static com.example.restvoting28.restaurant.web.RestaurantController.URL;
import static com.example.restvoting28.restaurant.web.RestaurantController.WRITE_PATH;
import static com.example.restvoting28.restaurant.web.RestaurantTestData.RESTAURANT_CRAZY_COOK_ID;
import static com.example.restvoting28.restaurant.web.RestaurantTestData.RESTAURANT_CRAZY_COOK_NAME;
import static com.example.restvoting28.restaurant.web.RestaurantTestData.getNew;
import static com.example.restvoting28.restaurant.web.RestaurantTestData.getNewWithHtml;
import static com.example.restvoting28.restaurant.web.RestaurantTestData.getUpdated;
import static com.example.restvoting28.restaurant.web.UserTestData.ADMIN_MAIL;
import static com.example.restvoting28.restaurant.web.UserTestData.USER_MAIL;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantControllerTest extends AbstractControllerTest {
    private static final String READ_URL = URL + READ_PATH;
    private static final String WRITE_URL = URL + WRITE_PATH;

    @Autowired
    private RestaurantRepository repository;

    @Test
    @WithUserDetails(USER_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(READ_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(READ_URL + "/" + RESTAURANT_CRAZY_COOK_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(RESTAURANT_CRAZY_COOK_NAME)));
    }

    @Test
    void getUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.get(READ_URL + "/" + RESTAURANT_CRAZY_COOK_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(READ_URL + "/999"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Restaurant newRestaurant = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(WRITE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newRestaurant)))
                .andDo(print())
                .andExpect(status().isCreated());
        MvcResult result = action.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        Restaurant created = readValue(contentAsString, Restaurant.class);

        assertEquals(newRestaurant.getName(), created.getName());
        long newId = created.id();
        assertEquals(newRestaurant.getName(), repository.getExisted(newId).getName());
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void createWithLocationForbidden() throws Exception {
        Restaurant newRestaurant = getNew();
        perform(MockMvcRequestBuilders.post(WRITE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newRestaurant)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void createWithLocationNoHtml() throws Exception {
        Restaurant newRestaurant = getNewWithHtml();
        perform(MockMvcRequestBuilders.post(WRITE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newRestaurant)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void update() throws Exception {
        Restaurant updatedRestaurant = getUpdated();
        ResultActions action = perform(MockMvcRequestBuilders.put(WRITE_URL + "/" + RESTAURANT_CRAZY_COOK_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updatedRestaurant)))
                .andDo(print())
                .andExpect(status().isOk());
        MvcResult result = action.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        Restaurant updated = readValue(contentAsString, Restaurant.class);

        assertEquals(updatedRestaurant.getName(), updated.getName());
        assertEquals(updatedRestaurant.getName(), repository.getExisted(RESTAURANT_CRAZY_COOK_ID).getName());
    }
}