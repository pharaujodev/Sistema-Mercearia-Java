package model;

import java.math.BigDecimal;

public class PagamentoPorDinheiro implements Pagamento {
    private final BigDecimal valorRecebido;

    public PagamentoPorDinheiro(BigDecimal valorRecebido) {
        if (valorRecebido == null || valorRecebido.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor recebido deve ser maior que zero.");
        }
        this.valorRecebido = valorRecebido;
    }

    @Override
    public void processarPagamento(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser maior que zero.");
        }
        if (valorRecebido.compareTo(valor) < 0) {
            throw new IllegalStateException(
                    String.format("Valor recebido insuficiente. Faltam R$ %.2f", valor.subtract(valorRecebido))
            );
        }

        BigDecimal troco = valorRecebido.subtract(valor);
        System.out.printf("Pagamento em dinheiro aprovado. Troco: R$ %.2f%n", troco);
    }

    @Override
    public String getDescricao() {
        return "Dinheiro";
    }
}
