import bd.ClienteDAO;
import bd.ProdutoDAO;
import bd.VendaDAO;
import model.Cliente;
import model.ItemVenda;
import model.Pagamento;
import model.PagamentoPorCartao;
import model.PagamentoPorDinheiro;
import model.PagamentoPorPix;
import model.Produto;
import model.ProdutoPorPeso;
import model.ProdutoPorUnidade;
import model.Venda;
import service.VendaService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    private static ProdutoDAO produtoDAO;
    private static ClienteDAO clienteDAO;
    private static VendaService vendaService;

    public static void main(String[] args) {
        produtoDAO = new ProdutoDAO();
        clienteDAO = new ClienteDAO();
        VendaDAO vendaDAO = new VendaDAO();

        vendaService = new VendaService(
                produtoDAO,
                clienteDAO,
                vendaDAO
        );

        int opcao = -1;

        while (opcao != 0) {
            exibirMenuPrincipal();
            opcao = lerInt("Escolha uma opção: ");

            try {
                switch (opcao) {
                    case 1:
                        menuProdutos();
                        break;
                    case 2:
                        menuClientes();
                        break;
                    case 3:
                        menuVendas();
                        break;
                    case 0:
                        System.out.println("Sistema encerrado.");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Erro: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Erro no banco de dados: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private static void exibirMenuPrincipal() {
        System.out.println("\n============================");
        System.out.println("      SISTEMA MERCATTO");
        System.out.println("============================");
        System.out.println("1 - Produtos");
        System.out.println("2 - Clientes");
        System.out.println("3 - Vendas");
        System.out.println("0 - Sair");
    }

    private static void menuProdutos() throws SQLException {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n--- MENU DE PRODUTOS ---");
            System.out.println("1 - Cadastrar produto");
            System.out.println("2 - Listar produtos");
            System.out.println("3 - Buscar produto por ID");
            System.out.println("4 - Adicionar estoque");
            System.out.println("5 - Atualizar produto");
            System.out.println("6 - Remover produto");
            System.out.println("0 - Voltar");

            opcao = lerInt("Escolha uma opção: ");

            try {
                switch (opcao) {
                    case 1:
                        cadastrarProduto();
                        break;
                    case 2:
                        listarProdutos();
                        break;
                    case 3:
                        buscarProduto();
                        break;
                    case 4:
                        adicionarEstoque();
                        break;
                    case 5:
                        atualizarProduto();
                        break;
                    case 6:
                        removerProduto();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private static void menuClientes() throws SQLException {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n--- MENU DE CLIENTES ---");
            System.out.println("1 - Cadastrar cliente");
            System.out.println("2 - Listar clientes");
            System.out.println("3 - Buscar cliente por ID");
            System.out.println("4 - Atualizar cliente");
            System.out.println("5 - Remover cliente");
            System.out.println("0 - Voltar");

            opcao = lerInt("Escolha uma opção: ");

            try {
                switch (opcao) {
                    case 1:
                        cadastrarCliente();
                        break;
                    case 2:
                        listarClientes();
                        break;
                    case 3:
                        buscarCliente();
                        break;
                    case 4:
                        atualizarCliente();
                        break;
                    case 5:
                        removerCliente();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private static void menuVendas() throws SQLException {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n--- MENU DE VENDAS ---");
            System.out.println("1 - Realizar venda");
            System.out.println("2 - Listar vendas");
            System.out.println("3 - Buscar venda por ID");
            System.out.println("0 - Voltar");

            opcao = lerInt("Escolha uma opção: ");

            try {
                switch (opcao) {
                    case 1:
                        realizarVenda();
                        break;
                    case 2:
                        listarVendas();
                        break;
                    case 3:
                        buscarVenda();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private static void cadastrarProduto() throws SQLException {
        System.out.println("\nTipo do produto:");
        System.out.println("1 - Produto por unidade");
        System.out.println("2 - Produto por peso");

        int tipo = lerInt("Escolha o tipo: ");
        String nome = lerTexto("Nome: ");
        Produto produto;

        if (tipo == 1) {
            BigDecimal preco = lerDecimal("Preço por unidade: R$ ");
            BigDecimal estoque = BigDecimal.valueOf(lerInt("Estoque inicial (unidades): "));
            produto = new ProdutoPorUnidade(nome, preco, estoque);
        } else if (tipo == 2) {
            BigDecimal preco = lerDecimal("Preço por kg: R$ ");
            BigDecimal estoqueKg = lerDecimal("Estoque inicial (kg): ");
            produto = new ProdutoPorPeso(nome, preco, estoqueKg);
        } else {
            throw new IllegalArgumentException("Tipo de produto inválido.");
        }

        produtoDAO.create(produto);
        System.out.println("Produto cadastrado com sucesso. ID: " + produto.getId());
    }

    private static void listarProdutos() throws SQLException {
        List<Produto> produtos = produtoDAO.readAll();

        System.out.println("\n--- PRODUTOS ---");

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
        } else {
            for (Produto produto : produtos) {
                exibirProduto(produto);
            }
        }
    }

    private static void buscarProduto() throws SQLException {
        long id = lerLong("ID do produto: ");
        Produto produto = obterProduto(id);
        exibirProduto(produto);
    }

    private static void adicionarEstoque() throws SQLException {
        listarProdutos();

        long id = lerLong("ID do produto: ");
        Produto produto = obterProduto(id);
        BigDecimal quantidade = lerDecimal(
                "Quantidade a adicionar (" + produto.getUnidadeMedida() + "): "
        );

        produto.adicionarEstoque(quantidade);

        if (!produtoDAO.update(produto)) {
            throw new IllegalStateException("Não foi possível atualizar o estoque.");
        }

        System.out.println("Estoque atualizado com sucesso.");
    }

    private static void atualizarProduto() throws SQLException {
        listarProdutos();

        long id = lerLong("ID do produto que deseja atualizar: ");
        Produto produtoAtual = obterProduto(id);

        System.out.println("\nDados atuais:");
        exibirProduto(produtoAtual);

        String nome = lerTexto("Novo nome: ");
        BigDecimal preco = lerDecimal("Novo preço: R$ ");
        Produto produtoAtualizado;

        if (produtoAtual instanceof ProdutoPorUnidade) {
            BigDecimal estoque = BigDecimal.valueOf(lerInt("Novo estoque (unidades): "));
            produtoAtualizado = new ProdutoPorUnidade(id, nome, preco, estoque);
        } else {
            BigDecimal estoqueKg = lerDecimal("Novo estoque (kg): ");
            produtoAtualizado = new ProdutoPorPeso(id, nome, preco, estoqueKg);
        }

        if (!produtoDAO.update(produtoAtualizado)) {
            throw new IllegalStateException("Não foi possível atualizar o produto.");
        }

        System.out.println("Produto atualizado com sucesso.");
    }

    private static void removerProduto() throws SQLException {
        listarProdutos();
        long id = lerLong("ID do produto que deseja remover: ");

        // Se o produto já tiver vendas, o DAO lança IllegalStateException
        // com uma mensagem explicando o motivo.
        if (!produtoDAO.delete(id)) {
            throw new IllegalArgumentException("Produto não encontrado.");
        }

        System.out.println("Produto removido com sucesso.");
    }

    private static Produto obterProduto(long id) throws SQLException {
        Produto produto = produtoDAO.readById(id);

        if (produto == null) {
            throw new IllegalArgumentException("Produto não encontrado.");
        }

        return produto;
    }

    private static void cadastrarCliente() throws SQLException {
        String nome = lerTexto("Nome: ");
        String email = lerTexto("Email: ");
        String telefone = lerTexto("Telefone: ");
        String endereco = lerTexto("Endereço: ");

        Cliente cliente = new Cliente(nome, email, telefone, endereco);
        clienteDAO.create(cliente);

        System.out.println("Cliente cadastrado com sucesso. ID: " + cliente.getId());
    }

    private static void listarClientes() throws SQLException {
        List<Cliente> clientes = clienteDAO.readAll();

        System.out.println("\n--- CLIENTES ---");

        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
        } else {
            for (Cliente cliente : clientes) {
                exibirCliente(cliente);
            }
        }
    }

    private static void buscarCliente() throws SQLException {
        long id = lerLong("ID do cliente: ");
        Cliente cliente = obterCliente(id);
        exibirCliente(cliente);
    }

    private static void atualizarCliente() throws SQLException {
        listarClientes();

        long id = lerLong("ID do cliente que deseja atualizar: ");
        Cliente clienteAtual = obterCliente(id);

        System.out.println("\nDados atuais:");
        exibirCliente(clienteAtual);

        String nome = lerTexto("Novo nome: ");
        String email = lerTexto("Novo email: ");
        String telefone = lerTexto("Novo telefone: ");
        String endereco = lerTexto("Novo endereço: ");

        Cliente clienteAtualizado = new Cliente(
                id,
                nome,
                email,
                telefone,
                endereco
        );

        if (!clienteDAO.update(clienteAtualizado)) {
            throw new IllegalStateException("Não foi possível atualizar o cliente.");
        }

        System.out.println("Cliente atualizado com sucesso.");
    }

    private static void removerCliente() throws SQLException {
        listarClientes();
        long id = lerLong("ID do cliente que deseja remover: ");

        if (!clienteDAO.delete(id)) {
            throw new IllegalArgumentException("Cliente não encontrado.");
        }

        System.out.println("Cliente removido com sucesso.");
    }

    private static Cliente obterCliente(long id) throws SQLException {
        Cliente cliente = clienteDAO.readById(id);

        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado.");
        }

        return cliente;
    }

    private static void realizarVenda() throws SQLException {
        if (clienteDAO.readAll().isEmpty()) {
            throw new IllegalStateException("Cadastre pelo menos um cliente antes de realizar uma venda.");
        }

        if (produtoDAO.readAll().isEmpty()) {
            throw new IllegalStateException("Cadastre pelo menos um produto antes de realizar uma venda.");
        }

        listarClientes();
        long clienteId = lerLong("ID do cliente: ");
        obterCliente(clienteId);

        List<ItemVenda> carrinho = new ArrayList<>();
        long produtoId = -1;

        while (produtoId != 0) {
            listarProdutos();
            produtoId = lerLong("ID do produto (0 para finalizar): ");

            if (produtoId != 0) {
                Produto produto = obterProduto(produtoId);

                if (produtoJaFoiAdicionado(carrinho, produtoId)) {
                    System.out.println("Esse produto já foi adicionado ao carrinho.");
                } else {
                    BigDecimal quantidade = lerDecimal(
                            "Quantidade de " + produto.getNome()
                                    + " (" + produto.getUnidadeMedida() + "): "
                    );

                    ItemVenda item = new ItemVenda(produto, quantidade);
                    carrinho.add(item);
                    System.out.println("Produto adicionado ao carrinho.");
                }
            } else if (carrinho.isEmpty()) {
                System.out.println("Adicione pelo menos um produto antes de finalizar.");
                produtoId = -1;
            }
        }

        BigDecimal total = calcularTotalCarrinho(carrinho);
        Pagamento pagamento = escolherFormaPagamento(total);
        Venda venda = vendaService.realizarVenda(clienteId, carrinho, pagamento);

        System.out.println("\nVenda realizada com sucesso!");
        exibirVenda(venda);
    }

    private static boolean produtoJaFoiAdicionado(List<ItemVenda> carrinho, long produtoId) {
        for (ItemVenda item : carrinho) {
            if (item.getProduto().getId() == produtoId) {
                return true;
            }
        }

        return false;
    }

    private static BigDecimal calcularTotalCarrinho(List<ItemVenda> carrinho) {
        BigDecimal total = BigDecimal.ZERO;

        for (ItemVenda item : carrinho) {
            total = total.add(item.getSubtotal());
        }

        return total;
    }

    private static Pagamento escolherFormaPagamento(BigDecimal total) {
        System.out.printf("%nTotal da compra: R$ %.2f%n", total);
        System.out.println("1 - PIX");
        System.out.println("2 - Cartão");
        System.out.println("3 - Dinheiro");

        int opcao = lerInt("Escolha a forma de pagamento: ");

        if (opcao == 1) {
            return new PagamentoPorPix();
        }

        if (opcao == 2) {
            return new PagamentoPorCartao();
        }

        if (opcao == 3) {
            BigDecimal valorRecebido = lerDecimal("Valor recebido: R$ ");
            return new PagamentoPorDinheiro(valorRecebido);
        }

        throw new IllegalArgumentException("Forma de pagamento inválida.");
    }

    private static void listarVendas() throws SQLException {
        List<Venda> vendas = vendaService.listarTodos();

        System.out.println("\n--- VENDAS ---");

        if (vendas.isEmpty()) {
            System.out.println("Nenhuma venda registrada.");
        } else {
            for (Venda venda : vendas) {
                exibirVenda(venda);
            }
        }
    }

    private static void buscarVenda() throws SQLException {
        long id = lerLong("ID da venda: ");
        Venda venda = vendaService.buscarPorId(id);
        exibirVenda(venda);
    }

    private static void exibirProduto(Produto produto) {
        System.out.printf(
                "ID: %d | %s | Preço: R$ %.2f/%s | Estoque: %.2f %s%n",
                produto.getId(),
                produto.getNome(),
                produto.getPreco(),
                produto.getUnidadeMedida(),
                produto.getQuantidadeEstoque(),
                produto.getUnidadeMedida()
        );
    }

    private static void exibirCliente(Cliente cliente) {
        System.out.println(
                "ID: " + cliente.getId()
                        + " | Nome: " + cliente.getNome()
                        + " | Email: " + cliente.getEmail()
                        + " | Telefone: " + cliente.getTelefone()
        );
    }

    private static void exibirVenda(Venda venda) {
        System.out.println("---------------------------------");
        System.out.println("Venda #" + venda.getId());
        System.out.println("Cliente: " + venda.getCliente().getNome());

        for (ItemVenda item : venda.getItens()) {
            System.out.printf(
                    "- %s | %.2f %s | Subtotal: R$ %.2f%n",
                    item.getProduto().getNome(),
                    item.getQuantidade(),
                    item.getProduto().getUnidadeMedida(),
                    item.getSubtotal()
            );
        }

        System.out.printf("Total: R$ %.2f%n", venda.calcularValorTotal());
        System.out.println("Pagamento: " + venda.getFormaPagamento());
        System.out.println("---------------------------------");
    }

    private static int lerInt(String mensagem) {
        int valor = 0;
        boolean valido = false;

        while (!valido) {
            try {
                System.out.print(mensagem);
                valor = Integer.parseInt(scanner.nextLine().trim());
                valido = true;
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro válido.");
            }
        }

        return valor;
    }

    private static long lerLong(String mensagem) {
        long valor = 0;
        boolean valido = false;

        while (!valido) {
            try {
                System.out.print(mensagem);
                valor = Long.parseLong(scanner.nextLine().trim());
                valido = true;
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro válido.");
            }
        }

        return valor;
    }

    private static BigDecimal lerDecimal(String mensagem) {
        BigDecimal valor = BigDecimal.ZERO;
        boolean valido = false;

        while (!valido) {
            try {
                System.out.print(mensagem);
                String texto = scanner.nextLine().trim().replace(',', '.');
                valor = new BigDecimal(texto); // aceita 12,50 ou 12.50
                valido = true;
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido.");
            }
        }

        return valor;
    }

    private static String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }
}
