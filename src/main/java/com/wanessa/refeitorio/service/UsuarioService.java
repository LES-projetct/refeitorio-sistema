/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.service;

import com.wanessa.refeitorio.model.Usuario;
import com.wanessa.refeitorio.repository.CompraRepository;
import com.wanessa.refeitorio.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author wanes
 */
@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final CompraRepository compraRepository;

    public UsuarioService(UsuarioRepository repository,
            CompraRepository compraRepository) {
        this.repository = repository;
        this.compraRepository = compraRepository;
    }

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    public Usuario salvar(Usuario usuario) {

        repository.findByCodigoRfid(usuario.getCodigoRfid())
                .ifPresent(usuarioExistente -> {

                    if (usuario.getId() == null
                            || !usuarioExistente.getId().equals(usuario.getId())) {

                        throw new IllegalArgumentException(
                                "Já existe usuário com este RFID");
                    }
                });

        // Cadastro de novo usuário
        if (usuario.getId() == null) {

            if (usuario.getAtivo() == null) {
                usuario.setAtivo(true);
            }

            return repository.save(usuario);
        }

        // Edição de usuário existente
        Usuario existente = repository.findById(usuario.getId())
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Usuário não encontrado"));

        existente.setNome(usuario.getNome());
        existente.setEmail(usuario.getEmail());
        existente.setCodigoRfid(usuario.getCodigoRfid());
        existente.setSaldo(usuario.getSaldo());
        existente.setLimiteCredito(usuario.getLimiteCredito());

        // Saldo negativo não pode ficar ativo
        if (usuario.getSaldo() != null
                && usuario.getSaldo().compareTo(BigDecimal.ZERO) < 0) {

            existente.setAtivo(false);

        } else {

            // Saldo zero ou positivo permite alterar o status
            existente.setAtivo(usuario.getAtivo());
        }

        return repository.save(existente);
    }

    public List<Usuario> listarInadimplentes() {
        return repository.findBySaldoLessThan(BigDecimal.ZERO);
    }

    public Usuario buscarPorId(Long id) {

        return repository.findById(id)
                .orElseThrow(()
                        -> new RuntimeException("Usuário não encontrado"));
    }

    public void excluir(Long id) {

        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        boolean possuiCompras = compraRepository.existsByUsuarioId(id);

        if (possuiCompras) {
            usuario.setAtivo(false);
            repository.save(usuario);
        } else {
            repository.deleteById(id);
        }
    }

    public List<Usuario> listarBloqueados() {
        return repository.findByAtivoFalse();
    }

    public Usuario buscarPorRfid(String codigoRfid) {
        return repository.findByCodigoRfid(codigoRfid)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

}
