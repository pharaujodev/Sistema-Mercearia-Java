package repository;

import model.Produto;
import java.util.ArrayList;
import java.util.List;

public class ProdutoRepositoryMemoria implements ProdutoRepository {
    private final List<Produto> produtos = new ArrayList<>();

    @Override
    public void salvar(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("O produto não pode ser nulo.");
        }

        boolean atualizado = false;

        for (int i = 0; i < produtos.size(); i++) {
            if (produtos.get(i).getId() == produto.getId()) {
                produtos.set(i, produto);
                atualizado = true;
            }
        }

        if (!atualizado) {
            produtos.add(produto);
        }
    }

    @Override
    public Produto buscarPorId(long id) {
        for (Produto produto : produtos) {
            if (produto.getId() == id) {
                return produto;
            }
        }

        return null;
    }

    @Override
    public List<Produto> listarTodos() {
        return new ArrayList<>(produtos);
    }

    @Override
    public void remover(long id) {
        Produto produtoEncontrado = null;

        for (Produto produto : produtos) {
            if (produto.getId() == id) {
                produtoEncontrado = produto;
            }
        }

        if (produtoEncontrado == null) {
            throw new IllegalArgumentException("Produto não encontrado.");
        }

        produtos.remove(produtoEncontrado);
    }
}
