package com.csvexample.api.domain.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.csvexample.api.domain.model.Usuario;
import com.csvexample.api.domain.repository.UsuarioRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void uploadCSVFile(MultipartFile file) throws IOException {

		Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));

		CsvToBean<Usuario> csvToBean = new CsvToBeanBuilder(reader)
				.withType(Usuario.class)
				.withIgnoreLeadingWhiteSpace(true)
				.build();

		List<Usuario> usuarios = csvToBean.parse();

		usuarioRepository.saveAll(usuarios);

	}

}
