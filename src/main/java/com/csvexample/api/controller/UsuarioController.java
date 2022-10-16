package com.csvexample.api.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.csvexample.api.domain.model.Usuario;
import com.csvexample.api.domain.repository.UsuarioRepository;
import com.csvexample.api.domain.service.UsuarioService;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

	@PostMapping("/upload-csv-file") 
    public ResponseEntity<?> uploadCSVFile(@RequestParam("file") MultipartFile file) throws IOException {
    	usuarioService.uploadCSVFile(file);
        return ResponseEntity.ok("Arquivo importado com sucesso.");
    }

    @GetMapping(path = "/generate-csv-file")
    public void generateCsvFile(HttpServletResponse response) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException {
        String filename = "Usuarios.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        usuarioService.generateCsvFile(response);        
    }
}
    
