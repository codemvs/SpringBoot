package com.codemvs.springboot.app.models.dao;

import java.util.List;

import com.codemvs.springboot.app.models.entity.Cliente;

public interface IClienteDao {
	public List<Cliente> findAll();
}
