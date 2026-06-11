/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.service;

import com.wanessa.refeitorio.model.Produto;
import com.wanessa.refeitorio.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author wanes
 */
@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public List<Produto> listarTodos() {
        return repository.findAll();
    }

    public Produto salvar(Produto produto) {
        return repository.save(produto);
    }

    public void excluir(Long id) {

        Produto produto = repository.findById(id)
                .orElseThrow(()
                        -> new RuntimeException("Produto não encontrado"));

        produto.setAtivo(false);

        repository.save(produto);
    }

    public Produto buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }
}
