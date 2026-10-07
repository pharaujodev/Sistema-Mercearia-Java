package model;

public class PagamentoPorPix implements Pagamento {

    @Override
    public void processarPagamento(double valor) {
        validarValor(valor);
        System.out.printf("Pagamento via PIX aprovado. Valor: R$ %.2f%n", valor);
    }

    @Override
    public String getDescricao() {
        return "PIX";
    }

    private void validarValor(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser maior que zero.");
        }
    }
}
