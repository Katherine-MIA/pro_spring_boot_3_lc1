package com.apress.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//For this user model I'm going to cheat a little bit and use lombok just for getters and setters.
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String email;
    //I also took the liberty to change this cus who the smack wants to be held accountable for holding
    //actual names for their users. So usernames it is.
    private String username;
    //LaterEdit: If I knew how much time I was going to waste trying to successfully integrate lombok
    //^ I would have just written the boilerplate as in the book and be done with it, but nooo
    //String me = Character.toString(0x1F921)
}
