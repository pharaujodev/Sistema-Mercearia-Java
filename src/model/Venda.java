package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Venda {
    private long id;
    private final Cliente cliente;
    private final List<ItemVenda> itens;
    private final String formaPagamento;

    public Venda(long id, Cliente cliente, List<ItemVenda> itens, String formaPagamento) {
        if (id < 0) {
            throw new IllegalArgumentException("O ID não pode ser negativo.");
        }

        if (cliente == null) {
            throw new IllegalArgumentException("O cliente é obrigatório.");
        }

        if (itens == null || itens.isEmpty()) {
            throw new IllegalArgumentException("A venda deve possuir pelo menos um item.");
        }

        if (formaPagamento == null || formaPagamento.isBlank()) {
            throw new IllegalArgumentException("A forma de pagamento é obrigatória.");
        }

        this.id = id;
        this.cliente = cliente;
        this.itens = new ArrayList<>(itens);
        this.formaPagamento = formaPagamento;
    }

    public Venda(Cliente cliente, List<ItemVenda> itens, String formaPagamento) {
        this(0, cliente, itens, formaPagamento);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("O ID deve ser maior que zero.");
        }
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<ItemVenda> getItens() {
        return new ArrayList<>(itens);
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public BigDecimal calcularValorTotal() {
        BigDecimal total = BigDecimal.ZERO;

        for (ItemVenda item : itens) {
            total = total.add(item.getSubtotal());
        }

        return total;
    }

    @Override
    public String toString() {
        return "Venda [ID=" + id + ", Cliente=" + cliente.getNome() + ", Itens=" + itens.size()
                + ", Total=R$ " + calcularValorTotal() + ", Pagamento=" + formaPagamento + "]";
    }
}
