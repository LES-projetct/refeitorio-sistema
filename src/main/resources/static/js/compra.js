document.addEventListener("DOMContentLoaded", function () {

    /* =========================================================
     1. ELEMENTOS PRINCIPAIS DA PÁGINA
     ========================================================= */

    const menuCompras = document.getElementById("menu-nova-compra");
    const form = document.querySelector(".form-pdv");

    const pdvProduto = document.querySelector(".pdv-produto");

    const produto = document.getElementById("produto");
    const quantidade = document.getElementById("quantidade");
    const labelQuantidade = document.getElementById("labelQuantidade");
    const btnAdicionar = document.getElementById("btnAdicionar");

    const areaAcaoProduto = document.getElementById("areaAcaoProduto");
    const campoQuantidadePdv = document.getElementById("campoQuantidadePdv");
    const avisoBalancaPdv = document.getElementById("avisoBalancaPdv");

    const tabelaItens = document.querySelector("#tabelaItens tbody");
    const itensHidden = document.getElementById("itensHidden");

    const valorTotal = document.getElementById("valorTotal");
    const valorResumo = document.getElementById("valorResumo");
    const quantidadeItens = document.getElementById("quantidadeItens");
    const erroProduto = document.getElementById("erroProduto");
    const erroCompra = document.getElementById("erroCompra");


    /* =========================================================
     2. ELEMENTOS DO CLIENTE
     ========================================================= */

    const usuarioSelecionado =
            document.getElementById("usuarioSelecionado");

    const nomeCliente =
            document.getElementById("nomeCliente");

    const saldoCliente =
            document.getElementById("saldoCliente");

    const limiteCliente =
            document.getElementById("limiteCliente");

    const saldoRestante =
            document.getElementById("saldoRestante");

    const avisoSaldo =
            document.getElementById("avisoSaldo");


    /* =========================================================
     3. ELEMENTOS DO MODAL RFID
     ========================================================= */

    const modalCliente =
            document.getElementById("modalCliente");

    const abrirModalCliente =
            document.getElementById("abrirModalCliente");

    const fecharModalCliente =
            document.getElementById("fecharModalCliente");

    const buscarCliente =
            document.getElementById("buscarCliente");

    const rfidInput =
            document.getElementById("rfidInput");

    const erroCliente =
            document.getElementById("erroCliente");

    /* =========================================================
     3.1 ELEMENTOS DO MODAL DA BALANÇA
     ========================================================= */

    const modalBalanca =
            document.getElementById("modalBalanca");

    const produtoBalanca =
            document.getElementById("produtoBalanca");

    const precoBalanca =
            document.getElementById("precoBalanca");

    const pesoBalancaInput =
            document.getElementById("pesoBalancaInput");

    const totalBalanca =
            document.getElementById("totalBalanca");

    const erroBalanca =
            document.getElementById("erroBalanca");

    const confirmarPesoBalanca =
            document.getElementById("confirmarPesoBalanca");

    const cancelarBalanca =
            document.getElementById("cancelarBalanca");

    /* =========================================================
     3.2 ELEMENTOS DO MODAL DE PRODUTOS
     ========================================================= */

    const modalProduto =
            document.getElementById("modalProduto");

    const abrirModalProduto =
            document.getElementById("abrirModalProduto");

    const fecharModalProduto =
            document.getElementById("fecharModalProduto");

    const pesquisaProdutoModal =
            document.getElementById("pesquisaProdutoModal");

    const tabelaProdutosModal =
            document.getElementById("tabelaProdutosModal");

    const produtoSelecionadoTexto =
            document.getElementById("produtoSelecionadoTexto");

    const produtoSelecionadoDetalhes =
            document.getElementById("produtoSelecionadoDetalhes");
    /* =========================================================
     4. DADOS DO CLIENTE IDENTIFICADO
     ========================================================= */

    let saldoAtualCliente = 0;
    let limiteCreditoCliente = 0;
    let adicaoLiberadaPelaBalanca = false;

    /* =========================================================
     5. CONFIGURAÇÃO INICIAL
     ========================================================= */

    if (menuCompras) {
        menuCompras.classList.add("ativo");
    }


    /* =========================================================
     6. FUNÇÕES AUXILIARES
     ========================================================= */

    /**
     * Retorna a opção atualmente selecionada no campo produto.
     */
    function obterOpcaoSelecionada() {
        return produto.options[produto.selectedIndex];
    }

    /**
     * Verifica se o produto selecionado é vendido por peso.
     */
    function produtoVendidoPorPeso() {

        const opcao = obterOpcaoSelecionada();

        if (!opcao) {
            return false;
        }

        return opcao.getAttribute("data-peso") === "true";
    }

    /**
     * Obtém o preço de venda do produto selecionado.
     */
    function obterPrecoProduto() {

        const opcao = obterOpcaoSelecionada();

        if (!opcao) {
            return 0;
        }

        const precoTexto = opcao.getAttribute("data-preco");

        return Number(precoTexto) || 0;
    }

    /**
     * Formata um número como moeda brasileira.
     */
    function formatarDinheiro(valor) {

        const numero = Number(valor) || 0;

        return numero.toLocaleString("pt-BR", {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        });
    }

    /* =========================================================
     6.1 FUNÇÕES DO SIMULADOR DE BALANÇA
     ========================================================= */

    function obterPesoDigitadoBalanca() {

        if (!pesoBalancaInput) {
            return 0;
        }

        const textoPeso =
                pesoBalancaInput.value
                .trim()
                .replace(",", ".");

        return Number(textoPeso) || 0;
    }

    function limparErroBalanca() {

        if (!erroBalanca || !pesoBalancaInput) {
            return;
        }

        erroBalanca.textContent = "";
        erroBalanca.hidden = true;

        pesoBalancaInput.classList.remove("campo-invalido");
    }

    function mostrarErroBalanca(mensagem) {

        if (!erroBalanca || !pesoBalancaInput) {
            return;
        }

        erroBalanca.textContent = mensagem;
        erroBalanca.hidden = false;

        pesoBalancaInput.classList.add("campo-invalido");
        pesoBalancaInput.focus();
    }

    function atualizarPreviaBalanca() {

        if (!totalBalanca) {
            return;
        }

        const peso = obterPesoDigitadoBalanca();
        const preco = obterPrecoProduto();

        const total = peso * preco;

        totalBalanca.textContent =
                "R$ " + formatarDinheiro(total);
    }

    function abrirModalBalanca() {

        if (!modalBalanca) {
            return;
        }

        const opcaoSelecionada = obterOpcaoSelecionada();

        if (!opcaoSelecionada || !produto.value) {

            mostrarErroProduto(
                    "Selecione um produto antes de acionar a balança."
                    );

            return;
        }

        limparErroBalanca();

        const nomeProduto =
                opcaoSelecionada.textContent.trim();

        const preco =
                obterPrecoProduto();

        produtoBalanca.textContent =
                nomeProduto;

        precoBalanca.textContent =
                "R$ " + formatarDinheiro(preco) + " / kg";

        pesoBalancaInput.value = "";
        totalBalanca.textContent = "R$ 0,00";

        modalBalanca.style.display = "flex";

        setTimeout(function () {
            pesoBalancaInput.focus();
        }, 100);
    }

    function fecharModalBalanca() {

        if (!modalBalanca) {
            return;
        }

        limparErroBalanca();

        modalBalanca.style.display = "none";
    }

    function confirmarPesoDaBalanca() {

        const peso = obterPesoDigitadoBalanca();

        if (isNaN(peso) || peso <= 0) {

            mostrarErroBalanca(
                    "Informe um peso válido. Exemplo: 0,500 kg."
                    );

            return;
        }

        /*
         * A balança simulada envia o peso para o campo já usado
         * pela lógica atual da compra.
         */
        quantidade.value =
                peso.toFixed(3).replace(".", ",");

        fecharModalBalanca();

        adicaoLiberadaPelaBalanca = true;

        adicionarItem();

        adicaoLiberadaPelaBalanca = false;
    }


    /**
     * Mostra uma mensagem geral na tela de compra.
     */
    function mostrarErroCompra(mensagem) {

        erroCompra.textContent = mensagem;
        erroCompra.hidden = false;

        erroCompra.scrollIntoView({
            behavior: "smooth",
            block: "center"
        });
    }

    /**
     * Remove a mensagem geral da tela.
     */
    function limparErroCompra() {

        erroCompra.textContent = "";
        erroCompra.hidden = true;
    }

    /* =========================================================
     6.2 FUNÇÕES DO MODAL DE PRODUTOS
     ========================================================= */

    function normalizarTexto(texto) {

        return String(texto || "")
                .toLowerCase()
                .normalize("NFD")
                .replace(/[\u0300-\u036f]/g, "");
    }

    function abrirModalSelecaoProduto() {

        if (!modalProduto) {
            return;
        }

        limparErroProduto();

        modalProduto.style.display = "flex";

        if (pesquisaProdutoModal) {
            pesquisaProdutoModal.value = "";
            filtrarProdutosModal();

            setTimeout(function () {
                pesquisaProdutoModal.focus();
            }, 100);
        }
    }

    function fecharModalSelecaoProduto() {

        if (!modalProduto) {
            return;
        }

        modalProduto.style.display = "none";
    }

    function atualizarProdutoSelecionadoVisual() {

        if (!produtoSelecionadoTexto || !produtoSelecionadoDetalhes) {
            return;
        }

        if (!produto.value) {

            produtoSelecionadoTexto.textContent =
                    "Nenhum produto selecionado";

            produtoSelecionadoDetalhes.textContent =
                    "Clique no botão abaixo para escolher.";

            return;
        }

        const opcaoSelecionada =
                obterOpcaoSelecionada();

        if (!opcaoSelecionada) {
            return;
        }

        const nome =
                opcaoSelecionada.textContent.trim();

        const codigo =
                opcaoSelecionada.getAttribute("data-codigo-barras") || "-";

        const preco =
                obterPrecoProduto();

        const tipo =
                produtoVendidoPorPeso()
                ? "Vendido por peso"
                : "Vendido por unidade";

        produtoSelecionadoTexto.textContent =
                nome;

        produtoSelecionadoDetalhes.textContent =
                "Código: " + codigo
                + " | R$ " + formatarDinheiro(preco)
                + " | " + tipo;
    }

    function selecionarProdutoPeloModal(linha) {

        if (!linha) {
            return;
        }

        const produtoId =
                linha.getAttribute("data-produto-id");

        if (!produtoId) {
            return;
        }

        produto.value = produtoId;

        produto.dispatchEvent(
                new Event("change")
                );

        atualizarProdutoSelecionadoVisual();
        fecharModalSelecaoProduto();
    }

    function filtrarProdutosModal() {

        if (!pesquisaProdutoModal || !tabelaProdutosModal) {
            return;
        }

        const termo =
                normalizarTexto(
                        pesquisaProdutoModal.value.trim()
                        );

        const linhas =
                tabelaProdutosModal.querySelectorAll("tr");

        linhas.forEach(function (linha) {

            const textoLinha =
                    normalizarTexto(linha.textContent);

            const codigo =
                    normalizarTexto(
                            linha.getAttribute("data-produto-codigo")
                            );

            const nome =
                    normalizarTexto(
                            linha.getAttribute("data-produto-nome")
                            );

            if (textoLinha.includes(termo)
                    || codigo.includes(termo)
                    || nome.includes(termo)) {

                linha.style.display = "";

            } else {

                linha.style.display = "none";
            }
        });
    }


    /* =========================================================
     7. CONFIGURAÇÃO DO CAMPO QUANTIDADE/PESO
     ========================================================= */

    function configurarCampoQuantidade() {

        const opcao = obterOpcaoSelecionada();

        /*
         * Nenhum produto selecionado:
         * esconde toda a área de ação.
         */
        if (!opcao || !produto.value) {

            labelQuantidade.textContent = "Quantidade";

            quantidade.value = "1";
            quantidade.min = "1";
            quantidade.step = "1";

            if (pdvProduto) {
                pdvProduto.classList.remove("com-produto");
            }

            if (areaAcaoProduto) {
                areaAcaoProduto.hidden = true;
            }

            if (campoQuantidadePdv) {
                campoQuantidadePdv.hidden = true;
            }

            if (avisoBalancaPdv) {
                avisoBalancaPdv.hidden = true;
            }

            if (btnAdicionar) {
                btnAdicionar.disabled = true;
                btnAdicionar.textContent = "ADICIONAR ITEM";
            }

            return;
        }

        const vendidoPorPeso = produtoVendidoPorPeso();

        if (pdvProduto) {
            pdvProduto.classList.add("com-produto");
        }

        if (areaAcaoProduto) {
            areaAcaoProduto.hidden = false;
        }

        if (btnAdicionar) {
            btnAdicionar.disabled = false;
        }

        if (vendidoPorPeso) {

            /*
             * Produto vendido por peso:
             * não mostra campo manual de peso.
             * O peso entra somente pelo modal da balança.
             */
            labelQuantidade.textContent = "Peso (kg)";

            quantidade.min = "0.001";
            quantidade.step = "0.001";
            quantidade.value = "1,000";

            if (campoQuantidadePdv) {
                campoQuantidadePdv.hidden = true;
            }

            if (avisoBalancaPdv) {
                avisoBalancaPdv.hidden = false;
            }

            if (btnAdicionar) {
                btnAdicionar.textContent = "ABRIR BALANÇA";
            }

        } else {

            /*
             * Produto vendido por unidade:
             * mostra campo de quantidade.
             */
            labelQuantidade.textContent = "Quantidade";

            quantidade.min = "1";
            quantidade.step = "1";
            quantidade.value = "1";

            if (campoQuantidadePdv) {
                campoQuantidadePdv.hidden = false;
            }

            if (avisoBalancaPdv) {
                avisoBalancaPdv.hidden = true;
            }

            if (btnAdicionar) {
                btnAdicionar.textContent = "ADICIONAR ITEM";
            }
        }
    }


    /* =========================================================
     8. CAMPOS OCULTOS ENVIADOS AO BACKEND
     ========================================================= */

    function atualizarItensHidden() {

        itensHidden.innerHTML = "";

        const linhas = tabelaItens.querySelectorAll("tr");

        linhas.forEach(function (linha, index) {

            const produtoId =
                    linha.getAttribute("data-produto-id");

            const vendidoPorPeso =
                    linha.getAttribute("data-vendido-por-peso") === "true";

            const quantidadeOuPeso =
                    linha.getAttribute("data-quantidade");

            const valorUnitarioItem =
                    linha.getAttribute("data-valor-unitario");

            const totalItem =
                    linha.getAttribute("data-total");

            let campoQuantidadeOuPeso;

            if (vendidoPorPeso) {

                campoQuantidadeOuPeso = `
                    <input type="hidden"
                           name="itens[${index}].peso"
                           value="${quantidadeOuPeso}">
                `;

            } else {

                campoQuantidadeOuPeso = `
                    <input type="hidden"
                           name="itens[${index}].quantidade"
                           value="${quantidadeOuPeso}">
                `;
            }

            itensHidden.insertAdjacentHTML("beforeend", `
                <input type="hidden"
                       name="itens[${index}].produto.id"
                       value="${produtoId}">

                ${campoQuantidadeOuPeso}

                <input type="hidden"
                       name="itens[${index}].valorUnitario"
                       value="${valorUnitarioItem}">

                <input type="hidden"
                       name="itens[${index}].valorTotal"
                       value="${totalItem}">
            `);
        });
    }


    /* =========================================================
     9. SALDO E LIMITE DE CRÉDITO
     ========================================================= */

    function atualizarSaldoRestante(totalCompra) {

        /* Sem cliente identificado, mantém os valores zerados */
        if (!usuarioSelecionado.value) {

            saldoRestante.textContent =
                    "Saldo após a compra: R$ 0,00";

            limiteCliente.textContent =
                    "Crédito disponível: R$ 0,00";

            avisoSaldo.style.display = "none";

            return;
        }

        /* Calcula o saldo depois da compra */
        const novoSaldo =
                saldoAtualCliente - totalCompra;

        /*
         * O crédito só começa a ser utilizado quando
         * o saldo fica abaixo de zero.
         */
        const creditoUtilizado =
                novoSaldo < 0 ? Math.abs(novoSaldo) : 0;

        /* Calcula quanto ainda resta do limite */
        const creditoDisponivel =
                Math.max(
                        limiteCreditoCliente - creditoUtilizado,
                        0
                        );

        const limiteNegativo =
                -limiteCreditoCliente;

        /* Atualiza o saldo previsto */
        saldoRestante.textContent =
                "Saldo após a compra: R$ "
                + formatarDinheiro(novoSaldo);

        /* Atualiza o crédito disponível */
        limiteCliente.textContent =
                "Crédito disponível: R$ "
                + formatarDinheiro(creditoDisponivel);

        /* Define o aviso conforme a situação */
        if (novoSaldo < limiteNegativo) {

            avisoSaldo.textContent =
                    "Limite de crédito excedido. A compra não poderá ser finalizada.";

            avisoSaldo.style.display = "block";

        } else if (novoSaldo < 0) {

            avisoSaldo.textContent =
                    "A compra utilizará R$ "
                    + formatarDinheiro(creditoUtilizado)
                    + " do limite de crédito.";

            avisoSaldo.style.display = "block";

        } else {

            avisoSaldo.style.display = "none";
        }
    }


    /* =========================================================
     10. TOTAL DA COMPRA E QUANTIDADE DE ITENS
     ========================================================= */

    function atualizarResumo() {

        const linhas = tabelaItens.querySelectorAll("tr");

        let totalCompra = 0;
        let totalItens = 0;

        linhas.forEach(function (linha) {

            const totalLinha =
                    Number(linha.getAttribute("data-total")) || 0;

            const quantidadeLinha =
                    Number(linha.getAttribute("data-quantidade")) || 0;

            const vendidoPorPeso =
                    linha.getAttribute("data-vendido-por-peso") === "true";

            totalCompra += totalLinha;

            /*
             * Produto vendido por peso conta como um item.
             * Produto vendido por unidade soma sua quantidade.
             */
            if (vendidoPorPeso) {
                totalItens += 1;
            } else {
                totalItens += quantidadeLinha;
            }
        });

        valorTotal.value = totalCompra.toFixed(2);

        valorResumo.textContent =
                "R$ " + formatarDinheiro(totalCompra);

        quantidadeItens.textContent = totalItens;

        atualizarSaldoRestante(totalCompra);
        atualizarItensHidden();
    }


    /* =========================================================
     11. LOCALIZAR PRODUTO JÁ EXISTENTE NO CARRINHO
     ========================================================= */

    function localizarLinhaProduto(produtoId) {

        const linhas = tabelaItens.querySelectorAll("tr");

        for (const linha of linhas) {

            const idTabela =
                    linha.getAttribute("data-produto-id");
            if (idTabela === produtoId) {
                return linha;
            }
        }

        return null;
    }


    /* =========================================================
     12. ADICIONAR PRODUTO AO CARRINHO
     ========================================================= */

    function adicionarItem() {

        const opcaoSelecionada = obterOpcaoSelecionada();

        if (!opcaoSelecionada || !produto.value) {

            mostrarErroProduto(
                    "Selecione um produto antes de adicionar ao carrinho."
                    );

            return;
        }

        limparErroProduto();
        limparErroCompra();

        const produtoId = produto.value;

        const nomeProduto =
                opcaoSelecionada.textContent.trim();

        const vendidoPorPeso =
                produtoVendidoPorPeso();

        const valor =
                obterPrecoProduto();

        /*
         * Se o produto é vendido por peso, o sistema simula a leitura da balança.
         * A compra só segue depois que o peso for confirmado no modal.
         */
        if (vendidoPorPeso && !adicaoLiberadaPelaBalanca) {

            abrirModalBalanca();

            return;
        }

        const valorDigitado =
                quantidade.value
                .trim()
                .replace(",", ".");

        const quantidadeInformada =
                Number(valorDigitado);

        if (isNaN(quantidadeInformada)
                || quantidadeInformada <= 0) {

            if (vendidoPorPeso) {

                mostrarErroCompra(
                        "Informe um peso válido. Exemplo: 0,500 kg."
                        );

            } else {

                mostrarErroCompra(
                        "Informe uma quantidade válida."
                        );
            }

            quantidade.focus();
            return;
        }

        if (!vendidoPorPeso
                && !Number.isInteger(quantidadeInformada)) {

            mostrarErroCompra(
                    "Produto vendido por unidade. Informe uma quantidade inteira, como 1, 2 ou 3."
                    );

            quantidade.focus();
            return;
        }

        const linhaExistente =
                localizarLinhaProduto(produtoId);

        if (linhaExistente) {

            atualizarProdutoExistente(
                    linhaExistente,
                    quantidadeInformada,
                    valor,
                    vendidoPorPeso
                    );

        } else {

            criarNovaLinhaProduto(
                    produtoId,
                    nomeProduto,
                    quantidadeInformada,
                    valor,
                    vendidoPorPeso
                    );
        }

        atualizarResumo();
        limparErroCompra();

        quantidade.value =
                vendidoPorPeso ? "1,000" : "1";
    }


    /* =========================================================
     13. ATUALIZAR PRODUTO REPETIDO
     ========================================================= */

    function atualizarProdutoExistente(
            linha,
            quantidadeAdicionada,
            valor,
            vendidoPorPeso) {

        const quantidadeAtual =
                Number(linha.getAttribute("data-quantidade")) || 0;

        const novaQuantidade =
                quantidadeAtual + quantidadeAdicionada;

        const novoTotal =
                novaQuantidade * valor;

        linha.setAttribute(
                "data-quantidade",
                novaQuantidade.toString()
                );

        linha.setAttribute(
                "data-valor-unitario",
                valor.toFixed(2)
                );

        linha.setAttribute(
                "data-total",
                novoTotal.toFixed(2)
                );

        linha.children[1].textContent =
                vendidoPorPeso
                ? novaQuantidade.toFixed(3)
                : novaQuantidade.toFixed(0);

        linha.children[2].textContent =
                formatarDinheiro(valor);

        linha.children[3].textContent =
                formatarDinheiro(novoTotal);
    }


    /* =========================================================
     14. CRIAR NOVA LINHA NO CARRINHO
     ========================================================= */

    function criarNovaLinhaProduto(
            produtoId,
            nomeProduto,
            quantidadeInformada,
            valor,
            vendidoPorPeso) {

        const totalItem =
                quantidadeInformada * valor;

        const quantidadeExibida =
                vendidoPorPeso
                ? quantidadeInformada.toFixed(3)
                : quantidadeInformada.toFixed(0);

        const linha =
                document.createElement("tr");

        linha.setAttribute(
                "data-produto-id",
                produtoId
                );

        linha.setAttribute(
                "data-vendido-por-peso",
                vendidoPorPeso.toString()
                );

        linha.setAttribute(
                "data-quantidade",
                quantidadeInformada.toString()
                );

        linha.setAttribute(
                "data-valor-unitario",
                valor.toFixed(2)
                );

        linha.setAttribute(
                "data-total",
                totalItem.toFixed(2)
                );

        linha.innerHTML = `
            <td>${nomeProduto}</td>

            <td>${quantidadeExibida}</td>

            <td>${formatarDinheiro(valor)}</td>

            <td>${formatarDinheiro(totalItem)}</td>

            <td>
                <button type="button"
                        class="botao-mini botao-vermelho btn-remover-item">
                    REMOVER
                </button>
            </td>
        `;

        tabelaItens.appendChild(linha);
    }


    /* =========================================================
     15. REMOVER ITEM DO CARRINHO
     ========================================================= */

    tabelaItens.addEventListener("click", function (event) {

        const botaoRemover =
                event.target.closest(".btn-remover-item");

        if (!botaoRemover) {
            return;
        }

        event.preventDefault();

        const linha =
                botaoRemover.closest("tr");

        if (linha) {
            linha.remove();
            atualizarResumo();
        }
    });


    /* =========================================================
     16. MENSAGENS DO MODAL RFID
     ========================================================= */

    /**
     * Mostra uma mensagem dentro do modal de identificação.
     */
    function mostrarErroProduto(mensagem) {

        erroProduto.textContent = mensagem;
        erroProduto.hidden = false;

        produto.classList.add("campo-invalido");
        produto.focus();
    }

    function limparErroProduto() {

        erroProduto.textContent = "";
        erroProduto.hidden = true;

        produto.classList.remove("campo-invalido");
    }

    function mostrarErroCliente(mensagem) {

        erroCliente.textContent = mensagem;
        erroCliente.hidden = false;

        rfidInput.classList.add("campo-invalido");
        rfidInput.focus();
    }

    function limparErroCliente() {

        erroCliente.textContent = "";
        erroCliente.hidden = true;

        rfidInput.classList.remove("campo-invalido");
    }




    /* =========================================================
     17. ABRIR E FECHAR MODAL RFID
     ========================================================= */

    abrirModalCliente.addEventListener("click", function () {

        limparErroCliente();

        rfidInput.value = "";
        modalCliente.style.display = "flex";

        setTimeout(function () {
            rfidInput.focus();
        }, 100);
    });

    fecharModalCliente.addEventListener("click", function () {

        limparErroCliente();
        modalCliente.style.display = "none";
    });

    /* Fecha ao clicar fora da caixa branca */
    modalCliente.addEventListener("click", function (event) {

        if (event.target === modalCliente) {

            limparErroCliente();
            modalCliente.style.display = "none";
        }
    });


    /* =========================================================
     18. IDENTIFICAR CLIENTE POR RFID
     ========================================================= */

    /**
     * Busca o usuário pelo RFID e preenche os dados na tela.
     */
    async function identificarCliente() {

        const rfid = rfidInput.value.trim();

        limparErroCliente();

        if (!rfid) {

            mostrarErroCliente(
                    "Informe ou aproxime o código RFID do cliente."
                    );

            return;
        }

        buscarCliente.disabled = true;
        buscarCliente.textContent = "IDENTIFICANDO...";

        try {

            const resposta = await fetch(
                    `/usuarios/rfid/${encodeURIComponent(rfid)}`
                    );

            if (!resposta.ok) {

                if (resposta.status === 404) {

                    throw new Error(
                            "Nenhum usuário foi encontrado com este RFID."
                            );
                }

                throw new Error(
                        "Não foi possível consultar o usuário."
                        );
            }

            const usuario = await resposta.json();

            const saldo =
                    Number(usuario.saldo) || 0;

            const limite =
                    Number(
                            usuario.limiteCredito
                            ?? usuario.limite_credito
                            ) || 0;

            const limiteNegativo = -limite;

            /*
             * Não permite identificar quem já ultrapassou
             * completamente o limite de crédito.
             */
            if (saldo < limiteNegativo) {

                mostrarErroCliente(
                        "O usuário ultrapassou o limite de crédito."
                        );

                return;
            }

            /*
             * Usuário inativo com saldo positivo ou zero
             * é considerado bloqueado manualmente.
             *
             * Usuário inativo com saldo negativo dentro do limite
             * ainda é aceito temporariamente por causa da regra antiga.
             */
            if (usuario.ativo === false && saldo >= 0) {

                mostrarErroCliente(
                        "Usuário bloqueado não pode realizar compra."
                        );

                return;
            }

            /* Armazena os dados para os cálculos */
            saldoAtualCliente = saldo;
            limiteCreditoCliente = limite;

            usuarioSelecionado.value = usuario.id;

            /* Atualiza os dados visíveis */
            nomeCliente.textContent =
                    usuario.nome || "Cliente identificado";

            saldoCliente.textContent =
                    "Saldo: R$ "
                    + formatarDinheiro(saldo);

            const creditoUtilizado =
                    saldo < 0 ? Math.abs(saldo) : 0;

            const creditoDisponivel =
                    Math.max(
                            limite - creditoUtilizado,
                            0
                            );

            limiteCliente.textContent =
                    "Crédito disponível: R$ "
                    + formatarDinheiro(creditoDisponivel);

            limparErroCompra();
            limparErroCliente();

            modalCliente.style.display = "none";

            atualizarResumo();

        } catch (erro) {

            console.error(
                    "Erro ao identificar cliente:",
                    erro
                    );

            mostrarErroCliente(
                    erro.message
                    || "Não foi possível identificar o cliente."
                    );

        } finally {

            buscarCliente.disabled = false;
            buscarCliente.textContent = "IDENTIFICAR";
        }
    }


    /* =========================================================
     19. EVENTOS DE IDENTIFICAÇÃO
     ========================================================= */

    /* Clique no botão IDENTIFICAR */
    buscarCliente.addEventListener("click", function (event) {

        event.preventDefault();

        identificarCliente();
    });

    /* Pressionar Enter no campo RFID */
    rfidInput.addEventListener("keydown", function (event) {

        if (event.key === "Enter") {

            event.preventDefault();

            identificarCliente();
        }
    });


    /* =========================================================
     20. VALIDAÇÃO ANTES DE FINALIZAR A COMPRA
     ========================================================= */

    form.addEventListener("submit", function (event) {

        limparErroCompra();

        const linhas =
                tabelaItens.querySelectorAll("tr");

        /*
         * Impede finalizar sem cliente identificado.
         */
        if (!usuarioSelecionado.value) {

            event.preventDefault();

            mostrarErroCompra(
                    "Identifique o cliente antes de finalizar a compra."
                    );

            return;
        }

        /*
         * Impede finalizar sem produtos no carrinho.
         */
        if (linhas.length === 0) {

            event.preventDefault();

            mostrarErroCompra(
                    "Adicione pelo menos um item à compra."
                    );

            produto.focus();

            return;
        }

        atualizarResumo();

        const totalCompra =
                Number(valorTotal.value) || 0;

        const novoSaldo =
                saldoAtualCliente - totalCompra;

        const limiteNegativo =
                -limiteCreditoCliente;

        /*
         * Impede finalizar quando o limite for ultrapassado.
         */
        if (novoSaldo < limiteNegativo) {

            event.preventDefault();

            mostrarErroCompra(
                    "Limite de crédito excedido. A compra não pode ser finalizada."
                    );

            return;
        }

        limparErroCompra();
    });


    /* =========================================================
     21. EVENTOS DOS CAMPOS
     ========================================================= */

    produto.addEventListener("change", function () {

        limparErroProduto();
        configurarCampoQuantidade();
        atualizarProdutoSelecionadoVisual();
    });

    quantidade.addEventListener("keydown", function (event) {

        if (event.key === "Enter") {

            event.preventDefault();

            adicionarItem();
        }
    });

    btnAdicionar.addEventListener(
            "click",
            adicionarItem
            );

    /* =========================================================
     21.1 EVENTOS DO SIMULADOR DE BALANÇA
     ========================================================= */

    if (pesoBalancaInput) {

        pesoBalancaInput.addEventListener("input", function () {
            limparErroBalanca();
            atualizarPreviaBalanca();
        });

        pesoBalancaInput.addEventListener("keydown", function (event) {

            if (event.key === "Enter") {

                event.preventDefault();

                confirmarPesoDaBalanca();
            }
        });
    }

    if (confirmarPesoBalanca) {

        confirmarPesoBalanca.addEventListener("click", function () {
            confirmarPesoDaBalanca();
        });
    }

    if (cancelarBalanca) {

        cancelarBalanca.addEventListener("click", function () {
            fecharModalBalanca();
        });
    }

    if (modalBalanca) {

        modalBalanca.addEventListener("click", function (event) {

            if (event.target === modalBalanca) {
                fecharModalBalanca();
            }
        });
    }
    /* =========================================================
     21.2 EVENTOS DO MODAL DE PRODUTOS
     ========================================================= */

    if (abrirModalProduto) {

        abrirModalProduto.addEventListener("click", function () {
            abrirModalSelecaoProduto();
        });
    }

    if (fecharModalProduto) {

        fecharModalProduto.addEventListener("click", function () {
            fecharModalSelecaoProduto();
        });
    }

    if (pesquisaProdutoModal) {

        pesquisaProdutoModal.addEventListener("input", function () {
            filtrarProdutosModal();
        });
    }

    if (tabelaProdutosModal) {

        tabelaProdutosModal.addEventListener("click", function (event) {

            const botao =
                    event.target.closest(".btn-selecionar-produto-modal");

            const linha =
                    event.target.closest("tr");

            if (botao || linha) {

                selecionarProdutoPeloModal(linha);
            }
        });
    }

    if (modalProduto) {

        modalProduto.addEventListener("click", function (event) {

            if (event.target === modalProduto) {
                fecharModalSelecaoProduto();
            }
        });
    }

    /* =========================================================
     22. INICIALIZAÇÃO DA TELA
     ========================================================= */

    configurarCampoQuantidade();
    atualizarProdutoSelecionadoVisual();
    atualizarResumo();
});