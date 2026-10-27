package model;

import java.math.BigDecimal;

public class PagamentoPorCartao implements Pagamento {

    @Override
    public void processarPagamento(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser maior que zero.");
        }

        System.out.printf("Pagamento via cartão aprovado. Valor: R$ %.2f%n", valor);
    }

    @Override
    public String getDescricao() {
        return "Cartão";
    }
}
