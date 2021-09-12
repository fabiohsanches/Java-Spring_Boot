package com.fcranchi.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fcranchi.minhasfinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

}
