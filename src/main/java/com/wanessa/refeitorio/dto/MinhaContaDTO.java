package com.wanessa.refeitorio.dto;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.math.BigDecimal;

/**
 * Dados pessoais e financeiros exibidos para o cliente autenticado.
 */
public record MinhaContaDTO(
        String nome,
        String email,
        String codigoRfid,
        BigDecimal saldo,
        BigDecimal limiteCredito,
        BigDecimal creditoDisponivel,
        BigDecimal totalDisponivel,
        String situacao) {

}
