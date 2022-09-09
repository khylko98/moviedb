package ua.khylko.moviedb.controller;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.khylko.moviedb.PostgresContainer;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Order(1)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerTest extends PostgresContainer {
    @Autowired
    private MockMvc mvc;

    @ParameterizedTest
    @DisplayName("SIGN UP WITH DEFAULT USER")
    @CsvSource({"testusername, testpassword, " +
                "Testfirstname, Testlastname, 24, " +
                "test2022@gmail.com, +380 99 200 20 90, " +
                "Teststreet, Testcity, " +
                "Testcountry, 44444"})
    @Order(1)
    void signUpDefaultUserTest(String username, String password,
                               String firstName, String lastName,
                               Integer age,
                               String email, String phoneNumber,
                               String street, String city,
                               String country, String zipCode) throws Exception {
        String body =   "{" +
                            "\"username\":\"" + username + "\"," +
                            "\"password\":\"" + password + "\"," +
                            "\"firstName\":\"" + firstName + "\"," +
                            "\"lastName\":\"" + lastName + "\"," +
                            "\"age\":" + age + "," +
                            "\"email\":\"" + email + "\"," +
                            "\"phoneNumber\":\"" + phoneNumber + "\"," +
                            "\"address\": {" +
                                "\"street\":\"" + street + "\"," +
                                "\"city\":\"" + city + "\"," +
                                "\"country\":\"" + country + "\"," +
                                "\"zipCode\":\"" + zipCode + "\"" +
                            "}" +
                        "}";

        MvcResult result = post("/moviedb/auth/signup", body);

        assertTrue(result.getResponse().getContentAsString()
                .contains("\"jwt-token\":\"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9."));
    }

    @ParameterizedTest
    @DisplayName("SIGN IN WITH DEFAULT USER")
    @CsvSource({"testusername, testpassword"})
    @Order(2)
    void signInDefaultUserTest(String username, String password) throws Exception {
        String body =   "{" +
                            "\"username\":\"" + username + "\"," +
                            "\"password\":\"" + password + "\"" +
                        "}";

        MvcResult result = post("/moviedb/auth/signin", body);

        assertTrue(result.getResponse().getContentAsString()
                .contains("\"jwt-token\":\"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9."));
    }

    @ParameterizedTest
    @DisplayName("SIGN IN WITH ADMIN")
    @CsvSource({"testadmin, testadmin"})
    @Order(3)
    void signInAdminTest(String username, String password) throws Exception {
        String body =   "{" +
                            "\"username\":\"" + username + "\"," +
                            "\"password\":\"" + password + "\"" +
                        "}";

        MvcResult result = post("/moviedb/auth/signin", body);

        assertTrue(result.getResponse().getContentAsString()
                .contains("\"jwt-token\":\"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9."));
    }

    @ParameterizedTest
    @DisplayName("SIGN UP EXCEPTION WITH USER ALREADY IN DB")
    @CsvSource({"testusername, testpassword, " +
                "Testfirstname, Testlastname, 24, " +
                "test2022@gmail.com, +380 99 200 20 90, " +
                "Teststreet, Testcity, " +
                "Testcountry, 44444"})
    @Order(4)
    void signUpDefaultUserExceptionTest(String username, String password,
                                        String firstName, String lastName,
                                        Integer age,
                                        String email, String phoneNumber,
                                        String street, String city,
                                        String country, String zipCode) throws Exception {
        String body =   "{" +
                            "\"username\":\"" + username + "\"," +
                            "\"password\":\"" + password + "\"," +
                            "\"firstName\":\"" + firstName + "\"," +
                            "\"lastName\":\"" + lastName + "\"," +
                            "\"age\":" + age + "," +
                            "\"email\":\"" + email + "\"," +
                            "\"phoneNumber\":\"" + phoneNumber + "\"," +
                            "\"address\": {" +
                                "\"street\":\"" + street + "\"," +
                                "\"city\":\"" + city + "\"," +
                                "\"country\":\"" + country + "\"," +
                                "\"zipCode\":\"" + zipCode + "\"" +
                            "}" +
                        "}";

        MvcResult result = postException("/moviedb/auth/signup", body);

        assertTrue(result.getResponse().getContentAsString()
                .contains("\"message\":\"username - User with username already exist\""));
    }

    @ParameterizedTest
    @DisplayName("SIGN IN EXCEPTION WITH NO USER IN DB")
    @CsvSource({"testusername123, testpassword123"})
    @Order(5)
    void signInDefaultUserExceptionTest(String username, String password) throws Exception {
        String body =   "{" +
                            "\"username\":\"" + username + "\"," +
                            "\"password\":\"" + password + "\"" +
                        "}";

        MvcResult result = postException("/moviedb/auth/signin", body);

        assertTrue(result.getResponse().getContentAsString()
                .contains("\"message\":\"username/password - Incorrect username or password\""));
    }

    private MvcResult post(String url, String body) throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                                            .post(url)
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(body);
        return mvc
                .perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    private MvcResult postException(String url, String body) throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                                            .post(url)
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(body);
        return mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}