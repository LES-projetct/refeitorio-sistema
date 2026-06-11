package com.wanessa.refeitorio.repository;

import com.wanessa.refeitorio.model.RegistroAcesso;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistroAcessoRepository
        extends JpaRepository<RegistroAcesso, Long> {

    /*
     * Localiza somente uma entrada permitida que ainda não
     * possui horário de saída.
     */
    Optional<RegistroAcesso>
            findTopByUsuarioIdAndAcessoPermitidoTrueAndDataHoraSaidaIsNullOrderByDataHoraEntradaDesc(
                    Long usuarioId
            );

    /*
     * Lista os registros mais recentes primeiro.
     */
    List<RegistroAcesso>
            findAllByOrderByDataHoraEntradaDesc();

    /*
     * Histórico de um usuário.
     */
    List<RegistroAcesso>
            findByUsuarioIdOrderByDataHoraEntradaDesc(
                    Long usuarioId
            );

    /*
     * Lista somente tentativas bloqueadas.
     */
    List<RegistroAcesso>
            findByAcessoPermitidoFalseOrderByDataHoraEntradaDesc();

    /*
     * Total de tentativas bloqueadas.
     */
    long countByAcessoPermitidoFalse();
}