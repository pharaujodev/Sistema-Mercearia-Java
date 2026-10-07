# Projeto Java Mercearia

Projeto acadêmico em Java desenvolvido para demonstrar os principais conceitos de Programação Orientada a Objetos de forma simples.

## Estrutura do projeto

```text
src/
├── model/
│   ├── Cliente.java
│   ├── Produto.java
│   ├── ProdutoPorUnidade.java
│   ├── ProdutoPorPeso.java
│   ├── ItemVenda.java
│   ├── Venda.java
│   ├── Pagamento.java
│   ├── PagamentoPorPix.java
│   ├── PagamentoPorCartao.java
│   └── PagamentoPorDinheiro.java
├── repository/
│   ├── ProdutoRepository.java
│   ├── ProdutoRepositoryMemoria.java
│   ├── ClienteRepository.java
│   ├── ClienteRepositoryMemoria.java
│   ├── VendaRepository.java
│   └── VendaRepositoryMemoria.java
├── service/
│   └── VendaService.java
└── Main.java
```

A estrutura foi mantida pequena de propósito. O cadastro e a consulta de produtos e clientes usam os repositórios diretamente. O `VendaService` existe porque realizar uma venda envolve várias regras ao mesmo tempo.

## Conceitos utilizados

- **Abstração:** `Produto` é uma classe abstrata.
- **Encapsulamento:** os atributos são privados e o estoque é alterado por métodos da própria classe.
- **Herança:** `ProdutoPorUnidade` e `ProdutoPorPeso` herdam de `Produto`.
- **Polimorfismo:** os tipos de produto implementam `calcularPrecoVenda` de acordo com sua regra, e as formas de pagamento implementam o mesmo contrato `Pagamento`.
- **Interface:** `Pagamento`, `ProdutoRepository`, `ClienteRepository` e `VendaRepository`.
- **Tratamento de exceções:** `try/catch`, `IllegalArgumentException`, `IllegalStateException` e `NumberFormatException`.

## Armazenamento atual e banco de dados futuro

Os repositórios em memória usam `ArrayList`:

```text
Repository -> RepositoryMemoria -> ArrayList
```

As regras usam as interfaces de repositório. No futuro, uma implementação de banco pode substituir a implementação em memória:

```text
Repository -> RepositoryBanco -> MySQL/PostgreSQL
```

Assim, não é necessário colocar banco de dados no projeto atual para deixar a arquitetura preparada para persistência futura.

## Menu

O programa usa menus simples com `while (opcao != 0)`, `if`, `for` e `switch`.

Menu principal:

```text
1 - Produtos
2 - Clientes
3 - Vendas
0 - Sair
```

O sistema permite cadastrar, listar, buscar e remover produtos e clientes, adicionar estoque, realizar vendas com vários itens, escolher a forma de pagamento e consultar vendas.
