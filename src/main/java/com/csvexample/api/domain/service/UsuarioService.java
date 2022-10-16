package com.csvexample.api.domain.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.csvexample.api.config.HeaderColumnAndOrderMappingStrategy;
import com.csvexample.api.domain.model.Usuario;
import com.csvexample.api.domain.repository.UsuarioRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

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

    public void generateCsvFile(HttpServletResponse response) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
     
        StatefulBeanToCsvBuilder<Usuario> builder= new StatefulBeanToCsvBuilder<Usuario>(response.getWriter()); 
        StatefulBeanToCsv<Usuario> beanWriter =  builder
			.withApplyQuotesToAll(false)
			.withMappingStrategy(new HeaderColumnAndOrderMappingStrategy<>(Usuario.class))
			.build(); 

        beanWriter.write(usuarioRepository.findAll());
 
    }

}
