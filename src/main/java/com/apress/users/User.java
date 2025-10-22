package com.apress.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

//For this user model I'm going to cheat a little bit and use lombok just for getters and setters.
//@Setter
//@Getter
//@AllArgsConstructor -> Usually builder pattern doesn't require an AllArgsConstructor but it depends.
//@NoArgsConstructor -> Same as above.
@Builder
@Data
public class User {
    @NotBlank(message = "Email can not be empty")
    private String email;
    //I also took the liberty to change this cus who the smack wants to be held accountable for holding
    //actual names for their users. So usernames it is.
    @NotBlank(message = "Name can not be empty") // For web request validation
    private String name;
    //String me = Character.toString(0x1F921)

    private String gravatarUrl;

    @Pattern( message = "Password must be at least 8 characters long and contain at least one number, one uppercase," +
            " one lowercase and one special character.",
    regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$" )
    // SonarQube says \\d decimal class is better than [0-9], still regex to me.
    private String password;

    @Singular("role") // against duplicates
    private List<UserRole> userRole;

    private boolean active;
}
