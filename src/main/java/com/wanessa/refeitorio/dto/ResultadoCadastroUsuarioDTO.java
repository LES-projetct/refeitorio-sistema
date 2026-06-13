/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.dto;

import com.wanessa.refeitorio.model.Usuario;

/**
 *
 * Resultado do cadastro ou edição de um usuário.
 * @author wanes
 */
public record ResultadoCadastroUsuarioDTO (
        Usuario usuario,
        CredencialInicialDTO credencialInicial,
        boolean novoCadastro
) {
}
