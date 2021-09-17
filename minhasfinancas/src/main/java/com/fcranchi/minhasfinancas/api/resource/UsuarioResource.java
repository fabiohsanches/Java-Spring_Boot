package com.fcranchi.minhasfinancas.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fcranchi.minhasfinancas.api.dto.UsuarioDTO;
import com.fcranchi.minhasfinancas.model.entity.Usuario;
import com.fcranchi.minhasfinancas.service.UsuarioService;
import com.fcranchi.minhasfinancas.service.exception.ErroAutenticacao;
import com.fcranchi.minhasfinancas.service.exception.RegraNegocioException;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice.Return;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioResource {

	private final UsuarioService service;
	
	@PostMapping("/autenticar")
	public ResponseEntity autenticar(@RequestBody UsuarioDTO dto) {

		String email = dto.getEmail();
		String senha = dto.getSenha();
		
		try { 
			  Usuario usuarioAutenticado = service.autenticar(email,senha); 
			  return new ResponseEntity(usuarioAutenticado,HttpStatus.CREATED); 
			  // return ResponseEntity.ok(usuarioAutenticado); 
		  } catch(ErroAutenticacao e) {
			  return ResponseEntity.badRequest().body(e.getMessage()); 
			  
		  }
		 
	}

	@PostMapping
	public ResponseEntity salvar(@RequestBody UsuarioDTO dto) {
		Usuario usuario = Usuario.builder().nome(dto.getNome()).email(dto.getEmail()).senha(dto.getSenha()).build();

		try {
			Usuario usuarioSalvo = service.salvarUsuario(usuario);
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
