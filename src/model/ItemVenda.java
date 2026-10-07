package model;

public class ItemVenda {
    private final Produto produto;
    private final double quantidade;
    private final double subtotal;

    public ItemVenda(Produto produto, double quantidade) {
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

    public Produto getProduto() {
        return produto;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public double getSubtotal() {
        return subtotal;
    }
}
