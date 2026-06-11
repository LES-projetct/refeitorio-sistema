/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.controller;

import com.wanessa.refeitorio.model.Usuario;
import com.wanessa.refeitorio.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

/**
 *
 * @author wanes
 */
@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @ResponseBody
    @GetMapping
    public List<Usuario> listar() {
        return service.listarTodos();
    }

    @ResponseBody
    @PostMapping
    public Usuario salvar(@RequestBody Usuario usuario) {
        return service.salvar(usuario);
    }

    @ResponseBody
    @GetMapping("/inadimplentes")
    public List<Usuario> listarInadimplentes() {
        return service.listarInadimplentes();
    }

    @GetMapping("/tela")
    public String telaUsuarios(Model model) {

        model.addAttribute(
                "usuarios",
                service.listarTodos());

        return "usuarios";
    }

    @GetMapping("/novo")
    public String novoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuario-form";
    }

    @PostMapping("/salvar")
    public String salvarUsuarioForm(
            @ModelAttribute Usuario usuario,
            Model model) {

        try {
            service.salvar(usuario);

            return "redirect:/usuarios/tela";

        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("usuario", usuario);

            return "usuario-form";
        }
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(
            @PathVariable Long id,
            Model model) {

        Usuario usuario = service.buscarPorId(id);

        model.addAttribute("usuario", usuario);

        return "usuario-form";
    }

    @GetMapping("/excluir/{id}")
    public String excluirUsuario(@PathVariable Long id) {

        service.excluir(id);

        return "redirect:/usuarios/tela";
    }

    @GetMapping("/bloqueados")
    @ResponseBody
    public List<Usuario> listarBloqueados() {
        return service.listarBloqueados();
    }

    @ResponseBody
    @GetMapping("/rfid/{codigoRfid}")
    public Usuario buscarPorRfid(@PathVariable String codigoRfid) {
        return service.buscarPorRfid(codigoRfid);
    }
}
