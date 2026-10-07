package model;

public class PagamentoPorDinheiro implements Pagamento {
    private final double valorRecebido;

    public PagamentoPorDinheiro(double valorRecebido) {
        if (valorRecebido <= 0) {
            throw new IllegalArgumentException("O valor recebido deve ser maior que zero.");
        }
        this.valorRecebido = valorRecebido;
    }

    @Override
    public void processarPagamento(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser maior que zero.");
        }
        if (valorRecebido < valor) {
            throw new IllegalStateException(
                    String.format("Valor recebido insuficiente. Faltam R$ %.2f", valor - valorRecebido)
            );
        }

        double troco = valorRecebido - valor;
        System.out.printf("Pagamento em dinheiro aprovado. Troco: R$ %.2f%n", troco);
    }

    @Override
    public String getDescricao() {
        return "Dinheiro";
    }
}
