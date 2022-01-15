package com.example.demo;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.demo.model.Account;
import com.example.demo.service.AccountsService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	CommandLineRunner runner(AccountsService accountsService){
		return args -> {
			// read json and write to h2 db
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<List<Account>> typeReference = new TypeReference<List<Account>>(){};
			InputStream inputStream = TypeReference.class.getResourceAsStream("/accounts.json");
			try{
				List<Account> accounts = mapper.readValue(inputStream, typeReference);
				accountsService.saveAccounts(accounts);
				System.out.println("Accounts saved!");
				System.out.println("Ready to make a transfer!"); 
				
			}catch(IOException e){
				System.out.println("unable to save the accounts: " + e.getMessage());
			}
			
			
		};
		
	}
}
