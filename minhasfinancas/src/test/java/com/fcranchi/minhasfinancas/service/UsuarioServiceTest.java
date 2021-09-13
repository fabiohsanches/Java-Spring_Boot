package com.fcranchi.minhasfinancas.service;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fcranchi.minhasfinancas.model.entity.Usuario;
import com.fcranchi.minhasfinancas.model.repository.UsuarioRepository;
import com.fcranchi.minhasfinancas.service.exception.ErroAutenticacao;
import com.fcranchi.minhasfinancas.service.exception.RegraNegocioException;
import com.fcranchi.minhasfinancas.service.impl.UsuarioServiceImpl;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	UsuarioService service;
	
	@MockBean
	UsuarioRepository repository;
	
	@BeforeEach
	public void setUp() {
		service = new UsuarioServiceImpl(repository);
	}
	
	@Test
	public void deveValidarEmail() {
		Mockito.when(repository.existsByEmail( Mockito.anyString() )).thenReturn(false);
		
		service.validarEmail("fabio@email.com");
	}

	@Test
	public void deveRetornarErroAoValidarEmailCadastrado() {
		Assertions.assertThrows(RegraNegocioException.class, () -> {
			Mockito.when(repository.existsByEmail( Mockito.anyString() )).thenReturn(true);
			
			service.validarEmail("fabio@email.com");
		});
	}
	
	@Test
	public void deveAutenticarUsuarioComSucesso() {
		String email = "fabio@email.com";
		String senha = "senha";
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario)) ;
		
		Usuario result = service.autenticar(email, senha);
		
		org.assertj.core.api.Assertions.assertThat(result).isNotNull();
	}
	
	@Test()
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastrado() {
		Assertions.assertThrows(ErroAutenticacao.class, () -> {
			Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
			
			service.autenticar("fabio@email.com", "senha");
		});
	}
	
	@Test()
	public void deveLanvcarErroQuandoASenhaDoUsuarioEstiverIncorreta() {
		Assertions.assertThrows(ErroAutenticacao.class, () -> {
			Usuario usuario = Usuario.builder().email("fabio.cranchi@email.com").senha("senha").build();
			Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
			
			service.autenticar("fabio@email.com", "123");
		});
		
	}
}
