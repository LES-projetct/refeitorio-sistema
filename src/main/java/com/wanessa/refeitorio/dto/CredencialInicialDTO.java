package com.wanessa.refeitorio.dto;

/**
 * Dados apresentados somente uma vez depois
 * que a conta do cliente é criada.
 */
public record CredencialInicialDTO(
        String login,
        String senhaTemporaria
) {
}