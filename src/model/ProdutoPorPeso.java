package model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ProdutoPorPeso extends Produto {

    public ProdutoPorPeso(long id, String nome, BigDecimal precoPorKg, BigDecimal estoqueEmKg) {
        super(id, nome, precoPorKg, estoqueEmKg);
    }

    public ProdutoPorPeso(String nome, BigDecimal precoPorKg, BigDecimal estoqueEmKg) {
        this(0, nome, precoPorKg, estoqueEmKg);
    }

    @Override
    public BigDecimal calcularPrecoVenda(BigDecimal quantidadeEmKg) {
        validarQuantidade(quantidadeEmKg);
        // Preço por kg × kg, arredondado para centavos.
        return getPreco().multiply(quantidadeEmKg).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String getUnidadeMedida() {
        return "kg";
    }
}
