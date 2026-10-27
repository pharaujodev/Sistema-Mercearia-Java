package model;

import java.math.BigDecimal;

public interface Pagamento {
    void processarPagamento(BigDecimal valor);
    String getDescricao();
}
