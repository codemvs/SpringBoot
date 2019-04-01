package com.codemvs.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.codemvs.springboot.app.model.service.IUploadFileService;

@SpringBootApplication
public class SpringBootDataJpaApplication implements CommandLineRunner{
	// Injectar clase Service
	@Autowired
	private IUploadFileService uploadFileService;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootDataJpaApplication.class, args);
	}
	
	/**
	 * Eliminar y crear directorio upload de forma automatica 
	 */
	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
		uploadFileService.deleteAll();
		uploadFileService.init();
		
	}
}
