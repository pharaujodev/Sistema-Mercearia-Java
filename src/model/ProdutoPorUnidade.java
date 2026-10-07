package model;

public class ProdutoPorUnidade extends Produto {

    public ProdutoPorUnidade(long id, String nome, double preco, double quantidadeEstoque) {
        super(id, nome, preco, quantidadeEstoque);
        validarQuantidadeInteira(quantidadeEstoque, true);
    }

    @Override
    protected void validarQuantidade(double quantidade) {
        super.validarQuantidade(quantidade);
        validarQuantidadeInteira(quantidade, false);
    }

    private void validarQuantidadeInteira(double quantidade, boolean permitirZero) {
        if (!permitirZero && quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        if (quantidade != Math.floor(quantidade)) {
            throw new IllegalArgumentException(
                    "Produtos por unidade devem usar quantidades inteiras."
            );
        }
    }

    @Override
    public double calcularPrecoVenda(double quantidade) {
        validarQuantidade(quantidade);
        return getPreco() * quantidade;
    }

    @Override
    public String getUnidadeMedida() {
        return "un";
    }
}
