package com.apress.users;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.servlet.function.RequestPredicates.accept;
import static org.springframework.web.servlet.function.RouterFunctions.route;

/*
* You can write functions to define routes and handle requests and spring MVC will recognise and use them.
* "Every HTTP request is handled by a HandlerFunction(RouterFunction) that takes a ServerRequest and returns a
* ServerResponse."
* "These requests are routed to a RouterFunction that takes the ServerRequest and
returns an optional HandlerFunction. You can consider this RouterFunction as being
equivalent to the @RequestMapping annotation but with the advantage that it deals not
only with data but also with behavior."
 */
@Configuration // Helps spring boot when it searches for beans to add to its context.
public class UsersRoutes {

    @Bean
    // RouterFunction<ServerResponse> -> Interface that defines methods to build a RouterFunction. It receives
    // a UsersHandler, this is injected by spring if the class is annotated with @Component or @Bean.
    // "The common way it to use the Fluent API that defines the RouterFunction interface."
    public RouterFunction<ServerResponse> userRoutes(UsersHandler usersHandler){
        // Route is a Fluent API that defines the necessary routing depending on the endpoint defined.
        // For this example, the common path /users is created using the RequestPredicates abstract class.
        // Aka defines the /users path as the routing for requests.
        // Builder (java.util.function.Consumer) -> specifies the methods that are routed to the handler.
        // In other words, here is specified for each request their attributed handler function.
        return route().nest(RequestPredicates.path("/users"), builder -> {
            builder.GET("", accept(APPLICATION_JSON), usersHandler::findAll);
            builder.GET("", accept(APPLICATION_JSON), usersHandler::findUserByEmail);
            builder.POST("", usersHandler::save);
            builder.DELETE("/{email}", usersHandler::deleteByEmail);
        }).build();
    }

    // When declaring web API endpoints in a functional way, like in this case. The @Valid will not trigger the default
    // validation as usual, that too must be specified and called. So the default one can be used for validation.
    // It will be called from the handler functions and is sent from here and will be used for the cascade validation.
    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }
}
