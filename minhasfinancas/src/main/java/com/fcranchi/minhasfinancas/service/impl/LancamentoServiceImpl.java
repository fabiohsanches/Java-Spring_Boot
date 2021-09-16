package com.fcranchi.minhasfinancas.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fcranchi.minhasfinancas.model.entity.Lancamento;
import com.fcranchi.minhasfinancas.model.enums.StatusLancamento;
import com.fcranchi.minhasfinancas.model.repository.LancamentoRepository;
import com.fcranchi.minhasfinancas.service.LancamentoService;
import com.fcranchi.minhasfinancas.service.exception.RegraNegocioException;

@Service
public class LancamentoServiceImpl implements LancamentoService {

	private LancamentoRepository repository;
	
	public LancamentoServiceImpl(LancamentoRepository repository) {
		this.repository = repository;
	}
	
	@Override
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		validar(lancamento);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		validar(lancamento);
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public void deletar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		repository.delete(lancamento);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
		
		Example example = Example.of(lancamentoFiltro, ExampleMatcher.matching()
				.withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING));
		
		return repository.findAll(example);
	}
	
	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatus(status);
		atualizar(lancamento);
	}

	
	@Override
	public void validar(Lancamento lancamento) {
		if (lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")) {
			throw new RegraNegocioException("Informe uma descrição valida!");
		}
		
		if (lancamento.getMes() == null || lancamento.getMes() < 1 ||  lancamento.getMes() > 12   ) {
			throw new RegraNegocioException("Informe um mês valido!");
		}
		
		if (lancamento.getAno() == null || lancamento.getAno().toString().length() != 4   ) {
			throw new RegraNegocioException("Informe uma Ano valida!");
		}
		
		if (lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null ) {
			throw new RegraNegocioException("Informe um usuário!");
		}
		
		if (lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1 ) {
			throw new RegraNegocioException("Informe um valor valido!");
		}
		
		if (lancamento.getTipo() == null  ) {
			throw new RegraNegocioException("Informe um  tipo de lançamento!");
		}
		
		
		
	}

}