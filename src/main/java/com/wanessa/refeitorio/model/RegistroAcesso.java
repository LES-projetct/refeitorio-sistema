/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 *
 * @author wanes
 */
@Entity
@Getter
@Setter
public class RegistroAcesso {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataHoraEntrada;

    private LocalDateTime dataHoraSaida;

    private Boolean acessoPermitido;

    private String motivoBloqueio;

    @ManyToOne
    private Usuario usuario;
}
