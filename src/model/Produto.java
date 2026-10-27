package model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class Produto {
    private long id;
    private final String nome;
    private final BigDecimal preco; 
    private BigDecimal quantidadeEstoque;

    public Produto(long id, String nome, BigDecimal preco, BigDecimal quantidadeEstoque) {
        if (id < 0) {
            throw new IllegalArgumentException("ID inválido. O ID não pode ser negativo.");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome inválido. O nome não pode ser nulo ou vazio.");
        }
        if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço inválido. O preço deve ser maior que zero.");
        }
        if (quantidadeEstoque == null || quantidadeEstoque.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Estoque inválido. A quantidade não pode ser negativa.");
        }

        this.id = id;
        this.nome = nome;
        this.preco = preco.setScale(2, RoundingMode.HALF_UP);
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public Produto(String nome, BigDecimal preco, BigDecimal quantidadeEstoque) {
        this(0, nome, preco, quantidadeEstoque);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido. O ID deve ser maior que zero.");
        }
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public BigDecimal getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void adicionarEstoque(BigDecimal quantidade) {
        validarQuantidade(quantidade);
        quantidadeEstoque = quantidadeEstoque.add(quantidade);
    }

    public void removerEstoque(BigDecimal quantidade) {
        validarQuantidade(quantidade);

        if (quantidade.compareTo(quantidadeEstoque) > 0) {
            throw new IllegalStateException(
                    "Estoque insuficiente para o produto " + nome
                            + ". Disponível: " + quantidadeEstoque + " " + getUnidadeMedida()
            );
        }

        quantidadeEstoque = quantidadeEstoque.subtract(quantidade);
    }

    public boolean possuiEstoque(BigDecimal quantidade) {
        validarQuantidade(quantidade);
        return quantidade.compareTo(quantidadeEstoque) <= 0;
    }

    protected void validarQuantidade(BigDecimal quantidade) {
        if (quantidade == null || quantidade.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
    }

    public abstract BigDecimal calcularPrecoVenda(BigDecimal quantidade);

    public abstract String getUnidadeMedida();

    @Override
    public String toString() {
        return "Produto [ID=" + id + ", Nome=" + nome + ", Preço=R$ " + preco + "/" + getUnidadeMedida()
                + ", Estoque=" + quantidadeEstoque + " " + getUnidadeMedida() + "]";
    }
}
