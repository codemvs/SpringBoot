package com.codemvs.springboot.app.models.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.codemvs.springboot.app.models.entity.Cliente;
//marca la clase como componente de persisntencia de accedo a datos
@Repository("clienteDaoJPA")
public class ClienteDaoImp implements IClienteDao {
	//injecta al em segun la configuracion de persistencia
	//que contiene el dataSource
	@PersistenceContext
	private EntityManager em; //maneja las clases de entidades
	
	@SuppressWarnings("unchecked")
	@Transactional()
	@Override
	public List<Cliente> findAll() {
		// TODO Auto-generated method stub
		return em.createQuery("from Cliente").getResultList();
	}

}
