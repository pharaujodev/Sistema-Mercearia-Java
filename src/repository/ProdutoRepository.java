package repository;

import model.Produto;
import java.util.List;

public interface ProdutoRepository {
    void salvar(Produto produto);
    Produto buscarPorId(long id);
    List<Produto> listarTodos();
    void remover(long id);
}
