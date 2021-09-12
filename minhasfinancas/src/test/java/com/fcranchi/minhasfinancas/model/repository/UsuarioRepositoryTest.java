package com.fcranchi.minhasfinancas.model.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fcranchi.minhasfinancas.model.entity.Usuario;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class UsuarioRepositoryTest {
	
	@Autowired
	UsuarioRepository repository;
	
	@Test
	public void deveVerificarAExistenciaDoEmail() {
		
		String email = "fabio@email.com";
		Usuario usuario = Usuario.builder().nome("usuario").email(email).build();
		repository.save(usuario);
		
		boolean resultado = repository.existsByEmail(email);
		
		Assertions.assertThat(resultado).isTrue();
	}
	
	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComEmail() {
		repository.deleteAll();
		
		boolean resultado = repository.existsByEmail("fabio@email.com");
		
		Assertions.assertThat(resultado).isFalse();
	}
	
	

}
