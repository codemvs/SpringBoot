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

	@Override
	@Transactional //lectura y escritura
	public void save(Cliente cliente) {
		// TODO Auto-generated method stub
		if(cliente.getId()!=null && cliente.getId()>0) {
			// actualiza si el id existe en la base de datos
			em.merge(cliente);
		}else {
			em.persist(cliente);
		}
		
	}

	@Override
	public Cliente findOne(Long id) {
		// TODO Auto-generated method stub
		return em.find(Cliente.class,id);
	}

}
