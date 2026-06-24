/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.config;

import com.wanessa.refeitorio.service.AutenticacaoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;

/**
 * Configura autenticação, perfis de acesso, login, logout e tratamento de
 * acesso negado.
 *
 * @author wanes
 */
@Configuration
public class SecurityConfig {

    private final AutenticacaoService autenticacaoService;

    public SecurityConfig(
            AutenticacaoService autenticacaoService) {

        this.autenticacaoService = autenticacaoService;
    }

    /**
     * Codificador utilizado para verificar as senhas armazenadas com BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Relaciona o serviço de autenticação com o codificador de senhas.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider
                = new DaoAuthenticationProvider();

        provider.setUserDetailsService(
                autenticacaoService
        );

        provider.setPasswordEncoder(
                passwordEncoder
        );

        return provider;
    }

    /**
     * Define as regras de segurança da aplicação.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            DaoAuthenticationProvider provider)
            throws Exception {

        http
                .authenticationProvider(provider)
                /*
                 * Temporariamente desativado porque os fetchs
                 * de compras e acessos ainda não enviam token CSRF.
                 */
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize
                        -> authorize
                        /*
                 * Arquivos públicos necessários
                 * para login e páginas do sistema.
                         */
                        .requestMatchers(
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/favicon.ico"
                        ).permitAll()
                        .requestMatchers(
                                "/cadastro-cliente/**"
                        )
                        .permitAll()
                        .requestMatchers("/simulador-acesso/**")
                        .hasAnyRole("ADMINISTRADOR", "OPERADOR")
                        /*
                 * Redirecionamento após o login.
                         */
                        .requestMatchers("/inicio")
                        .authenticated()
                        /*
                 * Trocar PIN.
                         */
                        .requestMatchers("/trocar-pin/**")
                        .authenticated()
                        /*
                 * Dashboard administrativo.
                         */
                        .requestMatchers("/")
                        .hasRole("ADMINISTRADOR")
                        /*
                * Rota usada pelo caixa/operador para identificar cliente por RFID
                * durante a compra.
                         */
                        .requestMatchers(HttpMethod.GET, "/usuarios/rfid/**")
                        .hasAnyRole(
                                "ADMINISTRADOR",
                                "OPERADOR"
                        )
                        /*
                 * Rotas exclusivas do administrador.
                         */
                        .requestMatchers(
                                "/usuarios/**",
                                "/contas/**",
                                "/relatorios/**",
                                "/despesas/**"
                        )
                        .hasRole("ADMINISTRADOR")
                        /*
                 * Rotas do administrador e operador.
                         */
                        .requestMatchers(
                                "/produtos/**",
                                "/compras/**",
                                "/acessos/**",
                                "/pagamentos/**"
                        )
                        .hasAnyRole(
                                "ADMINISTRADOR",
                                "OPERADOR"
                        )
                        /*
                 * Área pessoal do cliente.
                         */
                        .requestMatchers(
                                "/minha-conta/**"
                        )
                        .hasRole("CLIENTE")
                        /*
                 * Qualquer outra rota exige login.
                         */
                        .anyRequest()
                        .authenticated()
                )
                /* Login personalizado */
                .formLogin(form
                        -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/inicio", true)
                        .failureUrl("/login?erro")
                        .permitAll()
                )
                /* Página exibida quando faltar permissão */
                .exceptionHandling(exception
                        -> exception.accessDeniedHandler(
                        (request, response, accessDeniedException) -> {

                            /*
                     * Encerra a sessão atual para evitar que
                     * o usuário continue autenticado sem permissão.
                             */
                            var sessao = request.getSession(false);

                            if (sessao != null) {
                                sessao.invalidate();
                            }

                            /*
                     * Redireciona para a tela de login
                     * com uma mensagem de acesso negado.
                             */
                            response.sendRedirect(
                                    request.getContextPath()
                                    + "/login?acessoNegado"
                            );
                        }
                )
                )
                /* Encerramento da sessão */
                .logout(logout
                        -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?sair")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

}
