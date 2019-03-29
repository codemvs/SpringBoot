package com.codemvs.springboot.app.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileServiceImplmpl implements IUploadFileService {

	// Logg
	private final Logger log = LoggerFactory.getLogger(getClass());
	// Upload
	private final static String UPLOADS_FOLDER = "uploads";

	/**
	 * Funcion que permite renderizar imagen
	 * 
	 * @param String fileName | Nombre del archivo
	 * @return Resource | Regresa un recurso.
	 */
	
	@Override	
	public Resource load(String fileName) throws MalformedURLException {		

		Path pathFoto = getPath(fileName);
		log.info("::::pathFoto::: " + pathFoto);
		Resource recurso = null;

		recurso = (Resource) new UrlResource(pathFoto.toUri());
		if (!recurso.exists() || !recurso.isReadable()) {
			throw new RuntimeException("Error: No se puede cargar la imagen " + pathFoto.toString());
		}
			
		
		return recurso;
	}
	
	/**
	 * Copiar archivo local al servidor
	 * 
	 * @param MultipartFile file | archivo  
	 * @return String | Nombre del archivo copiado
	 */
	
	@Override
	public String copy(MultipartFile file) throws IOException{
		
		String uniqueFileName = UUID.randomUUID().toString()+"_"+file.getOriginalFilename();
		
		Path rootPath = getPath(uniqueFileName);
				
		log.info("rootPath: "+rootPath);
		
		Files.copy(file.getInputStream(),rootPath);
		
		return uniqueFileName;
	}
	/**
	 * Eliminar archivo del servidor
	 * @param String fileName | nombre archivo a eliminar
	 * @return boolean 
	 */
	@Override
	public boolean delete(String fileName) {
		
		Path rootPath = getPath(fileName);
		File archivo = rootPath.toFile();
		
		if(archivo.exists() && archivo.canRead() ) {
			
				return archivo.delete();
			
		}
		return false;
	}
	
	/**
	 * Obtener Paht 
	 * @param fileName | nombre del archivo
	 * @return Path
	 */
	
	public Path getPath( String fileName ) {
		return Paths.get(UPLOADS_FOLDER).resolve(fileName).toAbsolutePath();		
	}
	
	/**
	 * Eliminar todos los archivos del directorio uploads
	 */
	
	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		FileSystemUtils.deleteRecursively(Paths.get(UPLOADS_FOLDER).toFile());
	}
	
	/**
	 * Crear directorio uploads 
	 */
	
	@Override
	public void init() throws IOException {
		// TODO Auto-generated method stub
		Files.createDirectories(Paths.get(UPLOADS_FOLDER));
	}
}
