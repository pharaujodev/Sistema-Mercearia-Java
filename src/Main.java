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
import repository.ClienteRepository;
import repository.ClienteRepositoryMemoria;
import repository.ProdutoRepository;
import repository.ProdutoRepositoryMemoria;
import repository.VendaRepository;
import repository.VendaRepositoryMemoria;
import service.VendaService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    private static ProdutoRepository produtoRepository;
    private static ClienteRepository clienteRepository;
    private static VendaService vendaService;

    private static long proximoProdutoId = 1;
    private static long proximoClienteId = 1;

    public static void main(String[] args) {
        produtoRepository = new ProdutoRepositoryMemoria();
        clienteRepository = new ClienteRepositoryMemoria();
        VendaRepository vendaRepository = new VendaRepositoryMemoria();

        vendaService = new VendaService(
                produtoRepository,
                clienteRepository,
                vendaRepository
        );

        carregarDadosDeExemplo();

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
            }
        }

        scanner.close();
    }

    private static void exibirMenuPrincipal() {
        System.out.println("\n============================");
        System.out.println("      SISTEMA MERCEARIA");
        System.out.println("============================");
        System.out.println("1 - Produtos");
        System.out.println("2 - Clientes");
        System.out.println("3 - Vendas");
        System.out.println("0 - Sair");
    }

    private static void menuProdutos() {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n--- MENU DE PRODUTOS ---");
            System.out.println("1 - Cadastrar produto");
            System.out.println("2 - Listar produtos");
            System.out.println("3 - Buscar produto por ID");
            System.out.println("4 - Adicionar estoque");
            System.out.println("5 - Remover produto");
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

    private static void menuClientes() {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n--- MENU DE CLIENTES ---");
            System.out.println("1 - Cadastrar cliente");
            System.out.println("2 - Listar clientes");
            System.out.println("3 - Buscar cliente por ID");
            System.out.println("4 - Remover cliente");
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

    private static void menuVendas() {
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

    private static void cadastrarProduto() {
        System.out.println("\nTipo do produto:");
        System.out.println("1 - Produto por unidade");
        System.out.println("2 - Produto por peso");

        int tipo = lerInt("Escolha o tipo: ");
        String nome = lerTexto("Nome: ");
        Produto produto;

        if (tipo == 1) {
            double preco = lerDouble("Preço por unidade: R$ ");
            int estoque = lerInt("Estoque inicial (unidades): ");
            produto = new ProdutoPorUnidade(proximoProdutoId, nome, preco, estoque);
        } else if (tipo == 2) {
            double preco = lerDouble("Preço por kg: R$ ");
            double estoqueKg = lerDouble("Estoque inicial (kg): ");
            produto = new ProdutoPorPeso(proximoProdutoId, nome, preco, estoqueKg);
        } else {
            throw new IllegalArgumentException("Tipo de produto inválido.");
        }

        produtoRepository.salvar(produto);
        proximoProdutoId++;

        System.out.println("Produto cadastrado com sucesso. ID: " + produto.getId());
    }

    private static void listarProdutos() {
        List<Produto> produtos = produtoRepository.listarTodos();

        System.out.println("\n--- PRODUTOS ---");

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
        } else {
            for (Produto produto : produtos) {
                exibirProduto(produto);
            }
        }
    }

    private static void buscarProduto() {
        long id = lerLong("ID do produto: ");
        Produto produto = obterProduto(id);
        exibirProduto(produto);
    }

    private static void adicionarEstoque() {
        listarProdutos();

        long id = lerLong("ID do produto: ");
        Produto produto = obterProduto(id);
        double quantidade = lerDouble(
                "Quantidade a adicionar (" + produto.getUnidadeMedida() + "): "
        );

        produto.adicionarEstoque(quantidade);
        produtoRepository.salvar(produto);

        System.out.println("Estoque atualizado com sucesso.");
    }

    private static void removerProduto() {
        listarProdutos();
        long id = lerLong("ID do produto que deseja remover: ");
        produtoRepository.remover(id);
        System.out.println("Produto removido com sucesso.");
    }

    private static Produto obterProduto(long id) {
        Produto produto = produtoRepository.buscarPorId(id);

        if (produto == null) {
            throw new IllegalArgumentException("Produto não encontrado.");
        }

        return produto;
    }

    private static void cadastrarCliente() {
        String nome = lerTexto("Nome: ");
        String email = lerTexto("Email: ");
        String telefone = lerTexto("Telefone: ");
        String endereco = lerTexto("Endereço: ");

        Cliente cliente = new Cliente(
                proximoClienteId,
                nome,
                email,
                telefone,
                endereco
        );

        clienteRepository.salvar(cliente);
        proximoClienteId++;

        System.out.println("Cliente cadastrado com sucesso. ID: " + cliente.getId());
    }

    private static void listarClientes() {
        List<Cliente> clientes = clienteRepository.listarTodos();

        System.out.println("\n--- CLIENTES ---");

        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
        } else {
            for (Cliente cliente : clientes) {
                exibirCliente(cliente);
            }
        }
    }

    private static void buscarCliente() {
        long id = lerLong("ID do cliente: ");
        Cliente cliente = obterCliente(id);
        exibirCliente(cliente);
    }

    private static void removerCliente() {
        listarClientes();
        long id = lerLong("ID do cliente que deseja remover: ");
        clienteRepository.remover(id);
        System.out.println("Cliente removido com sucesso.");
    }

    private static Cliente obterCliente(long id) {
        Cliente cliente = clienteRepository.buscarPorId(id);

        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado.");
        }

        return cliente;
    }

    private static void realizarVenda() {
        if (clienteRepository.listarTodos().isEmpty()) {
            throw new IllegalStateException("Cadastre pelo menos um cliente antes de realizar uma venda.");
        }

        if (produtoRepository.listarTodos().isEmpty()) {
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
                    double quantidade = lerDouble(
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

        double total = calcularTotalCarrinho(carrinho);
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

    private static double calcularTotalCarrinho(List<ItemVenda> carrinho) {
        double total = 0;

        for (ItemVenda item : carrinho) {
            total += item.getSubtotal();
        }

        return total;
    }

    private static Pagamento escolherFormaPagamento(double total) {
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
            double valorRecebido = lerDouble("Valor recebido: R$ ");
            return new PagamentoPorDinheiro(valorRecebido);
        }

        throw new IllegalArgumentException("Forma de pagamento inválida.");
    }

    private static void listarVendas() {
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

    private static void buscarVenda() {
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

    private static double lerDouble(String mensagem) {
        double valor = 0;
        boolean valido = false;

        while (!valido) {
            try {
                System.out.print(mensagem);
                String texto = scanner.nextLine().trim().replace(',', '.');
                valor = Double.parseDouble(texto);
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

    private static void carregarDadosDeExemplo() {
        Produto arroz = new ProdutoPorUnidade(
                proximoProdutoId,
                "Arroz 5kg",
                25.90,
                20
        );
        produtoRepository.salvar(arroz);
        proximoProdutoId++;

        Produto feijao = new ProdutoPorUnidade(
                proximoProdutoId,
                "Feijão 1kg",
                8.50,
                30
        );
        produtoRepository.salvar(feijao);
        proximoProdutoId++;

        Produto queijo = new ProdutoPorPeso(
                proximoProdutoId,
                "Queijo Mussarela",
                42.90,
                12.5
        );
        produtoRepository.salvar(queijo);
        proximoProdutoId++;

        Cliente cliente = new Cliente(
                proximoClienteId,
                "Cliente Exemplo",
                "cliente@email.com",
                "(62) 99999-9999",
                "Goiânia - GO"
        );
        clienteRepository.salvar(cliente);
        proximoClienteId++;
    }
}
