/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.service;

import com.wanessa.refeitorio.model.Compra;
import com.wanessa.refeitorio.model.ItemCompra;
import com.wanessa.refeitorio.model.Produto;
import com.wanessa.refeitorio.model.Usuario;
import com.wanessa.refeitorio.repository.CompraRepository;
import com.wanessa.refeitorio.repository.ProdutoRepository;
import com.wanessa.refeitorio.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author wanes
 */
@Service
public class CompraService {

    private final CompraRepository compraRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProdutoRepository produtoRepository;

    public CompraService(
            CompraRepository compraRepository,
            UsuarioRepository usuarioRepository,
            ProdutoRepository produtoRepository) {

        this.compraRepository = compraRepository;
        this.usuarioRepository = usuarioRepository;
        this.produtoRepository = produtoRepository;
    }

    public Compra salvarCompra(Compra compra) {

        Long usuarioId = compra.getUsuario().getId();

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(()
                        -> new IllegalArgumentException("Usuário não encontrado"));

        if (Boolean.FALSE.equals(usuario.getAtivo())) {
            throw new IllegalArgumentException(
                    "Usuário bloqueado não pode realizar compra");
        }

        if (usuario.getSaldo() == null) {
            throw new IllegalArgumentException(
                    "Usuário sem saldo cadastrado");
        }

        if (usuario.getLimiteCredito() == null) {
            throw new IllegalArgumentException(
                    "Usuário sem limite de crédito cadastrado");
        }

        BigDecimal saldoAnterior = usuario.getSaldo();

        if (compra.getItens() != null && !compra.getItens().isEmpty()) {

            compra.getItens().forEach(item -> {

                if (item.getProduto() == null || item.getProduto().getId() == null) {
                    throw new IllegalArgumentException("Produto não informado");
                }
                
                Produto produto = produtoRepository.findById(
                        item.getProduto().getId())
                        .orElseThrow(()
                                -> new IllegalArgumentException(
                                "Produto não encontrado"));

                item.setProduto(produto);

                if (Boolean.FALSE.equals(produto.getAtivo())) {

                    throw new IllegalArgumentException(
                            "Produto inativo não pode ser vendido");
                }

                item.setCompra(compra);

                if (produto.getVendidoPorPeso()) {

                    if (item.getPeso() == null) {
                        throw new IllegalArgumentException(
                                "Peso não informado");
                    }

                    item.setQuantidade(null);

                    item.setValorTotal(
                            item.getPeso().multiply(item.getValorUnitario()));

                } else {

                    if (item.getQuantidade() == null) {
                        throw new IllegalArgumentException(
                                "Quantidade não informada");
                    }

                    item.setPeso(null);

                    item.setValorTotal(
                            item.getQuantidade().multiply(item.getValorUnitario()));
                }
            });

            BigDecimal total = compra.getItens().stream()
                    .map(ItemCompra::getValorTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            compra.setValorTotal(total);
        }

        if (compra.getValorTotal() == null) {
            throw new IllegalArgumentException(
                    "Valor total da compra não informado");
        }

        BigDecimal novoSaldo
                = saldoAnterior.subtract(compra.getValorTotal());

        BigDecimal limiteNegativo
                = usuario.getLimiteCredito().negate();

        if (novoSaldo.compareTo(limiteNegativo) < 0) {
            throw new IllegalArgumentException(
                    "Limite de crédito excedido");
        }

        if (novoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            usuario.setAtivo(false);
        }

        usuario.setSaldo(novoSaldo);

        usuarioRepository.save(usuario);

        compra.setUsuario(usuario);
        compra.setSaldoAnterior(saldoAnterior);
        compra.setSaldoAtualizado(novoSaldo);
        compra.setDataHora(LocalDateTime.now());

        return compraRepository.save(compra);
    }

    public List<Compra> listarTodas() {
        return compraRepository.findAll();
    }

    public BigDecimal calcularFaturamentoTotal() {
        return compraRepository.calcularFaturamentoTotal();
    }

    public long quantidadeCompras() {
        return compraRepository.count();
    }
}
