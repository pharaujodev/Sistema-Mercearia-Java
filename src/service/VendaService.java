package service;

import model.Cliente;
import model.ItemVenda;
import model.Pagamento;
import model.Venda;
import repository.ClienteRepository;
import repository.ProdutoRepository;
import repository.VendaRepository;

import java.util.List;

public class VendaService {
    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;
    private final VendaRepository vendaRepository;
    private long proximoId = 1;

    public VendaService(
            ProdutoRepository produtoRepository,
            ClienteRepository clienteRepository,
            VendaRepository vendaRepository
    ) {
        this.produtoRepository = produtoRepository;
        this.clienteRepository = clienteRepository;
        this.vendaRepository = vendaRepository;
    }

    public Venda realizarVenda(long clienteId, List<ItemVenda> itens, Pagamento pagamento) {
        if (itens == null || itens.isEmpty()) {
            throw new IllegalArgumentException("A venda deve possuir pelo menos um item.");
        }

        if (pagamento == null) {
            throw new IllegalArgumentException("A forma de pagamento é obrigatória.");
        }

        Cliente cliente = clienteRepository.buscarPorId(clienteId);

        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado.");
        }

        Venda venda = new Venda(proximoId, cliente, itens, pagamento.getDescricao());
        double total = venda.calcularValorTotal();

        pagamento.processarPagamento(total);

        for (ItemVenda item : itens) {
            item.getProduto().removerEstoque(item.getQuantidade());
            produtoRepository.salvar(item.getProduto());
        }

        vendaRepository.salvar(venda);
        proximoId++;

        return venda;
    }

    public Venda buscarPorId(long id) {
        Venda venda = vendaRepository.buscarPorId(id);

        if (venda == null) {
            throw new IllegalArgumentException("Venda não encontrada.");
        }

        return venda;
    }

    public List<Venda> listarTodos() {
        return vendaRepository.listarTodos();
    }
}
