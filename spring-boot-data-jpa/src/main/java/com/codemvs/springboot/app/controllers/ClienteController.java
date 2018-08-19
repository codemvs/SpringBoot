package com.codemvs.springboot.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.codemvs.springboot.app.models.dao.IClienteDao;

@Controller
public class ClienteController {
	//busca un componente registrado para ijectar
	@Autowired
	@Qualifier("clienteDaoJPA") //identificador de cliente impl para evitar ambiguedades
	private IClienteDao clienteDao;
	
	@RequestMapping(value="listar",method=RequestMethod.GET)
	public String listar(Model model) {
			model.addAttribute("titulo","Listado de Clientes");
			model.addAttribute("clientes",clienteDao.findAll());
			return "listar";
	}
}
