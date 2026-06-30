package com.wanessa.refeitorio.controller;

import com.wanessa.refeitorio.model.Compra;
import com.wanessa.refeitorio.model.Usuario;
import com.wanessa.refeitorio.service.CompraService;
import com.wanessa.refeitorio.service.UsuarioService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RelatorioController {

    private final UsuarioService usuarioService;
    private final CompraService compraService;

    public RelatorioController(
            UsuarioService usuarioService,
            CompraService compraService) {

        this.usuarioService = usuarioService;
        this.compraService = compraService;
    }

    /**
     * Tela principal de relatórios.
     */
    @GetMapping("/relatorios")
    public String telaRelatorios(Model model) {

        LocalDate hoje = LocalDate.now();

        LocalDateTime inicioDia
                = hoje.atStartOfDay();

        LocalDateTime fimDia
                = hoje.atTime(23, 59, 59);

        BigDecimal faturamentoDia
                = compraService.calcularFaturamentoPorPeriodo(
                        inicioDia,
                        fimDia
                );

        long quantidadeComprasDia
                = compraService.contarComprasPorPeriodo(
                        inicioDia,
                        fimDia
                );

        BigDecimal ticketMedioDia
                = compraService.calcularTicketMedioPorPeriodo(
                        inicioDia,
                        fimDia
                );

        model.addAttribute("faturamentoDia", faturamentoDia);
        model.addAttribute("quantidadeComprasDia", quantidadeComprasDia);
        model.addAttribute("ticketMedioDia", ticketMedioDia);

        return "relatorios";
    }

    /**
     * Relatório de compras por período.
     */
    @GetMapping("/relatorios/compras")
    public String relatorioComprasPorPeriodo(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            Model model) {

        if (dataInicio == null) {
            dataInicio = LocalDate.now();
        }

        if (dataFim == null) {
            dataFim = LocalDate.now();
        }

        LocalDateTime inicio
                = dataInicio.atStartOfDay();

        LocalDateTime fim
                = dataFim.atTime(23, 59, 59);

        List<Compra> compras
                = compraService.listarComprasPorPeriodo(inicio, fim);

        BigDecimal faturamento
                = compraService.calcularFaturamentoPorPeriodo(inicio, fim);

        long quantidade
                = compraService.contarComprasPorPeriodo(inicio, fim);

        BigDecimal ticketMedio
                = compraService.calcularTicketMedioPorPeriodo(inicio, fim);

        model.addAttribute("compras", compras);
        model.addAttribute("dataInicio", dataInicio);
        model.addAttribute("dataFim", dataFim);
        model.addAttribute("faturamento", faturamento);
        model.addAttribute("quantidade", quantidade);
        model.addAttribute("ticketMedio", ticketMedio);

        return "relatorio-compras";
    }

    /**
     * Relatório de clientes devedores há mais de 30 dias.
     */
    @GetMapping("/relatorios/devedores")
    public String relatorioDevedores(Model model) {

        List<Usuario> devedores
                = usuarioService.listarDevedoresMaisDe30Dias();

        model.addAttribute("devedores", devedores);

        return "relatorio-devedores";
    }
}
