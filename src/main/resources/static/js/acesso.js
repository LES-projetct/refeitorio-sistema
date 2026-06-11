document.addEventListener("DOMContentLoaded", function () {

    const menuAcessos =
            document.getElementById("menu-acessos");

    const rfidAcesso =
            document.getElementById("rfidAcesso");

    const btnEntrada =
            document.getElementById("btnEntrada");

    const btnSaida =
            document.getElementById("btnSaida");

    const mensagemAcesso =
            document.getElementById("mensagemAcesso");


    /* Marca a aba Acessos como ativa */
    if (menuAcessos) {
        menuAcessos.classList.add("ativo");
    }


    /**
     * Exibe uma mensagem dentro da página.
     */
    function mostrarMensagem(mensagem, tipo) {

        mensagemAcesso.textContent = mensagem;
        mensagemAcesso.hidden = false;

        mensagemAcesso.classList.remove(
                "mensagem-acesso-sucesso",
                "mensagem-acesso-erro",
                "mensagem-acesso-aviso"
                );

        mensagemAcesso.classList.add(
                `mensagem-acesso-${tipo}`
                );
    }


    /**
     * Esconde a mensagem atual.
     */
    function limparMensagem() {

        mensagemAcesso.textContent = "";
        mensagemAcesso.hidden = true;

        mensagemAcesso.classList.remove(
                "mensagem-acesso-sucesso",
                "mensagem-acesso-erro",
                "mensagem-acesso-aviso"
                );
    }


    /**
     * Bloqueia ou libera os botões durante a requisição.
     */
    function definirCarregamento(carregando) {

        btnEntrada.disabled = carregando;
        btnSaida.disabled = carregando;
        rfidAcesso.disabled = carregando;

        btnEntrada.textContent = carregando
                ? "PROCESSANDO..."
                : "REGISTRAR ENTRADA";

        btnSaida.textContent = carregando
                ? "PROCESSANDO..."
                : "REGISTRAR SAÍDA";
    }


    /**
     * Registra entrada ou saída pelo RFID.
     */
    async function registrarAcesso(tipo) {

        limparMensagem();

        const rfid = rfidAcesso.value.trim();

        if (!rfid) {

            mostrarMensagem(
                    "Informe ou aproxime o código RFID.",
                    "erro"
                    );

            rfidAcesso.classList.add("campo-invalido");
            rfidAcesso.focus();

            return;
        }

        rfidAcesso.classList.remove("campo-invalido");

        definirCarregamento(true);

        try {

            const resposta = await fetch(
                    `/acessos/${tipo}/${encodeURIComponent(rfid)}`,
                    {
                        method: "POST"
                    }
            );

            let dados;

            try {
                dados = await resposta.json();
            } catch (erroJson) {
                dados = {};
            }

            if (!resposta.ok) {

                mostrarMensagem(
                        dados.mensagem
                        || "Não foi possível registrar o acesso.",
                        "erro"
                        );

                return;
            }

            if (tipo === "entrada"
                    && dados.permitido === false) {

                mostrarMensagem(
                        dados.mensagem
                        || "Acesso bloqueado.",
                        "erro"
                        );

            } else {

                mostrarMensagem(
                        dados.mensagem
                        || "Registro realizado com sucesso.",
                        "sucesso"
                        );
            }

            rfidAcesso.value = "";

            /*
             * Atualiza a tabela após mostrar a mensagem.
             */
            setTimeout(function () {
                window.location.reload();
            }, 1200);

        } catch (erro) {

            console.error(
                    "Erro ao registrar acesso:",
                    erro
                    );

            mostrarMensagem(
                    "Não foi possível comunicar com o servidor.",
                    "erro"
                    );

        } finally {

            definirCarregamento(false);
        }
    }


    btnEntrada.addEventListener("click", function () {
        registrarAcesso("entrada");
    });

    btnSaida.addEventListener("click", function () {
        registrarAcesso("saida");
    });


    /*
     * Enter registra uma entrada.
     */
    rfidAcesso.addEventListener("keydown", function (event) {

        if (event.key === "Enter") {

            event.preventDefault();

            registrarAcesso("entrada");
        }
    });


    /*
     * Remove o erro quando o usuário começa a digitar.
     */
    rfidAcesso.addEventListener("input", function () {

        rfidAcesso.classList.remove("campo-invalido");

        if (mensagemAcesso.classList.contains(
                "mensagem-acesso-erro")) {

            limparMensagem();
        }
    });
});
