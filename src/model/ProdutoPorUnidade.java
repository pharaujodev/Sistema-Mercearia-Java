package model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ProdutoPorUnidade extends Produto {

    public ProdutoPorUnidade(long id, String nome, BigDecimal preco, BigDecimal quantidadeEstoque) {
        super(id, nome, preco, quantidadeEstoque);
        validarQuantidadeInteira(quantidadeEstoque);
    }

    public ProdutoPorUnidade(String nome, BigDecimal preco, BigDecimal quantidadeEstoque) {
        this(0, nome, preco, quantidadeEstoque);
    }

    @Override
    protected void validarQuantidade(BigDecimal quantidade) {
        super.validarQuantidade(quantidade);
        validarQuantidadeInteira(quantidade);
    }

    private void validarQuantidadeInteira(BigDecimal quantidade) {
        // Ex.: 3.00 tem parte decimal zero (ok); 2.5 não (erro).
        if (quantidade.stripTrailingZeros().scale() > 0) {
            throw new IllegalArgumentException(
                    "Produtos por unidade devem usar quantidades inteiras."
            );
        }
    }

    @Override
    public BigDecimal calcularPrecoVenda(BigDecimal quantidade) {
        validarQuantidade(quantidade);
        return getPreco().multiply(quantidade).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String getUnidadeMedida() {
        return "un";
    }
}
