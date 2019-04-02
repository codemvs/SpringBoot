package com.codemvs.springboot.app.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codemvs.springboot.app.model.service.IClienteService;
import com.codemvs.springboot.app.models.entity.Cliente;
import com.codemvs.springboot.app.models.entity.Factura;

//localhost:8080/factura/form/1

@Controller
@RequestMapping("/factura") 	
@SessionAttributes("factura")
public class FacturaController {
	
	@Autowired
	private IClienteService clienteService;
	
	@GetMapping("/form/{clienteId}")
	public String crear(@PathVariable(value="clienteId") Long clienteId, 
			Map<String, Object> model, RedirectAttributes flash){
		
		Cliente cliente = clienteService.findOne(clienteId);
		if(cliente == null) {
			flash.addAttribute("error","El cliente no existe en la base de datos");
			return "redirect:/listar";
		}
		
		// crear la factura
		Factura factura = new Factura();
		factura.setCliente(cliente);
		
		model.put("factura",factura);
		model.put("titulo","Crear Factura");
		
		return "factura/form";
	}
}
