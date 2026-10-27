package bd;

import model.Cliente;
import model.ItemVenda;
import model.Venda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VendaDAO {
    private final ItemVendaDAO itemVendaDAO = new ItemVendaDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    public Venda create(Venda venda) throws SQLException {
        String sqlVenda = "INSERT INTO venda (cliente_id, forma_pagamento) VALUES (?, ?)";

        try (Connection conn = ConexaoBD.getConexao()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtVenda = conn.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS)) {
                stmtVenda.setLong(1, venda.getCliente().getId());
                stmtVenda.setString(2, venda.getFormaPagamento());

                int affectedRows = stmtVenda.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Falha ao criar venda, nenhuma linha afetada.");
                }

                try (ResultSet generatedKeys = stmtVenda.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        venda.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Falha ao criar venda, nenhum ID obtido.");
                    }
                }

                for (ItemVenda item : venda.getItens()) {
                    itemVendaDAO.create(conn, venda.getId(), item);
                    produtoDAO.baixarEstoque(conn, item.getProduto().getId(), item.getQuantidade());
                }

                conn.commit();
                return venda;

            } catch (SQLException | RuntimeException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public List<Venda> readAll() throws SQLException {
        List<Venda> vendas = new ArrayList<>();
        String sql = "SELECT v.id AS venda_id, v.forma_pagamento, "
                + "c.id AS cliente_id, c.nome, c.email, c.telefone, c.endereco "
                + "FROM venda v INNER JOIN cliente c ON v.cliente_id = c.id ORDER BY v.id";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = mapearCliente(rs);
                long vendaId = rs.getLong("venda_id");
                List<ItemVenda> itens = itemVendaDAO.readByVendaId(conn, vendaId);

                vendas.add(new Venda(
                        vendaId,
                        cliente,
                        itens,
                        rs.getString("forma_pagamento")
                ));
            }
        }

        return vendas;
    }

    public Venda readById(long id) throws SQLException {
        String sql = "SELECT v.id AS venda_id, v.forma_pagamento, "
                + "c.id AS cliente_id, c.nome, c.email, c.telefone, c.endereco "
                + "FROM venda v INNER JOIN cliente c ON v.cliente_id = c.id WHERE v.id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = mapearCliente(rs);
                    List<ItemVenda> itens = itemVendaDAO.readByVendaId(conn, id);

                    return new Venda(
                            id,
                            cliente,
                            itens,
                            rs.getString("forma_pagamento")
                    );
                }
            }
        }

        return null;
    }

    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        return new Cliente(
                rs.getLong("cliente_id"),
                rs.getString("nome"),
                rs.getString("email"),
                rs.getString("telefone"),
                rs.getString("endereco")
        );
    }
}
