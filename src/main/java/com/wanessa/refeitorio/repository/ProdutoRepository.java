package com.wanessa.refeitorio.repository;

import com.wanessa.refeitorio.model.Produto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findAllByOrderByNomeAsc();

    List<Produto> findByAtivoTrueOrderByNomeAsc();
}