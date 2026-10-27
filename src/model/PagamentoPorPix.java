package model;

import java.math.BigDecimal;

public class PagamentoPorPix implements Pagamento {

    @Override
    public void processarPagamento(BigDecimal valor) {
        validarValor(valor);
        System.out.printf("Pagamento via PIX aprovado. Valor: R$ %.2f%n", valor);
    }

    @Override
    public String getDescricao() {
        return "PIX";
    }

    private void validarValor(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser maior que zero.");
        }
    }
}
