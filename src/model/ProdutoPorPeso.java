package model;

public class ProdutoPorPeso extends Produto {

    public ProdutoPorPeso(long id, String nome, double precoPorKg, double estoqueEmKg) {
        super(id, nome, precoPorKg, estoqueEmKg);
    }

    @Override
    public double calcularPrecoVenda(double quantidadeEmKg) {
        validarQuantidade(quantidadeEmKg);
        return getPreco() * quantidadeEmKg;
    }

    @Override
    public String getUnidadeMedida() {
        return "kg";
    }
}
