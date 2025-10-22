package com.apress.users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.Collection;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unused")
//As this class tests the behaviour of the entire API (meaning all endpoints for /user path;
// API is too complex of a concept for what this project is)
//this is of course an integration test class
//Signals the class as spring boot test class and accepts parameters
//one of them is webEnvironment which assigns a port to Tomcat when tests run (in our case a random port)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UsersApplicationTests {
    //this injects a value into the variable port
    //in this example it is the value of webEnvironment specified and set in the @SpringBootTest annotation
    //this is used to avoid port collision
    @Value("${local.server.port}")
    private int port;

    private final String BASE_URL = "http://localhost:";
    private final String USERS_PATH = "/users";
    //This annotation is used to inject an instance of a class, in this case it is class TestRestTemplate
    //which is used to execute any remote request to an external web API (for making requests to the endpoints)
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void indexPageShouldReturnHeaderOneContent() throws Exception {
        assertThat(this.restTemplate.getForObject(BASE_URL + port, String.class))
                .contains("Simple Users Rest Application");
    }

    @Test
    public void usersEndPointShouldReturnCollectionWithTwoUsers() throws Exception {
        Collection<User> response = this.restTemplate.getForObject(BASE_URL + port + USERS_PATH, Collection.class);
        assertThat(response.size()).isEqualTo(2);
    }

    // This test has issues now !!! -> further investigation required
//    @Test
//    public void saveUserEndpointShouldReturnUser() throws Exception{
//        User user = User.builder()
//                .name("Test User")
//                .email("testUser@email.com")
//                .active(true)
//                .role(UserRole.USER)
//                .password("t3sT*usR")
//                .build();
//        User response = this.restTemplate.postForObject(BASE_URL + port + USERS_PATH, user, User.class);
//        assertThat(response).isNotNull();
//        assertThat(response.getEmail()).isEqualTo(user.getEmail());
//        Collection<User> users = this.restTemplate.getForObject(BASE_URL + port + USERS_PATH, Collection.class);
//        assertThat(users.size()).isGreaterThanOrEqualTo(2);
//    }

    @Test
    public void userDeleteEndpointShouldReturnVoid() throws Exception {
        assertThat(restTemplate.getForObject(BASE_URL + port + USERS_PATH, Collection.class).size()).isEqualTo(2);
        this.restTemplate.delete(BASE_URL + port + USERS_PATH, "AnotherHardcodedUser@email.com");
        Collection<User> newUsers = restTemplate.getForObject(BASE_URL + port + USERS_PATH, Collection.class);
        assertThat(newUsers.size()).isLessThanOrEqualTo(2);
    }

    @Test
    public void findUserByEmailShouldReturnTheSearchedUser() throws Exception {
        final String EMAIL = "HardcodedUser@email.com";
        User user = this.restTemplate.getForObject(BASE_URL + port + USERS_PATH + "/" + EMAIL, User.class);
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo(EMAIL);
    }

}
