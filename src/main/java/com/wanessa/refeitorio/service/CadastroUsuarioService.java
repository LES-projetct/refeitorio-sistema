package com.wanessa.refeitorio.service;

import com.wanessa.refeitorio.dto.CredencialInicialDTO;
import com.wanessa.refeitorio.dto.ResultadoCadastroUsuarioDTO;
import com.wanessa.refeitorio.model.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Coordena o cadastro do usuário e da conta cliente.
 */
@Service
public class CadastroUsuarioService {

    private final UsuarioService usuarioService;
    private final ContaSistemaService contaSistemaService;

    public CadastroUsuarioService(
            UsuarioService usuarioService,
            ContaSistemaService contaSistemaService) {

        this.usuarioService = usuarioService;
        this.contaSistemaService = contaSistemaService;
    }

    /**
     * Cadastra ou atualiza um usuário.
     *
     * Quando for um novo cadastro, também cria automaticamente uma conta com
     * perfil CLIENTE.
     */
    @Transactional
    public ResultadoCadastroUsuarioDTO salvar(Usuario usuario) {

        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não informado");
        }

        boolean novoCadastro = usuario.getId() == null;

        /*
     * Se for um novo cadastro e o RFID não tiver sido informado,
     * o sistema gera automaticamente.
         */
        if (novoCadastro
                && (usuario.getCodigoRfid() == null
                || usuario.getCodigoRfid().isBlank())) {

            usuario.setCodigoRfid(
                    usuarioService.gerarCodigoRfidAutomatico()
            );
        }

        if (usuario.getAtivo() == null) {
            usuario.setAtivo(true);
        }

        Usuario usuarioSalvo = usuarioService.salvar(usuario);

        CredencialInicialDTO credencial = null;

        if (novoCadastro) {
            credencial = contaSistemaService.criarContaCliente(usuarioSalvo);
        }

        return new ResultadoCadastroUsuarioDTO(
                usuarioSalvo,
                credencial,
                novoCadastro
        );
    }
}
