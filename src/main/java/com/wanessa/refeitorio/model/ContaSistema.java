/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.model;

import com.wanessa.refeitorio.enums.PerfilAcesso;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 *
 * @author wanes
 */
@Entity
@Table(name = "conta_sistema")
@Getter
@Setter
public class ContaSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome da pessoa responsável pela conta.
     */
    @Column(nullable = false)
    private String nome;

    /**
     * Nome usado para entrar no sistema.
     */
    @Column(
            nullable = false,
            unique = true,
            length = 100
    )
    private String login;

    /**
     * A senha será armazenada criptografada. Nunca deve ser salva em texto
     * comum.
     */
    @Column(nullable = false)
    private String senha;

    /**
     * Define as permissões da conta.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PerfilAcesso perfil;

    /**
     * Permite desativar a conta sem excluí-la.
     */
    @Column(nullable = false)
    private Boolean ativo = true;

    /**
     * Preenchido apenas quando a conta pertence a um cliente cadastrado no
     * refeitório.
     *
     * Administradores e operadores podem deixar esse campo vazio.
     */
    @OneToOne
    @JoinColumn(
            name = "usuario_id",
            unique = true
    )
    private Usuario usuarioRelacionado;

    /**
     * Indica que o cliente ainda precisa substituir a senha temporária gerada
     * no cadastro.
     */
    @Column(nullable = false)
    private Boolean deveTrocarSenha = true;

    /**
     * Data em que a conta foi criada.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    /**
     * Preenche valores padrão antes do primeiro salvamento.
     */
    @PrePersist
    public void prepararCadastro() {

        if (ativo == null) {
            ativo = true;
        }

        if (deveTrocarSenha == null) {
            deveTrocarSenha = true;
        }

        if (dataCriacao == null) {
            dataCriacao = LocalDateTime.now();
        }
    }
}
