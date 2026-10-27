package model;

import java.math.BigDecimal;

public class ItemVenda {
    private final Produto produto;
    private final BigDecimal quantidade;
    private final BigDecimal subtotal;

    public ItemVenda(Produto produto, BigDecimal quantidade) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto inválido. O produto não pode ser nulo.");
        }
        if (!produto.possuiEstoque(quantidade)) {
            throw new IllegalStateException(
                    "Estoque insuficiente para o produto " + produto.getNome() + "."
            );
        }

        this.produto = produto;
        this.quantidade = quantidade;
        this.subtotal = produto.calcularPrecoVenda(quantidade);
    }

    // Usado pelo DAO ao reconstruir uma venda já armazenada no banco.
    public ItemVenda(Produto produto, BigDecimal quantidade, BigDecimal subtotal) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto inválido. O produto não pode ser nulo.");
        }
        if (quantidade == null || quantidade.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        if (subtotal == null || subtotal.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O subtotal não pode ser negativo.");
        }

        this.produto = produto;
        this.quantidade = quantidade;
        this.subtotal = subtotal;
    }

    public Produto getProduto() {
        return produto;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    @Override
    public String toString() {
        return "ItemVenda [Produto=" + produto.getNome() + ", Qtd=" + quantidade + " "
                + produto.getUnidadeMedida() + ", Subtotal=R$ " + subtotal + "]";
    }
}
