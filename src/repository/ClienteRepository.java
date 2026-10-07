package repository;

import model.Cliente;
import java.util.List;

public interface ClienteRepository {
    void salvar(Cliente cliente);
    Cliente buscarPorId(long id);
    List<Cliente> listarTodos();
    void remover(long id);
}
