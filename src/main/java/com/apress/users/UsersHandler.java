package com.apress.users;

import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor // creates the constructor for all fields of the classin which SpringBoot will
// inject the associated beans.
@Component // tells spring to create bean
public class UsersHandler {
    private final Repository userRepository;
    private final Validator validator;

    /*
    *  ServerRequest -> Has access to the body and headers of the server-side HTTP request.
    * It's handled by the handler function. (a bit obvious but maybe worth the mention)
    * The request gets routed to the function (as parameter).
    *  ServerResponse -> The function constructs a server-side HTTP response,
    * according to the business logic and returns it. It has several methods
    * for construction of the response.
     */
    public ServerResponse findAll(ServerRequest request){
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.userRepository.findAll());
    }

    public ServerResponse findUserByEmail(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.userRepository.findById(request.pathVariable("email")));
    }

    /*
     *  ServerRequest -> casts the request.body(<class-type>) to User.class (in this case). For this, it uses
     * message converters in the background.
     *  BindingResult -> validate(user) returns a BindingResult that invokes the validatior for every annotation
     * constraint on the fields of the object it receives as a parameter. If any errors occur, then they are
     * sent to be processed and the appropriate message for the ServerResponse is constructed.
     */
    public ServerResponse save(ServerRequest request) throws ServletException, IOException {
        User user = request.body(User.class);
        BindingResult bindingResult = validate(user);
        if(bindingResult.hasErrors()) {
            return prepareErrorResponse(bindingResult);
        }
        this.userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{email}")
                .buildAndExpand(user.getEmail())
                .toUri();
        return ServerResponse.created(location).body(user);
    }

    public ServerResponse deleteByEmail(ServerRequest request) {
        this.userRepository.deleteById(request.pathVariable("email"));
        return ServerResponse.noContent().build();
    }

    private BindingResult validate(User user) {
        DataBinder binder = new DataBinder(user);
        binder.addValidators(validator);
        binder.validate();
        return binder.getBindingResult();
    }

    /*
     * If the validation yields any errors they can be taken from @bindingResult and placed into a ServerResponse
     */
    private ServerResponse prepareErrorResponse(BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();

        response.put("msg", "There is an error.");
        response.put("code", HttpStatus.BAD_REQUEST.value());
        response.put("time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        Map<String, String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(fieldError ->
            errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
        response.put("errors", errors);
        return ServerResponse.badRequest().body(response);
    }
}
