package com.apress.users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.Collection;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"unused","unassigned"})
// "This annotation sets up our integration tests that let you interact with a running web application."
// WebEnvironment.RANDOM_PORT -> Starts the application to be tested, on a webserver that is running
// and listening on a randomly chosen port.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UsersHttpRequestTests {
    // Injects values, from local.server.port, into the field it annotates (int port)
    @Value("${local.server.port}")
    private int port;

    private final String BASE_URL = "http://localhost:";
    private final String USERS_PATH = "/users";

    @Autowired // dependency injection
    private TestRestTemplate restTemplate; // alternative to RestTemplate
    // TestRestTemplate -> Designed for integration testing of RESTful web apps. It configures an Apache HTTP
    // or an OkHttp client in order to send requests. Handles errors (4xx/5xx) by returning ResponseEntity objects
    // that contain errors thrown and response status. It also has methods to handle authentication (withBasicAuth/withOAuth2Client).

    @Test
    void indexPageShouldReturnHeaderOneContent() throws Exception{
        assertThat(this.restTemplate.getForObject(BASE_URL + port, String.class))
                .contains("Simple Users Rest Application");
    }

    @Test
    void userEndPointShouldReturnCollectionWithTwoUsers() throws Exception {
        Collection<User> response = this.restTemplate.getForObject(BASE_URL + port + USERS_PATH, Collection.class);
        assertThat(response).isNotNull();
        assertThat(response).isNotEmpty();
    }

    @Test
    void userEndPointPostNewUserShouldReturnUser() throws Exception {
        User user = User.builder()
                .name("Dummy")
                .email("dummy@email.com")
                .password("aw2sOm3R!")
                .build();
        User response = this.restTemplate.postForObject(BASE_URL + port + USERS_PATH, user, User.class);
        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo(user.getEmail());

        Collection<User> users = this.restTemplate.getForObject(BASE_URL + port + USERS_PATH, Collection.class);
        assertThat(users.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    void userEndPointDeleteUserShouldReturnVoid() throws Exception {
        this.restTemplate.delete(BASE_URL + port + USERS_PATH + "/norma@email.com");
        Collection<User> users = this.restTemplate.getForObject(BASE_URL + port + USERS_PATH, Collection.class);
        assertThat(users.size()).isLessThanOrEqualTo(2);
    }

    /*
     * Here, using the TestRestTemplate, the request expects a Map and the error sent in the response
     * is received as a JSON that easily gets converted to a Map.
     */
    @Test
    void userEndPointPostNewUserShouldReturnBadUserResponse() throws Exception {
        User user = User.builder()
                .email("dummy@email.com")
                .name("Dummy")
                .password("aw2s0m")
                .build();
        Map response = this.restTemplate.postForObject(BASE_URL + port + USERS_PATH, user, Map.class);
        assertThat(response).isNotNull();
        assertThat(response.get("errors")).isNotNull();
        Map errors = (Map) response.get("errors");
        assertThat(errors.get("password")).isNotNull();
        assertThat(errors.get("password")).isEqualTo("Password must be at least 8 characters long and contain" +
                        " at least one number, one uppercase, one lowercase and one special character.");
    }
}

// Sonar Qube says public modifier is not ok on tests class, so I removed it :)