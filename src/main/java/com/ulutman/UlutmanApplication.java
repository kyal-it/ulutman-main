package com.ulutman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class UlutmanApplication {

	public static void main(String[] args) {
		SpringApplication.run(UlutmanApplication.class, args);
		System.out.println("testPush1.1");
	}

	@GetMapping
	public String greetings(){
		return "index";
	}




}
