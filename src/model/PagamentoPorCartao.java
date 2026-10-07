package model;

public class PagamentoPorCartao implements Pagamento {

    @Override
    public void processarPagamento(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser maior que zero.");
        }

        System.out.printf("Pagamento via cartão aprovado. Valor: R$ %.2f%n", valor);
    }

    @Override
    public String getDescricao() {
        return "Cartão";
    }
}
