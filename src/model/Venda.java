package model;

import java.util.ArrayList;
import java.util.List;

public class Venda {
    private final long id;
    private final Cliente cliente;
    private final List<ItemVenda> itens;
    private final String formaPagamento;

    public Venda(long id, Cliente cliente, List<ItemVenda> itens, String formaPagamento) {
        if (id <= 0) {
            throw new IllegalArgumentException("O ID deve ser maior que zero.");
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

    public long getId() {
        return id;
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

    public double calcularValorTotal() {
        double total = 0;

        for (ItemVenda item : itens) {
            total += item.getSubtotal();
        }

        return total;
    }
}
