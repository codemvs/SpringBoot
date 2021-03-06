package com.codemvs.springboot.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="facturas")
public class Factura implements Serializable{
	
	@Id // Llave Primaria
	@GeneratedValue(strategy=GenerationType.IDENTITY)  // Autoincrementable
	private Long id;
		
	private String descripcion;
	private String observacion;
	
	@Temporal(TemporalType.DATE) // Fecha
	@Column(name="create_at") // Renormbrar nombre en BD 
	
	private Date createAt;
	
	// Muchas facturas, un cliente
	@ManyToOne(fetch = FetchType.LAZY) // Realiza la consulta a medida que se requiere
	private Cliente cliente;
	
	// Una factura y muchos item-factura
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	
	// Indicar que la realacion es de un solo sentido
	@JoinColumn(name="factura_id") // Llave foranea
	private List<ItemFactura> items;
	
	public Factura() {
		this.items = new ArrayList<ItemFactura>();
	}


	@PrePersist
	public void prePersist() {
		// Se encarga de generar la fecha
		createAt = new Date();
	}
	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	
	public List<ItemFactura> getItems() {
		return items;
	}

	public void setItems(List<ItemFactura> items) {
		this.items = items;
	}

	public void addIntemFacturas( ItemFactura item) {
		this.items.add(item);
	}
	
	public Double getTotal() {
		Double total = 0.0;
		
		int size = items.size();
		for(int i = 0; i< size; i++ ) {
			total += items.get(i).calcularImporte();
		}
		
		/*for(ItemFactura item: items ) {
			total += item.calcularImporte();
		}*/
		
		/*items.stream().forEach((i)->{
			total += i.calcularImporte();
		});*/
				
		return total;
	}
	private static final long serialVersionUID=1L;
}
