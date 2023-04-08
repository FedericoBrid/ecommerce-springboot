package com.ecommerce.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.model.Orden;
import com.ecommerce.repository.IOrdenRepository;

@Service
public class OrdenServiceImpl implements IOrdenService {

	@Autowired
	private IOrdenRepository ordenRepository;
	
	@Override
	public Orden save(Orden orden) {
		
		return ordenRepository.save(orden);
	}

	@Override
	public List<Orden> findAll() {
		return ordenRepository.findAll();
	}

	public String generarNumeroOrden() {
		int numero = 0;
		String numeroConcatenado = "";
		List<Orden> ordenes = findAll();
		List<Integer> numeros = new ArrayList<Integer>();
		
		ordenes.stream().forEach(o->numeros.add(Integer.parseInt(o.getNumero())));
		
		if (ordenes.isEmpty()) {
			numero = 1;
		}else {
			numero=numeros.stream().max(Integer::compare).get();
			numero++;
		}
		
		if (numero<10) {
			numeroConcatenado = "00000"+ String.valueOf(numero);
		}else if(numero<100) {
			numeroConcatenado = "0000"+ String.valueOf(numero);
		}else if(numero<1000) {
			numeroConcatenado = "000"+ String.valueOf(numero);
		}else if(numero<10000) {
			numeroConcatenado = "00"+ String.valueOf(numero);
		}
		
		return numeroConcatenado;
	}

}
