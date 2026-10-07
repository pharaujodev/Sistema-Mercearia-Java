package repository;

import model.Venda;
import java.util.List;

public interface VendaRepository {
    void salvar(Venda venda);
    Venda buscarPorId(long id);
    List<Venda> listarTodos();
}
