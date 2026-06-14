package com.wanessa.refeitorio.service;

import com.wanessa.refeitorio.model.Produto;
import com.wanessa.refeitorio.repository.ProdutoRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Produto> listarTodos() {
        return repository.findAllByOrderByNomeAsc();
    }

    @Transactional
    public Produto salvar(Produto produto) {

        if (produto.getAtivo() == null) {
            produto.setAtivo(true);
        }

        return repository.save(produto);
    }

    @Transactional(readOnly = true)
    public Produto buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    @Transactional
    public void desativar(Long id) {

        Produto produto = buscarPorId(id);

        produto.setAtivo(false);

        repository.save(produto);
    }

    @Transactional
    public void reativar(Long id) {

        Produto produto = buscarPorId(id);

        produto.setAtivo(true);

        repository.save(produto);
    }

    @Transactional
    public void excluir(Long id) {
        desativar(id);
    }

    @Transactional(readOnly = true)
    public List<Produto> listarAtivos() {
        return repository.findByAtivoTrueOrderByNomeAsc();
    }
}
