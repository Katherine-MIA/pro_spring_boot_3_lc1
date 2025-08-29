package com.apress.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UsersApplication {

	public static void main(String[] args) {
		//The book makes an error here by saying "UserApplication.java ... uses the static SpringApplication class"
		//This if I understand java correctly is not true, if we check out the implementation of SpringApplication class
		// we will see that while it has static methods and static fields and even static nested classes (as far as
		// I know they are the only possible static classes) the class itself is not static. So in the main method
		//the static method SpringApplication.run() is called.
		SpringApplication.run(UsersApplication.class, args);
	}
}
//Note to self: check books before you make git repo set-ups this repo edit was hell and led to nothing
//so sad I will cry