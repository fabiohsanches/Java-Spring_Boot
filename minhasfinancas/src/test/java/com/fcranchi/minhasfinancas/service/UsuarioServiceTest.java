package com.fcranchi.minhasfinancas.service;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
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
	
	@SpyBean
	UsuarioServiceImpl service;
	
	@MockBean
	UsuarioRepository repository;
	

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
	
	@Test()
	public void deveLanvcarErroQuandoASenhaDoUsuarioEstiverIncorretaComparandocomRetorno() {
		Usuario usuario = Usuario.builder().email("fabio.cranchi@email.com").senha("senha").build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		
		Throwable exception =  org.assertj.core.api.Assertions.catchThrowable( () -> service.autenticar("fabio@email.com", "123") );
		
		org.assertj.core.api.Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha inválida!");
	}
	
	
	@Test()
	public void deveSalvarUmUsuario() {
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder().id(1L).nome("Fabio").email("fabio@email.com").senha("senha").build();
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		Usuario usuarioSalvo = service.salvarUsuario(usuario);
		
		org.assertj.core.api.Assertions.assertThat(usuarioSalvo).isNotNull();
		org.assertj.core.api.Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1L);
		org.assertj.core.api.Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("Fabio");
		org.assertj.core.api.Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("fabio@email.com");
		org.assertj.core.api.Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
	}
	
	@Test
	public void naoDeveSalvarUsuarioComEmailCadastrado() {
		String email = "fabio.@email.com";
		Usuario usuario = Usuario.builder().email(email).build();
		
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
		
		Mockito.verify(repository, Mockito.never()).save(usuario);
	}
	
	
	
}
