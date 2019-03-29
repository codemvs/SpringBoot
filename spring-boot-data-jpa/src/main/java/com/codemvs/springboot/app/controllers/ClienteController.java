package com.codemvs.springboot.app.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
import java.net.MalformedURLException;
import com.codemvs.springboot.app.models.entity.Cliente;
import com.codemvs.springboot.app.service.IClienteService;
import com.codemvs.springboot.app.service.IUploadFileService;
import com.codemvs.springboot.app.util.paginator.PageRender;


@Controller
@SessionAttributes("cliente")
public class ClienteController {
	
	//busca un componente registrado para ijectar
	
	@Autowired
	//@Qualifier("clienteDaoJPA") //identificador de cliente impl para evitar ambiguedades
	private IClienteService clienteService;
	
	@Autowired
	private IUploadFileService uploadFileService;
	
	/**
	 * Renderizar imagen de forma programatica
	 * @param filename
	 * @return
	 */
	@GetMapping(value="/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename){
		
		Resource recurso=null;
		try {
			recurso = uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,"attachment: filename=\""+recurso.getFilename()+"\"")
				.body(recurso);
		
	}
	
	/**
	 * Metodo que permte mostrar el detalle de un cliente
	 * @param id
	 * @param model
	 * @param flash
	 * @return String
	 */
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
			//Eliminar foto al actualizar el usuario
			if( cliente.getId() != null &&
					cliente.getId() > 0 &&
					cliente.getFoto() != null &&
					cliente.getFoto().length() > 0 ) {
				
				//Eliminar foto al eliminar cliente
				uploadFileService.delete(cliente.getFoto());
			}
			// Mover imagen
			String uniqueFileName=null;
			try {
				uniqueFileName = uploadFileService.copy(foto);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			flash.addFlashAttribute("info","Has subido correctamente "+ uniqueFileName);
			
			cliente.setFoto(uniqueFileName);
			
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
			 
			Cliente cliente = clienteService.findOne(id);
			
			flash.addFlashAttribute("success","Cliente ha eliminado con éxito"); // mensajes flash
			clienteService.delete(id);
			
			//Eliminar foto al eliminar cliente
			if(uploadFileService.delete(cliente.getFoto())) {
				flash.addFlashAttribute("info","Foto "+cliente.getFoto()+" eliminar con éxito");
			}
			
			
		}
		
		return "redirect:/listar";
	}
	
}
