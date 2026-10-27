package bd;

import model.Produto;
import model.ProdutoPorPeso;
import model.ProdutoPorUnidade;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public Produto create(Produto produto) throws SQLException {
        String sql = "INSERT INTO produto (nome, preco, quantidade_estoque, tipo) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, produto.getNome());
            stmt.setBigDecimal(2, produto.getPreco());
            stmt.setBigDecimal(3, produto.getQuantidadeEstoque());
            stmt.setString(4, obterTipo(produto));

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Falha ao criar produto, nenhuma linha afetada.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    produto.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Falha ao criar produto, nenhum ID obtido.");
                }
            }

            return produto;
        }
    }

    public List<Produto> readAll() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produto";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                produtos.add(mapearProduto(rs));
            }
        }

        return produtos;
    }

    public Produto readById(long id) throws SQLException {
        String sql = "SELECT * FROM produto WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearProduto(rs);
                }
            }
        }

        return null;
    }

    public boolean update(Produto produto) throws SQLException {
        String sql = "UPDATE produto SET nome = ?, preco = ?, quantidade_estoque = ?, tipo = ? WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setBigDecimal(2, produto.getPreco());
            stmt.setBigDecimal(3, produto.getQuantidadeEstoque());
            stmt.setString(4, obterTipo(produto));
            stmt.setLong(5, produto.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Dá baixa no estoque usando a conexão (transação) da venda.
     * O "quantidade_estoque >= ?" impede que o estoque fique negativo.
     */
    public void baixarEstoque(Connection conn, long produtoId, BigDecimal quantidade) throws SQLException {
        String sql = "UPDATE produto SET quantidade_estoque = quantidade_estoque - ? "
                + "WHERE id = ? AND quantidade_estoque >= ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, quantidade);
            stmt.setLong(2, produtoId);
            stmt.setBigDecimal(3, quantidade);

            if (stmt.executeUpdate() == 0) {
                throw new IllegalStateException(
                        "Estoque insuficiente ou produto inexistente (ID " + produtoId + ")."
                );
            }
        }
    }

    public boolean delete(long id) throws SQLException {
        String sql = "DELETE FROM produto WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            // Chave estrangeira: o produto aparece em item_venda.
            throw new IllegalStateException(
                    "Não é possível remover este produto, pois ele já foi vendido."
            );
        }
    }

    private Produto mapearProduto(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String nome = rs.getString("nome");
        BigDecimal preco = rs.getBigDecimal("preco");
        BigDecimal estoque = rs.getBigDecimal("quantidade_estoque");
        String tipo = rs.getString("tipo");

        if ("UNIDADE".equalsIgnoreCase(tipo)) {
            return new ProdutoPorUnidade(id, nome, preco, estoque);
        }

        if ("PESO".equalsIgnoreCase(tipo)) {
            return new ProdutoPorPeso(id, nome, preco, estoque);
        }

        throw new SQLException("Tipo de produto inválido no banco: " + tipo);
    }

    private String obterTipo(Produto produto) throws SQLException {
        if (produto instanceof ProdutoPorUnidade) {
            return "UNIDADE";
        }

        if (produto instanceof ProdutoPorPeso) {
            return "PESO";
        }

        throw new SQLException("Tipo de produto não suportado.");
    }
}
