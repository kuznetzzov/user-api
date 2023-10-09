package com.flybuilder.userapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flybuilder.userapi.controllers.UserController;
import com.flybuilder.userapi.model.db.entity.User;
import com.flybuilder.userapi.model.dto.request.UserInfoRequest;
import com.flybuilder.userapi.model.dto.response.UserInfoResponse;
import com.flybuilder.userapi.model.enums.Gender;
import com.flybuilder.userapi.service.impl.UserServiceImpl;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserControllerTest {

    UserInfoRequest user = new UserInfoRequest();

    {
        user.setFirstName("Ivan");
        user.setLastName("Ivanov");
        user.setEmail("test@test.com");
    }

    MockMvc mockMvc;

    @InjectMocks
    private UserServiceImpl userService;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    ObjectMapper objectMapper;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");


    @Before
    public void setUp() {
        ConfigurableMockMvcBuilder builder =
                MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                        .apply(documentationConfiguration(this.restDocumentation));
        this.mockMvc = builder.build();
    }


    @Test
    @SneakyThrows
    public void createUser() {
        String content = objectMapper.writeValueAsString(user);
        System.out.println(content);
        String uri = "/users";
        mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Ivan"))
                .andExpect(jsonPath("$.lastName").value("Ivanov"))
                .andDo(document(uri.replace("/", "")));
    }


    @Test
    public void getUserById() throws Exception {
        Long userId = 1L; // Здесь укажите существующий ID пользователя
        String uri = "/users/{id}";
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andDo(document(uri.replace("/", "")));
    }

    @Test
    public void updateUser() throws Exception {
        Long userId = 1L;
        String uri = "/users/{id}";

        UserInfoRequest request = new UserInfoRequest();
        request.setEmail("new@test.com");

        UserInfoResponse updatedUser = new UserInfoResponse();
        updatedUser.setId(userId);
        updatedUser.setEmail("new@test.com");

        when(userService.updateUser(userId, request)).thenReturn(updatedUser);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put(uri, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.email").value("new@example.com"))
                .andDo(document(uri.replace("/", "")));
    }

    @Test
    public void testGetAllUsers() throws Exception {
        UserInfoResponse userInfoResponse1 = new UserInfoResponse();
        userInfoResponse1.setFirstName("John");
        userInfoResponse1.setLastName("Doe");

        UserInfoResponse userInfoResponse2 = new UserInfoResponse();
        userInfoResponse2.setFirstName("Jane");
        userInfoResponse2.setLastName("Smith");

        List<UserInfoResponse> userInfoResponseList = Arrays.asList(
                userInfoResponse1,
                userInfoResponse2
        );

        int page = 1;
        int perPage = 10;
        String sort = "age";
        Sort.Direction order = Sort.Direction.ASC;
        String filter = "filter";

        when(userService.getAllUsers(page, perPage, sort, order, filter))
                .thenReturn(new PageImpl<>(userInfoResponseList));

        mockMvc.perform(MockMvcRequestBuilders.
                        get("/users/all")
                        .param("page", "1")
                        .param("perPage", "10")
                        .param("sort", "age")
                        .param("order", "ASC")
                        .param("filter", "filter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect((ResultMatcher) jsonPath("$.content[0].firstName", is("John")))
                .andExpect((ResultMatcher) jsonPath("$.content[0].lastName", is("Doe")))
                .andExpect((ResultMatcher) jsonPath("$.content[1].firstName", is("Jane")))
                .andExpect((ResultMatcher) jsonPath("$.content[1].lastName", is("Smith")));

        verify(userService, times(1)).getAllUsers(1, 1, "age", Sort.Direction.ASC, "someFilterValue");
    }

}