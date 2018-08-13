package com.codemvs.thymeleaf.com.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
	@GetMapping("/")
	public String hola(Model model) {
		model.addAttribute("mensaje","Hola tymelief Spring");
		return "hola";
	}
}
