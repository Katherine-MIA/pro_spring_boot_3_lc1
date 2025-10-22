package com.apress.users;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
// This means that this class is responsible for every incoming request
//@RestController
// This means that there will be a based /users endpoint for every request (the path of the endpoint)
//@RequestMapping("/users")
public class InitialUsersController {
    /*
    private Map<String,User> users = new HashMap() {{
        put("HardcodedUser@email.com",new User("HardcodedUser@email.com","Hardcoded User"));
        put("AnotherHardcodedUser@email.com",new User("AnotherHardcodedUser@email.com","Another Hardcoded User"));
    }};

    The code above is suggested by the book.
    Oh boy does Sonar Qube have a lot to say about it.
    I will rewrite it using all Sonar Qube suggestions because not even I can stand so many warnings (Sorry book :()
     */
    //Here now Sonar Qube will ask for the field to be final
    //And that's why is bad to have hardcoded values for tests outside test areas
    private Map<String, User> users;

    public InitialUsersController() {
        this.users = new HashMap<>();
        this.users.put("HardcodedUser@email.com", User.builder()
                        .name("Hardcoded User")
                        .email("HardcodedUser@email.com")
                        .active(true)
                        .role(UserRole.USER)
                        .role(UserRole.INFO)
                        .role(UserRole.ADMIN)
                        .password("adm!nPa55")
                        .build());
        this.users.put("AnotherHardcodedUser@email.com",
                User.builder()
                        .name("Another Hardcoded User")
                        .email("AnotherHardcodedUser@email.com")
                        .role(UserRole.USER)
                        .password("usrPa55wd!")
                        .build());
    }
    // Says that this method should be executed for GET requests (without a body or  a path variable)
    //@GetMapping
    public Collection<User> getAll(){
        return this.users.values();
    }

    // Says this method will be executed for every GET request made with a path variable
    //@GetMapping("/{email}")
    public User findUserByEmail(@PathVariable String email){
        return this.users.get(email);
    }

    // This method will be executed for every POST request with a request body
    //@PostMapping
    public User save(@RequestBody User user){
        this.users.put(user.getEmail(),user);
        //here book says to just return user but, I find that if I search for it in the map,
        //then, if found and returned, should serve as proof that it was successfully saved
        return this.users.get(user.getEmail());
    }

    // This method will be executed for every DELETE request with a path variable
    //@DeleteMapping("/{email}")
    public void save(@PathVariable String email){
        this.users.remove(email);
    }


}
