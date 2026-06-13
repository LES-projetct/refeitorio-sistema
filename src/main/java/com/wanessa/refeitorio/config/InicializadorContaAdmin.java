/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.config;

import com.wanessa.refeitorio.enums.PerfilAcesso;
import com.wanessa.refeitorio.model.ContaSistema;
import com.wanessa.refeitorio.repository.ContaSistemaRepository;
import com.wanessa.refeitorio.service.ContaSistemaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 *
 * @author wanes
 */
/**
 * Cria a primeira conta administrativa caso ainda não exista nenhuma conta com
 * o login configurado.
 */
@Component
public class InicializadorContaAdmin implements CommandLineRunner {

    private final ContaSistemaRepository repository;
    private final ContaSistemaService service;

    @Value("${app.admin.nome:Administrador}")
    private String nomeAdministrador;

    @Value("${app.admin.login:admin}")
    private String loginAdministrador;

    @Value("${app.admin.senha:}")
    private String senhaAdministrador;

    public InicializadorContaAdmin(
            ContaSistemaRepository repository,
            ContaSistemaService service) {

        this.repository = repository;
        this.service = service;
    }

    @Override
    public void run(String... args) {

        /*
         * Não cria outra conta se o login já existir.
         */
        if (repository.existsByLoginIgnoreCase(
                loginAdministrador)) {

            return;
        }

        /*
         * Evita criar uma conta sem senha.
         */
        if (senhaAdministrador == null
                || senhaAdministrador.isBlank()) {

            System.out.println(
                    "Conta administrativa inicial não criada. "
                    + "Defina a variável APP_ADMIN_SENHA."
            );

            return;
        }

        ContaSistema conta = new ContaSistema();

        conta.setNome(nomeAdministrador);
        conta.setLogin(loginAdministrador);
        conta.setSenha(senhaAdministrador);
        conta.setPerfil(PerfilAcesso.ADMINISTRADOR);
        conta.setAtivo(true);
        conta.setUsuarioRelacionado(null);

        /*
         * O ContaSistemaService criptografa a senha
         * antes de salvar no banco.
         */
        service.salvar(conta);

        System.out.println(
                "Conta administrativa inicial criada."
        );

        System.out.println(
                "Login administrativo: "
                + loginAdministrador
        );
    }

}
