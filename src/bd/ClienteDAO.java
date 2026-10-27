package bd;

import model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public Cliente create(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (nome, email, telefone, endereco) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getEndereco());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Falha ao criar cliente, nenhuma linha afetada.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cliente.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Falha ao criar cliente, nenhum ID obtido.");
                }
            }

            return cliente;
        }
    }

    public List<Cliente> readAll() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }
        }

        return clientes;
    }

    public Cliente readById(long id) throws SQLException {
        String sql = "SELECT * FROM cliente WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearCliente(rs);
                }
            }
        }

        return null;
    }

    public boolean update(Cliente cliente) throws SQLException {
        String sql = "UPDATE cliente SET nome = ?, email = ?, telefone = ?, endereco = ? WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getEndereco());
            stmt.setLong(5, cliente.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(long id) throws SQLException {
        String sql = "DELETE FROM cliente WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            // Chave estrangeira: o cliente aparece na tabela venda.
            throw new IllegalStateException(
                    "Não é possível remover este cliente, pois ele possui vendas registradas."
            );
        }
    }

    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        return new Cliente(
                rs.getLong("id"),
                rs.getString("nome"),
                rs.getString("email"),
                rs.getString("telefone"),
                rs.getString("endereco")
        );
    }
}
