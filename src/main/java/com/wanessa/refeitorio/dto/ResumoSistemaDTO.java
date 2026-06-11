/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.dto;

import java.math.BigDecimal;

/**
 *
 * @author wanes
 */
public class ResumoSistemaDTO {

    private long totalUsuarios;
    private long totalCompras;
    private BigDecimal faturamentoTotal;
    private long acessosBloqueados;

    public long getTotalUsuarios() {
        return totalUsuarios;
    }

    public void setTotalUsuarios(long totalUsuarios) {
        this.totalUsuarios = totalUsuarios;
    }

    public long getTotalCompras() {
        return totalCompras;
    }

    public void setTotalCompras(long totalCompras) {
        this.totalCompras = totalCompras;
    }

    public BigDecimal getFaturamentoTotal() {
        return faturamentoTotal;
    }

    public void setFaturamentoTotal(BigDecimal faturamentoTotal) {
        this.faturamentoTotal = faturamentoTotal;
    }

    public long getAcessosBloqueados() {
        return acessosBloqueados;
    }

    public void setAcessosBloqueados(long acessosBloqueados) {
        this.acessosBloqueados = acessosBloqueados;
    }
}
