package com.codemvs.springboot.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.codemvs.springboot.app.models.entity.Producto;

public interface IProductoDao extends CrudRepository<Producto,Long>{
	
	@Query("select p from Producto where p.nombre like %?1%")
	public List<Producto> findByName(String term);
	
}
