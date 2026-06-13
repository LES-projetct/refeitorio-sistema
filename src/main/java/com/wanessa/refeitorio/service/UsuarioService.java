/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.service;

import com.wanessa.refeitorio.model.Usuario;
import com.wanessa.refeitorio.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.wanessa.refeitorio.repository.ContaSistemaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

/**
 *
 * @author wanes
 */
@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final ContaSistemaRepository contaSistemaRepository;
    private static final SecureRandom RANDOM = new SecureRandom();

    public UsuarioService(
            UsuarioRepository repository,
            ContaSistemaRepository contaSistemaRepository) {

        this.repository = repository;
        this.contaSistemaRepository
                = contaSistemaRepository;
    }

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    public Usuario salvar(Usuario usuario) {

        repository.findByCodigoRfid(usuario.getCodigoRfid())
                .ifPresent(usuarioExistente -> {

                    if (usuario.getId() == null
                            && (usuario.getCodigoRfid() == null
                            || usuario.getCodigoRfid().isBlank())) {

                        usuario.setCodigoRfid(
                                gerarCodigoRfidAutomatico()
                        );
                    }

                    if (usuario.getCodigoRfid() != null) {
                        usuario.setCodigoRfid(
                                usuario.getCodigoRfid().trim().toUpperCase()
                        );
                    }

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

        BigDecimal saldo = usuario.getSaldo();
        BigDecimal limiteCredito = usuario.getLimiteCredito();

        if (saldo != null && limiteCredito != null) {

            BigDecimal limiteNegativo
                    = limiteCredito.negate();

            if (saldo.compareTo(limiteNegativo) < 0) {
                throw new IllegalArgumentException(
                        "O saldo não pode ultrapassar o limite de crédito"
                );
            }
        }

        if (usuario.getAtivo() != null) {
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

    /**
     * Desativa o usuário e sua conta de acesso.
     *
     * Os registros de compras, acessos e pagamentos permanecem preservados.
     *
     * @param id
     */
    @Transactional
    public void desativar(Long id) {

        Usuario usuario = repository.findById(id)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Usuário não encontrado"
                )
                );

        usuario.setAtivo(false);

        repository.save(usuario);

        /*
     * Também desativa a conta CLIENTE ligada
     * ao cadastro do usuário.
         */
        contaSistemaRepository
                .findByUsuarioRelacionadoId(id)
                .ifPresent(conta -> {

                    conta.setAtivo(false);

                    contaSistemaRepository.save(conta);
                });
    }

    public List<Usuario> listarBloqueados() {
        return repository.findByAtivoFalse();
    }

    public Usuario buscarPorRfid(String codigoRfid) {

        if (codigoRfid == null || codigoRfid.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Código RFID não informado"
            );
        }

        return repository
                .findByCodigoRfidIgnoreCase(codigoRfid.trim())
                .orElseThrow(()
                        -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário não encontrado"
                )
                );
    }

    /**
     * Gera automaticamente um código RFID no padrão RFID + 4 dígitos. Exemplo:
     * RFID4827.
     */
    public String gerarCodigoRfidAutomatico() {

        String codigo;

        do {
            int numero = 1000 + RANDOM.nextInt(9000);
            codigo = "RFID" + numero;

        } while (repository.existsByCodigoRfidIgnoreCase(codigo));

        return codigo;
    }

}
