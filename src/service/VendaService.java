package service;

import bd.ClienteDAO;
import bd.ProdutoDAO;
import bd.VendaDAO;
import model.Cliente;
import model.ItemVenda;
import model.Pagamento;
import model.Produto;
import model.Venda;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class VendaService {
    private final ProdutoDAO produtoDAO;
    private final ClienteDAO clienteDAO;
    private final VendaDAO vendaDAO;

    public VendaService(
            ProdutoDAO produtoDAO,
            ClienteDAO clienteDAO,
            VendaDAO vendaDAO
    ) {
        this.produtoDAO = produtoDAO;
        this.clienteDAO = clienteDAO;
        this.vendaDAO = vendaDAO;
    }

    public Venda realizarVenda(long clienteId, List<ItemVenda> itens, Pagamento pagamento) throws SQLException {
        if (itens == null || itens.isEmpty()) {
            throw new IllegalArgumentException("A venda deve possuir pelo menos um item.");
        }

        if (pagamento == null) {
            throw new IllegalArgumentException("A forma de pagamento é obrigatória.");
        }

        Cliente cliente = clienteDAO.readById(clienteId);

        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado.");
        }

        Venda venda = new Venda(cliente, itens, pagamento.getDescricao());
        BigDecimal total = venda.calcularValorTotal();

        for (ItemVenda item : itens) {
            Produto atual = produtoDAO.readById(item.getProduto().getId());

            if (atual == null) {
                throw new IllegalArgumentException("Produto " + item.getProduto().getNome() + " não existe mais.");
            }

            if (!atual.possuiEstoque(item.getQuantidade())) {
                throw new IllegalStateException("Estoque insuficiente para o produto " + atual.getNome() + ".");
            }
        }

        pagamento.processarPagamento(total);

        vendaDAO.create(venda);
        return venda;
    }

    public Venda buscarPorId(long id) throws SQLException {
        Venda venda = vendaDAO.readById(id);

        if (venda == null) {
            throw new IllegalArgumentException("Venda não encontrada.");
        }

        return venda;
    }

    public List<Venda> listarTodos() throws SQLException {
        return vendaDAO.readAll();
    }
}
