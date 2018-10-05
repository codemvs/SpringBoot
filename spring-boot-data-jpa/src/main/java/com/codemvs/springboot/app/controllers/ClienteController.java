package com.codemvs.springboot.app.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codemvs.springboot.app.models.dao.IClienteDao;
import com.codemvs.springboot.app.models.entity.Cliente;
import com.codemvs.springboot.app.service.IClienteService;

@Controller
@SessionAttributes("cliente")
public class ClienteController {
	//busca un componente registrado para ijectar
	@Autowired
	//@Qualifier("clienteDaoJPA") //identificador de cliente impl para evitar ambiguedades
	private IClienteService clienteService;
	/**
	 * listar cliente
	 * @param model
	 * @return
	 */
	@RequestMapping(value="listar",method=RequestMethod.GET)
	public String listar(Model model) {
			model.addAttribute("titulo","Listado de Clientes");
			model.addAttribute("clientes",clienteService.findAll());
			return "listar";
	}
	/**
	 * crear cliente
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/form") //get
	public String crear(Map<String,Object> model) {
		Cliente cliente = new Cliente();
		model.put("titulo", "Formulario Cliente");
		model.put("cliente", cliente);
		return "form";
	}
	/**
	 * Editar cliente
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/form/{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Cliente cliente = null;
		if(id>0) {
			cliente = clienteService.findOne(id);
			
			if(cliente == null) {
				flash.addFlashAttribute("error","El ID del cliente no existe en la base de datos.");
				return "redirect:/listar";
			}
			
		}else {
			flash.addAttribute("error","El ID del cliente no puede ser cero!");
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Editar Cliente");
		return "form";
	}
	/**
	 * Guardar cliente
	 * @param cliente
	 * @param result
	 * @param model
	 * @param status
	 * @return
	 */
	@RequestMapping(value="/form",method=RequestMethod.POST) //post
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status) {
		if(result.hasErrors()) {
			model.addAttribute("titulo","Formulario Clientes");
			return "form";
		}
		String mensaje = (cliente.getId()!=null)?"Cliente editado con éxito!":"Cliente creado con éxito!";
		clienteService.save(cliente);
		status.setComplete(); // elimina el objeto cliente de la sesion
		
		flash.addFlashAttribute("success",mensaje); // mensajes flash
		
		return "redirect:listar";
	}
	/**
	 * Eliminar
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id, RedirectAttributes flash) {
		if(id>0) {
			flash.addFlashAttribute("success","Cliente ha eliminado con éxito"); // mensajes flash
			clienteService.delete(id);
		}
		
		return "redirect:/listar";
	}
	
}
