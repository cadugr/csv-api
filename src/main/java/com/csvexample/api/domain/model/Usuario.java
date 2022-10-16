package com.csvexample.api.domain.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.csvexample.api.config.CsvBindByNameOrder;
import com.opencsv.bean.CsvBindByName;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@CsvBindByNameOrder({"nome","email"})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    
	@EqualsAndHashCode.Include
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@CsvBindByName(column = "nome")
    private String nome;
	@CsvBindByName(column = "email")
    private String email;
    
}
