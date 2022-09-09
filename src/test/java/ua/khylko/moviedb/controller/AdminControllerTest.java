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
@Order(2)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AdminControllerTest extends PostgresContainer {
    @Autowired
    private MockMvc mvc;
    private static String token;

    @BeforeEach
    void setUp() throws Exception {
        String body =   "{" +
                            "\"username\":\"testadmin\"," +
                            "\"password\":\"testadmin\"" +
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

    @ParameterizedTest
    @DisplayName("ADMIN GET USER INFO BY USERNAME")
    @CsvSource({"testusername1"})
    @Order(1)
    void userInfoTest(String username) throws Exception {
        String expected =   "{" +
                                "\"id\":2," +
                                "\"username\":\"testusername1\"," +
                                "\"firstName\":\"Testfirstname\"," +
                                "\"lastName\":\"Testlastname\"," +
                                "\"age\":24," +
                                "\"email\":\"test2021@gmail.com\"," +
                                "\"phoneNumber\":\"+380 99 200 20 90\"," +
                                "\"address\":{" +
                                    "\"id\":1," +
                                    "\"street\":\"Teststreet\"," +
                                    "\"city\":\"Testcity\"," +
                                    "\"country\":\"Testcountry\"," +
                                    "\"zipCode\":\"44444\"" +
                                "}," +
                                "\"dateAudit\":{" +
                                    "\"id\":1," +
                                    "\"createdAt\":\"2022-09-06T10:32:58.341+00:00\"," +
                                    "\"updatedAt\":\"2022-09-06T10:32:58.341+00:00\"" +
                                "}," +
                                "\"role\":{" +
                                    "\"id\":2," +
                                    "\"roleName\":\"ROLE_USER\"" +
                                "}," +
                                "\"watchedMovies\":[]" +
                            "}";

        MvcResult result = get("/moviedb/admin/user/{username}", username);

        assertEquals(expected, result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @DisplayName("ADMIN ADD MOVIE")
    @CsvSource({"testmovie, 20/02/2000, " +
                "Testdirectorfirstname, Testdirectorlastname, 54, " +
                "Testactorfirstname, Testactorlastname, 45, " +
                "Testactorfirstnametwo, Testactorlastnametwo, 34"})
    @Order(2)
    void addMovieTest(String title, String releaseDate,
                      String directorFirstName, String directorLastName,
                      Integer directorAge,
                      String actorOneFirstName, String actorOneLastName,
                      Integer actorOneAge,
                      String actorTwoFirstName, String actorTwoLastName,
                      Integer actorTwoAge) throws Exception {
        String body =   "{" +
                            "\"title\":\"" + title + "\"," +
                            "\"releaseDate\":\"" + releaseDate + "\"," +
                            "\"director\": {" +
                                "\"firstName\":\"" + directorFirstName + "\"," +
                                "\"lastName\":\"" + directorLastName + "\"," +
                                "\"age\":" + directorAge +
                            "}, " +
                            "\"actors\":[" +
                                "{" +
                                    "\"firstName\":\"" + actorOneFirstName + "\"," +
                                    "\"lastName\":\"" + actorOneLastName + "\"," +
                                    "\"age\":" + actorOneAge +
                                "}," +
                                "{" +
                                    "\"firstName\":\"" + actorTwoFirstName + "\"," +
                                    "\"lastName\":\"" + actorTwoLastName + "\"," +
                                    "\"age\":" + actorTwoAge +
                                "}" +
                            "]" +
                        "}";

        RequestBuilder request = MockMvcRequestBuilders
                                            .post("/moviedb/admin/movie/addMovie")
                                            .header("Authorization", "Bearer " + token)
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(body);

        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @ParameterizedTest
    @DisplayName("ADMIN GET MOVIE BY TITLE")
    @CsvSource({"testmovie"})
    @Order(3)
    void movieInfoTest(String title) throws Exception {
        String expected =   "{" +
                                "\"id\":2," +
                                "\"title\":\"testmovie\"," +
                                "\"releaseDate\":\"2000-02-20\"," +
                                "\"director\":{" +
                                    "\"id\":2," +
                                    "\"firstName\":\"Testdirectorfirstname\"," +
                                    "\"lastName\":\"Testdirectorlastname\"," +
                                    "\"age\":54" +
                                "}," +
                                "\"actors\":[" +
                                    "{" +
                                        "\"id\":2," +
                                        "\"firstName\":\"Testactorfirstname\"," +
                                        "\"lastName\":\"Testactorlastname\"," +
                                        "\"age\":45" +
                                    "}," +
                                    "{" +
                                        "\"id\":3," +
                                        "\"firstName\":\"Testactorfirstnametwo\"," +
                                        "\"lastName\":\"Testactorlastnametwo\"," +
                                        "\"age\":34" +
                                    "}" +
                                "]" +
                            "}";

        MvcResult result = get("/moviedb/admin/movie/{title}", title);

        assertEquals(expected, result.getResponse().getContentAsString());
    }

    private MvcResult get(String url, String value) throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                                            .get(url, value)
                                            .header("Authorization", "Bearer " + token);
        return mvc
                .perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }
}