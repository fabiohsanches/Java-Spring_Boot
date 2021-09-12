package com.fcranchi.minhasfinancas.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fcranchi.minhasfinancas.model.entity.Usuario;
import com.fcranchi.minhasfinancas.model.repository.UsuarioRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	@Autowired
	UsuarioService service;
	
	@Autowired
	UsuarioRepository repository;
	
	@Test()
	public void deveValidarEmail() {
		repository.deleteAll();
		service.validarEmail("fabio@email.com");
	}
	
	@Test()
	public void deveRetornarErroAoValidarEmailCadastrado() {
	
		String email = "fabio@email.com";
		Usuario usuario = Usuario.builder().nome("Fabio").email(email).build();
		repository.save(usuario);
		
		service.validarEmail(email);	
	}
	
}
