package com.wanessa.refeitorio.controller;

import com.wanessa.refeitorio.dto.ResultadoCadastroUsuarioDTO;
import com.wanessa.refeitorio.model.Usuario;
import com.wanessa.refeitorio.service.CadastroUsuarioService;
import com.wanessa.refeitorio.service.UsuarioService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final CadastroUsuarioService cadastroUsuarioService;

    public UsuarioController(
            UsuarioService usuarioService,
            CadastroUsuarioService cadastroUsuarioService) {

        this.usuarioService = usuarioService;
        this.cadastroUsuarioService
                = cadastroUsuarioService;
    }

    /**
     * Lista usuários em JSON.
     * @return 
     */
    @ResponseBody
    @GetMapping
    public List<Usuario> listar() {

        return usuarioService.listarTodos();
    }

    /**
     * Cadastra usuário pela API.
     * @param usuario
     * @return 
     */
    @ResponseBody
    @PostMapping
    public ResultadoCadastroUsuarioDTO salvar(
            @RequestBody Usuario usuario) {

        return cadastroUsuarioService.salvar(usuario);
    }

    /**
     * Lista usuários inadimplentes.
     * @return 
     */
    @ResponseBody
    @GetMapping("/inadimplentes")
    public List<Usuario> listarInadimplentes() {

        return usuarioService.listarInadimplentes();
    }

    /**
     * Abre a tela de usuários.
     * @param model
     * @return 
     */
    @GetMapping("/tela")
    public String telaUsuarios(Model model) {

        model.addAttribute(
                "usuarios",
                usuarioService.listarTodos()
        );

        return "usuarios";
    }

    /**
     * Abre o formulário de cadastro.
     * @param model
     * @return 
     */
    @GetMapping("/novo")
    public String novoUsuario(Model model) {

        Usuario usuario = new Usuario();
        
        usuario.setCodigoRfid(
                usuarioService.gerarCodigoRfidAutomatico()
        );

        usuario.setAtivo(true);

        model.addAttribute(
                "usuario",
                usuario
        );

        return "usuario-form";
    }

    /**
     * Salva o formulário de usuário.
     * @param usuario
     * @param model
     * @param redirectAttributes
     * @return 
     */
    @PostMapping("/salvar")
    public String salvarUsuarioForm(
            @ModelAttribute Usuario usuario,
            Model model,
            RedirectAttributes redirectAttributes) {

        /*
     * Guarda essa informação antes da tentativa,
     * porque o Hibernate pode preencher o ID mesmo
     * que a transação seja posteriormente desfeita.
         */
        boolean novoCadastro
                = usuario.getId() == null;

        try {

            ResultadoCadastroUsuarioDTO resultado
                    = cadastroUsuarioService.salvar(usuario);

            if (resultado.novoCadastro()) {

                redirectAttributes.addFlashAttribute(
                        "sucesso",
                        "Usuário e conta de acesso criados com sucesso."
                );

                redirectAttributes.addFlashAttribute(
                        "credencialLogin",
                        resultado.credencialInicial().login()
                );

                redirectAttributes.addFlashAttribute(
                        "credencialSenha",
                        resultado.credencialInicial().senhaTemporaria()
                );

            } else {

                redirectAttributes.addFlashAttribute(
                        "sucesso",
                        "Usuário atualizado com sucesso."
                );
            }

            return "redirect:/usuarios/tela";

        } catch (IllegalArgumentException e) {

            /*
         * Se o cadastro novo foi desfeito, remove o ID
         * que permaneceu somente no objeto em memória.
             */
            if (novoCadastro) {
                usuario.setId(null);
            }

            model.addAttribute(
                    "erro",
                    e.getMessage()
            );

            model.addAttribute(
                    "usuario",
                    usuario
            );

            return "usuario-form";

        } catch (Exception e) {

            if (novoCadastro) {
                usuario.setId(null);
            }

            model.addAttribute(
                    "erro",
                    "Não foi possível salvar o usuário e criar sua conta."
            );

            model.addAttribute(
                    "usuario",
                    usuario
            );

            return "usuario-form";
        }
    }

    /**
     * Abre a edição do usuário.
     * @param id
     * @param model
     * @param redirectAttributes
     * @return 
     */
    @GetMapping("/editar/{id}")
    public String editarUsuario(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {

            Usuario usuario
                    = usuarioService.buscarPorId(id);

            model.addAttribute(
                    "usuario",
                    usuario
            );

            return "usuario-form";

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    e.getMessage()
            );

            return "redirect:/usuarios/tela";
        }
    }

    /**
     * Desativa o usuário sem apagar seu histórico.
     * @param id
     * @param redirectAttributes
     * @return 
     */
    @PostMapping("/desativar/{id}")
    public String desativarUsuario(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {

            usuarioService.desativar(id);

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Usuário e conta de acesso desativados com sucesso."
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    e.getMessage()
            );
        }

        return "redirect:/usuarios/tela";
    }

    /**
     * Lista usuários bloqueados.
     * @return 
     */
    @ResponseBody
    @GetMapping("/bloqueados")
    public List<Usuario> listarBloqueados() {

        return usuarioService.listarBloqueados();
    }

    /**
     * Busca usuário pelo código RFID.
     * @param codigoRfid
     * @return 
     */
    @ResponseBody
    @GetMapping("/rfid/{codigoRfid}")
    public Usuario buscarPorRfid(
            @PathVariable String codigoRfid) {

        return usuarioService.buscarPorRfid(
                codigoRfid
        );
    }
}
