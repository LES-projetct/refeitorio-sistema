/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.service;

import com.wanessa.refeitorio.model.ContaSistema;
import com.wanessa.refeitorio.repository.ContaSistemaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author wanes
 */
/**
 * Serviço usado pelo Spring Security para autenticar as contas cadastradas no
 * banco de dados.
 */
@Service
public class AutenticacaoService implements UserDetailsService {

    private final ContaSistemaRepository repository;

    public AutenticacaoService(
            ContaSistemaRepository repository) {

        this.repository = repository;
    }

    /**
     * Procura a conta pelo login digitado na tela de autenticação.
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(
            String login) throws UsernameNotFoundException {

        if (login == null || login.isBlank()) {
            throw new UsernameNotFoundException(
                    "Login não informado"
            );
        }

        ContaSistema conta = repository
                .findByLoginIgnoreCase(login.trim())
                .orElseThrow(()
                        -> new UsernameNotFoundException(
                        "Login ou senha inválidos"
                )
                );

        /*
         * O prefixo ROLE_ é utilizado pelo Spring Security
         * para reconhecer os perfis nas regras de acesso.
         */
        String permissao
                = "ROLE_" + conta.getPerfil().name();

        return User
                .withUsername(conta.getLogin())
                .password(conta.getSenha())
                .authorities(permissao)
                /*
                 * Conta inativa não poderá efetuar login.
                 */
                .disabled(
                        !Boolean.TRUE.equals(
                                conta.getAtivo()
                        )
                )
                .build();
    }

}
