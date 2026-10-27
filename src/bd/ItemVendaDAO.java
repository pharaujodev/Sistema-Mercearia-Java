package bd;

import model.ItemVenda;
import model.Produto;
import model.ProdutoPorPeso;
import model.ProdutoPorUnidade;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemVendaDAO {

    public void create(Connection conn, long vendaId, ItemVenda item) throws SQLException {
        String sql = "INSERT INTO item_venda (venda_id, produto_id, quantidade, subtotal) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, vendaId);
            stmt.setLong(2, item.getProduto().getId());
            stmt.setBigDecimal(3, item.getQuantidade());
            stmt.setBigDecimal(4, item.getSubtotal());

            if (stmt.executeUpdate() == 0) {
                throw new SQLException("Falha ao criar item da venda, nenhuma linha afetada.");
            }
        }
    }

    public List<ItemVenda> readByVendaId(Connection conn, long vendaId) throws SQLException {
        List<ItemVenda> itens = new ArrayList<>();
        String sql = "SELECT p.id, p.nome, p.preco, p.quantidade_estoque, p.tipo, "
                + "iv.quantidade, iv.subtotal "
                + "FROM item_venda iv INNER JOIN produto p ON iv.produto_id = p.id "
                + "WHERE iv.venda_id = ? ORDER BY iv.id";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, vendaId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Produto produto = mapearProduto(rs);
                    BigDecimal quantidade = rs.getBigDecimal("quantidade");
                    BigDecimal subtotal = rs.getBigDecimal("subtotal");
                    itens.add(new ItemVenda(produto, quantidade, subtotal));
                }
            }
        }

        return itens;
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
}
