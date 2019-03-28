package com.codemvs.springboot.app.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.codemvs.springboot.app.models.dao.IClienteDao;
import com.codemvs.springboot.app.models.entity.Cliente;
import com.codemvs.springboot.app.service.IClienteService;
import com.codemvs.springboot.app.util.paginator.PageRender;

@Controller
@SessionAttributes("cliente")
public class ClienteController {
	//busca un componente registrado para ijectar
	@Autowired
	//@Qualifier("clienteDaoJPA") //identificador de cliente impl para evitar ambiguedades
	private IClienteService clienteService;
	
	@GetMapping(value="/ver/{id}")
	public String ver(@PathVariable(value="id") Long id, Map<String, Object> model,RedirectAttributes flash) {
		Cliente cliente = clienteService.findOne(id);
		
		if(cliente == null) {
			flash.addFlashAttribute("error","El cliente no existe en la BD");
			return "redirect:/listar";
		}
		
		model.put("cliente",cliente);
		model.put("titulo","Detalle cliente: "+cliente.getNombre());
		
		return "ver";
	}
	
	/**
	 * listar cliente
	 * @param model
	 * @return
	 */
	@RequestMapping(value="listar",method=RequestMethod.GET)
	public String listar(@RequestParam(name="page", defaultValue="0") int page,Model model) {
			// paginable
			Pageable pagesRequest = new PageRequest(page, 5);
			Page<Cliente> clientes = clienteService.findAll(pagesRequest);
	
			// Paginador
			PageRender<Cliente> pageRender = new PageRender<>("/listar",clientes);
			
			model.addAttribute("titulo","Listado de Clientes");
			//model.addAttribute("clientes",clienteService.findAll());
			model.addAttribute("clientes",clientes);
			model.addAttribute("page",pageRender);
			
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
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model,@RequestParam("file") MultipartFile foto,RedirectAttributes flash, SessionStatus status) {
		if(result.hasErrors()) {
			model.addAttribute("titulo","Formulario Clientes");
			return "form";
		}
		if(!foto.isEmpty()) {
			Path directorioRecursos = Paths.get("src//main//resources//static/uploads");
			String rootPath = directorioRecursos.toFile().getAbsolutePath();
			try {
				byte[] bytes = foto.getBytes();
				String nombreFoto = foto.getOriginalFilename();
				Path rutaCompleta = Paths.get(rootPath+"//"+ nombreFoto);
				
				Files.write(rutaCompleta, bytes);
				flash.addFlashAttribute("info","Has subido correctamente "+ nombreFoto);
				
				cliente.setFoto(nombreFoto);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
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
