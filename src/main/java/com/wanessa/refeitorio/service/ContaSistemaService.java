/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.service;

import com.wanessa.refeitorio.enums.PerfilAcesso;
import com.wanessa.refeitorio.model.ContaSistema;
import com.wanessa.refeitorio.repository.ContaSistemaRepository;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wanessa.refeitorio.dto.CredencialInicialDTO;
import java.security.SecureRandom;

import com.wanessa.refeitorio.model.Usuario;
import com.wanessa.refeitorio.repository.UsuarioRepository;
import java.math.BigDecimal;

import com.wanessa.refeitorio.model.Compra;
import com.wanessa.refeitorio.repository.CompraRepository;

import com.wanessa.refeitorio.dto.MinhaContaDTO;
import com.wanessa.refeitorio.model.RegistroAcesso;
import com.wanessa.refeitorio.repository.PagamentoRepository;
import com.wanessa.refeitorio.repository.RegistroAcessoRepository;
import com.wanessa.refeitorio.model.Pagamento;

/**
 *
 * @author wanes
 */
@Service
public class ContaSistemaService {

    private final ContaSistemaRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final CompraRepository compraRepository;
    private final RegistroAcessoRepository registroAcessoRepository;
    private final PagamentoRepository pagamentoRepository;

    private static final SecureRandom RANDOM
            = new SecureRandom();

    public ContaSistemaService(
            ContaSistemaRepository repository,
            PasswordEncoder passwordEncoder,
            UsuarioRepository usuarioRepository,
            CompraRepository compraRepository,
            RegistroAcessoRepository registroAcessoRepository,
            PagamentoRepository pagamentoRepository) {

        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
        this.compraRepository = compraRepository;
        this.registroAcessoRepository = registroAcessoRepository;
        this.pagamentoRepository = pagamentoRepository;
    }

    /**
     * Lista todas as contas em ordem alfabética.
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<ContaSistema> listarTodas() {
        return repository.findAllByOrderByNomeAsc();
    }

    /**
     * Lista somente as contas ativas.
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<ContaSistema> listarAtivas() {
        return repository.findByAtivoTrueOrderByNomeAsc();
    }

    /**
     * Busca uma conta pelo identificador.
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public ContaSistema buscarPorId(Long id) {

        return repository.findById(id)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Conta do sistema não encontrada"
                )
                );
    }

    /**
     * Busca uma conta pelo login.
     *
     * @param login
     * @return
     */
    @Transactional(readOnly = true)
    public ContaSistema buscarPorLogin(String login) {

        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException(
                    "Login não informado"
            );
        }

        return repository
                .findByLoginIgnoreCase(login.trim())
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Conta do sistema não encontrada"
                )
                );
    }

    /**
     * Cadastra ou atualiza uma conta.
     *
     * @param conta
     * @return
     */
    @Transactional
    public ContaSistema salvar(ContaSistema conta) {

        validarDadosObrigatorios(conta);

        String loginNormalizado
                = conta.getLogin().trim();

        repository
                .findByLoginIgnoreCase(loginNormalizado)
                .ifPresent(contaExistente -> {

                    boolean outraConta
                            = conta.getId() == null
                            || !contaExistente.getId()
                                    .equals(conta.getId());

                    if (outraConta) {
                        throw new IllegalArgumentException(
                                "Já existe uma conta com este login"
                        );
                    }
                });
        /*
             * Contas de cliente precisam estar ligadas
             * a um usuário do refeitório.
         */
        if (conta.getPerfil()
                == PerfilAcesso.CLIENTE) {

            if (conta.getUsuarioRelacionado() == null
                    || conta.getUsuarioRelacionado().getId() == null) {

                throw new IllegalArgumentException(
                        "Selecione o usuário relacionado à conta cliente"
                );
            }

        } else {

            /*
     * Administrador e operador não precisam
     * estar vinculados a um cliente.
             */
            conta.setUsuarioRelacionado(null);
        }

        /*
         * Cadastro de uma nova conta.
         */
        if (conta.getId() == null) {

            if (conta.getSenha() == null
                    || conta.getSenha().isBlank()) {

                throw new IllegalArgumentException(
                        "A senha é obrigatória"
                );
            }

            conta.setLogin(loginNormalizado);

            conta.setSenha(
                    passwordEncoder.encode(
                            conta.getSenha()
                    )
            );

            if (conta.getAtivo() == null) {
                conta.setAtivo(true);
            }

            /*
                * Administrador e operador definem a própria senha
                * no cadastro e não precisam realizar troca inicial.
             */
            if (conta.getPerfil() != PerfilAcesso.CLIENTE) {
                conta.setDeveTrocarSenha(false);
            }

            return repository.save(conta);
        }

        /*
         * Atualização de uma conta existente.
         */
        ContaSistema existente
                = repository.findById(conta.getId())
                        .orElseThrow(()
                                -> new IllegalArgumentException(
                                "Conta do sistema não encontrada"
                        )
                        );

        existente.setNome(
                conta.getNome().trim()
        );

        existente.setLogin(
                loginNormalizado
        );

        existente.setPerfil(
                conta.getPerfil()
        );

        existente.setUsuarioRelacionado(
                conta.getUsuarioRelacionado()
        );

        if (conta.getAtivo() != null) {

            existente.setAtivo(
                    conta.getAtivo()
            );

            sincronizarStatusUsuarioRelacionado(
                    existente,
                    conta.getAtivo()
            );
        }
        /*
         * Senha vazia durante a edição mantém
         * a senha já cadastrada.
         */
        if (conta.getSenha() != null
                && !conta.getSenha().isBlank()) {

            existente.setSenha(
                    passwordEncoder.encode(
                            conta.getSenha()
                    )
            );
        }

        return repository.save(existente);
    }

    /**
     * Desativa a conta e, quando for uma conta de cliente, também desativa o
     * usuário vinculado.
     *
     * @param id
     */
    @Transactional
    public void desativar(Long id) {

        ContaSistema conta = repository.findById(id)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Conta do sistema não encontrada"
                )
                );

        conta.setAtivo(false);

        sincronizarStatusUsuarioRelacionado(
                conta,
                false
        );

        repository.save(conta);
    }

    /**
     * Reativa a conta e o usuário vinculado, desde que o saldo esteja dentro do
     * limite.
     *
     * @param id
     */
    @Transactional
    public void reativar(Long id) {

        ContaSistema conta = repository.findById(id)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Conta do sistema não encontrada"
                )
                );

        conta.setAtivo(true);

        sincronizarStatusUsuarioRelacionado(
                conta,
                true
        );

        repository.save(conta);
    }

    /**
     * Valida os campos essenciais.
     */
    private void validarDadosObrigatorios(
            ContaSistema conta) {

        if (conta == null) {
            throw new IllegalArgumentException(
                    "Conta não informada"
            );
        }

        if (conta.getNome() == null
                || conta.getNome().isBlank()) {

            throw new IllegalArgumentException(
                    "O nome é obrigatório"
            );
        }

        if (conta.getLogin() == null
                || conta.getLogin().isBlank()) {

            throw new IllegalArgumentException(
                    "O login é obrigatório"
            );
        }

        if (conta.getPerfil() == null) {
            throw new IllegalArgumentException(
                    "O perfil de acesso é obrigatório"
            );
        }
    }

    /**
     * Cria automaticamente a conta de acesso do cliente.
     *
     * @param usuario
     * @return
     */
    @Transactional
    public CredencialInicialDTO criarContaCliente(
            Usuario usuario) {

        if (usuario == null || usuario.getId() == null) {
            throw new IllegalArgumentException(
                    "O usuário precisa estar salvo antes da criação da conta"
            );
        }

        if (usuario.getCodigoRfid() == null
                || usuario.getCodigoRfid().isBlank()) {

            throw new IllegalArgumentException(
                    "O código RFID é obrigatório para criar a conta"
            );
        }

        if (repository.existsByUsuarioRelacionadoId(
                usuario.getId())) {

            throw new IllegalArgumentException(
                    "Este usuário já possui uma conta de acesso"
            );
        }

        String login = gerarLoginCliente(usuario);
        String pinTemporario = gerarPinTemporario();

        ContaSistema conta = new ContaSistema();

        conta.setNome(usuario.getNome());

        /*
     * O RFID é definido como login antes de chamar salvar().
         */
        conta.setLogin(login);

        /*
     * O método salvar() criptografará este PIN.
         */
        conta.setSenha(pinTemporario);

        conta.setPerfil(PerfilAcesso.CLIENTE);
        conta.setAtivo(true);
        conta.setDeveTrocarSenha(true);
        conta.setUsuarioRelacionado(usuario);

        ContaSistema contaSalva
                = salvar(conta);

        return new CredencialInicialDTO(
                contaSalva.getLogin(),
                pinTemporario
        );
    }

    /**
     * Gera um PIN temporário entre 1000 e 9999.
     */
    private String gerarPinTemporario() {

        int pin = 1000 + RANDOM.nextInt(9000);

        return String.valueOf(pin);
    }

    /**
     * Escolhe um caractere aleatório de um conjunto.
     */
    private Character caractereAleatorio(
            String conjunto) {

        int indice
                = RANDOM.nextInt(
                        conjunto.length()
                );

        return conjunto.charAt(indice);
    }

    /**
     * Mantém o status da conta cliente sincronizado com o status do usuário do
     * refeitório.
     */
    private void sincronizarStatusUsuarioRelacionado(
            ContaSistema conta,
            boolean ativo) {

        if (conta.getUsuarioRelacionado() == null
                || conta.getUsuarioRelacionado().getId() == null) {

            /*
         * Administrador e operador normalmente
         * não possuem usuário vinculado.
             */
            return;
        }

        Long usuarioId
                = conta.getUsuarioRelacionado().getId();

        Usuario usuario = usuarioRepository
                .findById(usuarioId)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Usuário vinculado não encontrado"
                )
                );

        /*
     * Antes de reativar, verifica se o saldo ainda
     * está dentro do limite de crédito permitido.
         */
        if (ativo) {

            if (usuario.getSaldo() == null) {
                throw new IllegalArgumentException(
                        "O usuário não possui saldo cadastrado"
                );
            }

            if (usuario.getLimiteCredito() == null) {
                throw new IllegalArgumentException(
                        "O usuário não possui limite de crédito cadastrado"
                );
            }

            BigDecimal limiteNegativo
                    = usuario.getLimiteCredito().negate();

            if (usuario.getSaldo()
                    .compareTo(limiteNegativo) < 0) {

                throw new IllegalArgumentException(
                        "O usuário não pode ser reativado porque ultrapassou o limite de crédito"
                );
            }
        }

        usuario.setAtivo(ativo);

        usuarioRepository.save(usuario);
    }

    /**
     * Utiliza o RFID como login do cliente.
     */
    private String gerarLoginCliente(
            Usuario usuario) {

        String codigoRfid
                = usuario.getCodigoRfid();

        if (codigoRfid == null
                || codigoRfid.isBlank()) {

            throw new IllegalArgumentException(
                    "O código RFID é obrigatório para criar a conta"
            );
        }

        String login
                = codigoRfid.trim().toUpperCase();

        if (repository.existsByLoginIgnoreCase(login)) {
            throw new IllegalArgumentException(
                    "Já existe uma conta utilizando este RFID"
            );
        }

        return login;
    }

    /**
     * Altera o PIN temporário no primeiro acesso.
     *
     * @param login
     * @param novoPin
     * @param confirmacaoPin
     */
    @Transactional
    public void alterarPinPrimeiroAcesso(
            String login,
            String novoPin,
            String confirmacaoPin) {

        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException(
                    "Conta autenticada não encontrada"
            );
        }

        if (novoPin == null || !novoPin.matches("\\d{4}")) {
            throw new IllegalArgumentException(
                    "O novo PIN deve possuir exatamente 4 números"
            );
        }

        if (!novoPin.equals(confirmacaoPin)) {
            throw new IllegalArgumentException(
                    "A confirmação do PIN não corresponde"
            );
        }

        ContaSistema conta = repository
                .findByLoginIgnoreCase(login.trim())
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Conta do sistema não encontrada"
                )
                );

        if (!Boolean.TRUE.equals(conta.getDeveTrocarSenha())) {
            throw new IllegalArgumentException(
                    "Esta conta não possui troca de PIN pendente"
            );
        }

        /*
     * O novo PIN precisa ser diferente do PIN temporário.
         */
        if (passwordEncoder.matches(
                novoPin,
                conta.getSenha())) {

            throw new IllegalArgumentException(
                    "Escolha um PIN diferente do PIN temporário"
            );
        }

        conta.setSenha(
                passwordEncoder.encode(novoPin)
        );

        conta.setDeveTrocarSenha(false);

        repository.save(conta);
    }

    /**
     * Retorna os dados pessoais e financeiros da conta cliente autenticada.
     *
     * @param login
     * @return
     */
    @Transactional(readOnly = true)
    public MinhaContaDTO consultarMinhaConta(String login) {

        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException(
                    "Conta autenticada não encontrada"
            );
        }

        ContaSistema conta = repository
                .findByLoginIgnoreCase(login.trim())
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Conta do sistema não encontrada"
                )
                );

        if (conta.getPerfil() != PerfilAcesso.CLIENTE) {
            throw new IllegalArgumentException(
                    "Esta conta não pertence a um cliente"
            );
        }

        Usuario usuario = conta.getUsuarioRelacionado();

        if (usuario == null) {
            throw new IllegalArgumentException(
                    "A conta não possui usuário vinculado"
            );
        }

        BigDecimal saldo = usuario.getSaldo() != null
                ? usuario.getSaldo()
                : BigDecimal.ZERO;

        BigDecimal limiteCredito
                = usuario.getLimiteCredito() != null
                ? usuario.getLimiteCredito()
                : BigDecimal.ZERO;

        /*
     * Quando o saldo está negativo, essa parte
     * representa o crédito já utilizado.
         */
        BigDecimal creditoUtilizado
                = saldo.compareTo(BigDecimal.ZERO) < 0
                ? saldo.abs()
                : BigDecimal.ZERO;

        BigDecimal creditoDisponivel
                = limiteCredito.subtract(creditoUtilizado);

        if (creditoDisponivel.compareTo(BigDecimal.ZERO) < 0) {
            creditoDisponivel = BigDecimal.ZERO;
        }

        /*
     * Saldo positivo mais crédito ainda disponível.
         */
        BigDecimal totalDisponivel
                = saldo.max(BigDecimal.ZERO)
                        .add(creditoDisponivel);

        String situacao;

        if (Boolean.FALSE.equals(usuario.getAtivo())) {

            situacao = "BLOQUEADO";

        } else if (saldo.compareTo(BigDecimal.ZERO) < 0) {

            situacao = "SALDO DEVEDOR";

        } else {

            situacao = "REGULAR";
        }

        return new MinhaContaDTO(
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCodigoRfid(),
                saldo,
                limiteCredito,
                creditoDisponivel,
                totalDisponivel,
                situacao
        );
    }

    /**
     * Retorna somente as compras pertencentes ao cliente atualmente
     * autenticado.
     *
     * @param login
     * @return
     */
    @Transactional(readOnly = true)
    public List<Compra> listarMinhasCompras(String login) {

        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException(
                    "Conta autenticada não encontrada"
            );
        }

        ContaSistema conta = repository
                .findByLoginIgnoreCase(login.trim())
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Conta do sistema não encontrada"
                )
                );

        if (conta.getPerfil() != PerfilAcesso.CLIENTE) {
            throw new IllegalArgumentException(
                    "Esta conta não pertence a um cliente"
            );
        }

        Usuario usuario = conta.getUsuarioRelacionado();

        if (usuario == null || usuario.getId() == null) {
            throw new IllegalArgumentException(
                    "A conta não possui usuário vinculado"
            );
        }

        return compraRepository
                .findByUsuarioIdOrderByDataHoraDesc(
                        usuario.getId()
                );
    }

    /**
     * Retorna somente os acessos pertencentes ao cliente atualmente
     * autenticado.
     *
     * @param login
     * @return
     */
    @Transactional(readOnly = true)
    public List<RegistroAcesso> listarMeusAcessos(String login) {

        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException(
                    "Conta autenticada não encontrada"
            );
        }

        ContaSistema conta = repository
                .findByLoginIgnoreCase(login.trim())
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Conta do sistema não encontrada"
                )
                );

        if (conta.getPerfil() != PerfilAcesso.CLIENTE) {
            throw new IllegalArgumentException(
                    "Esta conta não pertence a um cliente"
            );
        }

        Usuario usuario = conta.getUsuarioRelacionado();

        if (usuario == null || usuario.getId() == null) {
            throw new IllegalArgumentException(
                    "A conta não possui usuário vinculado"
            );
        }

        return registroAcessoRepository
                .findByUsuarioIdOrderByDataHoraEntradaDesc(
                        usuario.getId()
                );
    }

    /**
     * Busca os detalhes de uma compra específica, mas somente se ela pertencer
     * ao cliente autenticado.
     *
     * @param login
     * @param compraId
     * @return
     */
    @Transactional(readOnly = true)
    public Compra buscarMinhaCompraPorId(
            String login,
            Long compraId) {

        ContaSistema conta
                = repository.findByLoginIgnoreCase(login)
                        .orElseThrow(()
                                -> new IllegalArgumentException("Conta não encontrada.")
                        );

        if (conta.getUsuarioRelacionado() == null) {
            throw new IllegalArgumentException(
                    "Esta conta não possui cliente vinculado."
            );
        }

        Long usuarioId
                = conta.getUsuarioRelacionado().getId();

        return compraRepository.buscarDetalhesDoCliente(
                compraId,
                usuarioId
        )
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Compra não encontrada para este cliente."
                )
                );
    }

    /**
     * Permite que o cliente altere o próprio PIN dentro da área Minha Conta.
     *
     * @param login
     * @param pinAtual
     * @param novoPin
     * @param confirmacaoPin
     */
    @Transactional
    public void alterarPinCliente(
            String login,
            String pinAtual,
            String novoPin,
            String confirmacaoPin) {

        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException(
                    "Conta autenticada não encontrada"
            );
        }

        if (pinAtual == null || pinAtual.isBlank()) {
            throw new IllegalArgumentException(
                    "Informe o PIN atual"
            );
        }

        if (novoPin == null || !novoPin.matches("\\d{4}")) {
            throw new IllegalArgumentException(
                    "O novo PIN deve possuir exatamente 4 números"
            );
        }

        if (!novoPin.equals(confirmacaoPin)) {
            throw new IllegalArgumentException(
                    "A confirmação do PIN não corresponde"
            );
        }

        ContaSistema conta = repository
                .findByLoginIgnoreCase(login.trim())
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Conta do sistema não encontrada"
                )
                );

        if (conta.getPerfil() != PerfilAcesso.CLIENTE) {
            throw new IllegalArgumentException(
                    "Esta operação é permitida somente para clientes"
            );
        }

        if (!Boolean.TRUE.equals(conta.getAtivo())) {
            throw new IllegalArgumentException(
                    "Conta inativa"
            );
        }

        if (!passwordEncoder.matches(
                pinAtual,
                conta.getSenha())) {

            throw new IllegalArgumentException(
                    "PIN atual incorreto"
            );
        }

        if (passwordEncoder.matches(
                novoPin,
                conta.getSenha())) {

            throw new IllegalArgumentException(
                    "O novo PIN deve ser diferente do PIN atual"
            );
        }

        conta.setSenha(
                passwordEncoder.encode(novoPin)
        );

        conta.setDeveTrocarSenha(false);

        repository.save(conta);
    }

    /**
     * Retorna somente os pagamentos pertencentes ao cliente atualmente
     * autenticado.
     *
     * @param login
     * @return
     */
    @Transactional(readOnly = true)
    public List<Pagamento> listarMeusPagamentos(String login) {

        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException(
                    "Conta autenticada não encontrada"
            );
        }

        ContaSistema conta = repository
                .findByLoginIgnoreCase(login.trim())
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Conta do sistema não encontrada"
                )
                );

        if (conta.getPerfil() != PerfilAcesso.CLIENTE) {
            throw new IllegalArgumentException(
                    "Esta conta não pertence a um cliente"
            );
        }

        Usuario usuario = conta.getUsuarioRelacionado();

        if (usuario == null || usuario.getId() == null) {
            throw new IllegalArgumentException(
                    "A conta não possui usuário vinculado"
            );
        }

        return pagamentoRepository
                .findByUsuarioIdOrderByDataHoraDesc(
                        usuario.getId()
                );
    }

    @Transactional(readOnly = true)
    public Usuario buscarUsuarioClientePorLogin(String login) {

        ContaSistema conta = repository.findByLoginIgnoreCase(login)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada."));

        Usuario usuario = conta.getUsuarioRelacionado();

        if (usuario == null) {
            throw new RuntimeException("Conta não está vinculada a um cliente.");
        }

        return usuario;
    }
}
