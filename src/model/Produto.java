package model;

public abstract class Produto {
    private final long id;
    private final String nome;
    private final double preco;
    private double quantidadeEstoque;

    public Produto(long id, String nome, double preco, double quantidadeEstoque) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido. O ID deve ser um número positivo.");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome inválido. O nome não pode ser nulo ou vazio.");
        }
        if (preco <= 0) {
            throw new IllegalArgumentException("Preço inválido. O preço deve ser maior que zero.");
        }
        if (quantidadeEstoque < 0) {
            throw new IllegalArgumentException("Estoque inválido. A quantidade não pode ser negativa.");
        }

        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public double getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void adicionarEstoque(double quantidade) {
        validarQuantidade(quantidade);
        quantidadeEstoque += quantidade;
    }

    public void removerEstoque(double quantidade) {
        validarQuantidade(quantidade);

        if (quantidade > quantidadeEstoque) {
            throw new IllegalStateException(
                    "Estoque insuficiente para o produto " + nome
                            + ". Disponível: " + quantidadeEstoque + " " + getUnidadeMedida()
            );
        }

        quantidadeEstoque -= quantidade;
    }

    public boolean possuiEstoque(double quantidade) {
        validarQuantidade(quantidade);
        return quantidade <= quantidadeEstoque;
    }

    protected void validarQuantidade(double quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
    }

    public abstract double calcularPrecoVenda(double quantidade);

    public abstract String getUnidadeMedida();
}
