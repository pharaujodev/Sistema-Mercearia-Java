# Mercatto - JDBC / Aula 5

Versão do projeto adaptada para comunicação com MySQL seguindo o padrão proposto:

- `model`: classes do domínio.
- `bd/ConexaoBD.java`: centraliza a conexão JDBC.
- `bd/ProdutoDAO.java`: CRUD de produtos.
- `bd/ClienteDAO.java`: CRUD de clientes.
- `bd/VendaDAO.java`: grava e lê vendas. A gravação (venda + itens + baixa de estoque) acontece em uma única transação.
- `bd/ItemVendaDAO.java`: grava e lê os itens de uma venda, usando a conexão/transação do `VendaDAO`.
- `service/VendaService.java`: mantém a regra de negócio da venda.
- `Main.java`: controla menus e chama DAO/Service, sem conter SQL.

## Antes de executar

1. Execute `sql/mercearia.sql` no MySQL.
2. Baixe o MySQL Connector/J e coloque o `.jar` na pasta `lib`.
3. Adicione o `.jar` ao Build Path/Classpath da IDE.
4. Em `src/bd/ConexaoBD.java`, substitua `SUA_SENHA` pela senha do MySQL.

A URL configurada é:

```text
jdbc:mysql://localhost:3306/mercearia
```


## Funcionalidades

- Produtos: cadastrar, listar, buscar, adicionar estoque, atualizar e remover.
- Clientes: cadastrar, listar, buscar, atualizar e remover.
- Vendas: realizar, listar e buscar por ID.

As atualizações de produto e cliente utilizam os métodos `update()` de seus respectivos DAOs, mantendo o SQL fora do `Main`.
