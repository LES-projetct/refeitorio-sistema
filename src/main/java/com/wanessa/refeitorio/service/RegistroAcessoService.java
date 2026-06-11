package com.wanessa.refeitorio.service;

import com.wanessa.refeitorio.model.RegistroAcesso;
import com.wanessa.refeitorio.model.Usuario;
import com.wanessa.refeitorio.repository.RegistroAcessoRepository;
import com.wanessa.refeitorio.repository.UsuarioRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RegistroAcessoService {

    private final RegistroAcessoRepository registroAcessoRepository;
    private final UsuarioRepository usuarioRepository;

    public RegistroAcessoService(
            RegistroAcessoRepository registroAcessoRepository,
            UsuarioRepository usuarioRepository) {

        this.registroAcessoRepository = registroAcessoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Registra uma entrada permitida ou uma tentativa bloqueada.
     */
    @Transactional
    public RegistroAcesso registrarEntradaPorRfid(
            String codigoRfid) {

        Usuario usuario = buscarUsuarioPorRfid(codigoRfid);

        RegistroAcesso registro = new RegistroAcesso();

        registro.setUsuario(usuario);
        registro.setDataHoraEntrada(LocalDateTime.now());
        registro.setDataHoraSaida(null);

        /*
         * Usuário bloqueado manualmente não pode entrar.
         */
        if (Boolean.FALSE.equals(usuario.getAtivo())) {

            return salvarAcessoBloqueado(
                    registro,
                    "Usuário bloqueado"
            );
        }

        /*
         * Usuário precisa ter saldo cadastrado.
         */
        if (usuario.getSaldo() == null) {

            return salvarAcessoBloqueado(
                    registro,
                    "Usuário sem saldo cadastrado"
            );
        }

        /*
         * Usuário precisa ter limite cadastrado.
         */
        if (usuario.getLimiteCredito() == null) {

            return salvarAcessoBloqueado(
                    registro,
                    "Usuário sem limite de crédito cadastrado"
            );
        }

        /*
         * Limite de crédito não pode ser negativo.
         */
        if (usuario.getLimiteCredito()
                .compareTo(BigDecimal.ZERO) < 0) {

            return salvarAcessoBloqueado(
                    registro,
                    "Limite de crédito inválido"
            );
        }

        BigDecimal limiteNegativo =
                usuario.getLimiteCredito().negate();

        /*
         * Saldo negativo dentro do limite ainda permite acesso.
         * O bloqueio ocorre apenas quando o limite é ultrapassado.
         */
        if (usuario.getSaldo()
                .compareTo(limiteNegativo) < 0) {

            return salvarAcessoBloqueado(
                    registro,
                    "Limite de crédito excedido"
            );
        }

        /*
         * Impede duas entradas abertas para o mesmo usuário.
         */
        boolean possuiEntradaAberta =
                registroAcessoRepository
                        .findTopByUsuarioIdAndAcessoPermitidoTrueAndDataHoraSaidaIsNullOrderByDataHoraEntradaDesc(
                                usuario.getId()
                        )
                        .isPresent();

        if (possuiEntradaAberta) {

            return salvarAcessoBloqueado(
                    registro,
                    "Usuário já possui entrada em aberto"
            );
        }

        /*
         * Entrada liberada.
         */
        registro.setAcessoPermitido(true);
        registro.setMotivoBloqueio(null);

        return registroAcessoRepository.save(registro);
    }

    /**
     * Registra a saída na última entrada permitida ainda aberta.
     */
    @Transactional
    public RegistroAcesso registrarSaidaPorRfid(
            String codigoRfid) {

        Usuario usuario = buscarUsuarioPorRfid(codigoRfid);

        RegistroAcesso registro =
                registroAcessoRepository
                        .findTopByUsuarioIdAndAcessoPermitidoTrueAndDataHoraSaidaIsNullOrderByDataHoraEntradaDesc(
                                usuario.getId()
                        )
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.CONFLICT,
                                        "Nenhuma entrada aberta foi encontrada"
                                )
                        );

        registro.setDataHoraSaida(LocalDateTime.now());

        return registroAcessoRepository.save(registro);
    }

    /**
     * Busca e valida um usuário pelo RFID.
     */
    private Usuario buscarUsuarioPorRfid(
            String codigoRfid) {

        if (codigoRfid == null || codigoRfid.isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Código RFID não informado"
            );
        }

        String codigoNormalizado =
                codigoRfid.trim();

        return usuarioRepository
                .findByCodigoRfidIgnoreCase(
                        codigoNormalizado
                )
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Cartão RFID não encontrado"
                        )
                );
    }

    /**
     * Finaliza e salva uma tentativa bloqueada.
     */
    private RegistroAcesso salvarAcessoBloqueado(
            RegistroAcesso registro,
            String motivo) {

        registro.setAcessoPermitido(false);
        registro.setMotivoBloqueio(motivo);

        return registroAcessoRepository.save(registro);
    }

    @Transactional(readOnly = true)
    public List<RegistroAcesso> listarTodos() {

        return registroAcessoRepository
                .findAllByOrderByDataHoraEntradaDesc();
    }

    @Transactional(readOnly = true)
    public List<RegistroAcesso> listarPorUsuario(
            Long usuarioId) {

        return registroAcessoRepository
                .findByUsuarioIdOrderByDataHoraEntradaDesc(
                        usuarioId
                );
    }

    @Transactional(readOnly = true)
    public List<RegistroAcesso> listarBloqueados() {

        return registroAcessoRepository
                .findByAcessoPermitidoFalseOrderByDataHoraEntradaDesc();
    }
}