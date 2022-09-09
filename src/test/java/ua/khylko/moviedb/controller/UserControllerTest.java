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
@Order(3)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest extends PostgresContainer {
    @Autowired
    private MockMvc mvc;
    private static String token;
    private static String username = "testusername2";

    @BeforeEach
    void setUp() throws Exception {
        String body =   "{" +
                            "\"username\":\"" + username + "\"," +
                            "\"password\":\"testpassword\"" +
                        "}";

        RequestBuilder request = MockMvcRequestBuilders
                                            .post("/moviedb/auth/signin")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(body);

        MvcResult result = mvc
                            .perform(request)
                            .andExpect(status().isOk())
                            .andReturn();

        token = result.getResponse().getContentAsString()
                .substring(14, result.getResponse().getContentAsString().length() - 2);
    }

    @Test
    @DisplayName("INFO DEFAULT USER")
    @Order(1)
    void infoTest() throws Exception {
        String expected =   "{" +
                                "\"id\":3," +
                                "\"username\":\"testusername2\"," +
                                "\"firstName\":\"Testfirstname\"," +
                                "\"lastName\":\"Testlastname\"," +
                                "\"age\":24," +
                                "\"email\":\"test2023@gmail.com\"," +
                                "\"phoneNumber\":\"+380 99 200 20 90\"," +
                                "\"address\":{" +
                                    "\"id\":2," +
                                    "\"street\":\"Teststreet\"," +
                                    "\"city\":\"Testcity\"," +
                                    "\"country\":\"Testcountry\"," +
                                    "\"zipCode\":\"44444\"" +
                                "}," +
                                "\"watchedMovies\":[]" +
                            "}";

        RequestBuilder request = MockMvcRequestBuilders
                                            .get("/moviedb/user/info")
                                            .header("Authorization", "Bearer " + token);

        MvcResult result = mvc
                            .perform(request)
                            .andExpect(status().isOk())
                            .andReturn();

        assertEquals(expected, result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @DisplayName("USER ADD MOVIE TO 'WATCHED' LIST")
    @CsvSource({"testmovie1, 20/02/2022"})
    @Order(2)
    void addMovie(String title, String releaseDate) throws Exception {
        String body =   "{" +
                            "\"title\":\"" + title + "\"," +
                            "\"releaseDate\":\"" + releaseDate + "\"" +
                        "}";

        RequestBuilder request = MockMvcRequestBuilders
                                            .post("/moviedb/user/addMovie")
                                            .header("Authorization", "Bearer " + token)
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(body);

        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("GET 'WATCHED' MOVIES BY DEFAULT USER")
    @Order(3)
    void getMovies() throws Exception {
        String expected =   "{" +
                                "\"watchedMovies\":[" +
                                    "{" +
                                        "\"id\":1," +
                                        "\"title\":\"testmovie1\"," +
                                        "\"releaseDate\":\"19/02/2022\"," +
                                        "\"director\":{" +
                                            "\"id\":1," +
                                            "\"firstName\":\"Testname\"," +
                                            "\"lastName\":\"Testlastname\"," +
                                            "\"age\":54" +
                                        "}," +
                                        "\"actors\":[" +
                                            "{" +
                                                "\"id\":1," +
                                                "\"firstName\":\"Testname\"," +
                                                "\"lastName\":\"Testlastname\"," +
                                                "\"age\":34" +
                                            "}" +
                                        "]" +
                                    "}" +
                                "]" +
                            "}";

        RequestBuilder request = MockMvcRequestBuilders
                                            .get("/moviedb/user/getMovies")
                                            .header("Authorization", "Bearer " + token);

        MvcResult result = mvc
                            .perform(request)
                            .andExpect(status().isOk())
                            .andReturn();

        assertEquals(expected, result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @DisplayName("UPDATE DEFAULT USER")
    @CsvSource({"testusername3, testpassword, " +
                "Testfirstname, Testlastname, 56, " +
                "testupdate@gmail.com, +380 99 200 20 90, " +
                "Teststreet, Testcity, " +
                "Testcountry, 55555"})
    @Order(4)
    void updateMeTest(String username, String password,
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

        RequestBuilder request = MockMvcRequestBuilders
                                            .patch("/moviedb/user/update")
                                            .header("Authorization", "Bearer " + token)
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(body);

        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andReturn();

        UserControllerTest.username = "testusername3";
    }

    @Test
    @DisplayName("DELETE DEFAULT USER")
    @Order(5)
    void delete() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                                            .delete("/moviedb/user/delete")
                                            .header("Authorization", "Bearer " + token);

        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }
}