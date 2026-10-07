package repository;

import model.Venda;
import java.util.ArrayList;
import java.util.List;

public class VendaRepositoryMemoria implements VendaRepository {
    private final List<Venda> vendas = new ArrayList<>();

    @Override
    public void salvar(Venda venda) {
        if (venda == null) {
            throw new IllegalArgumentException("A venda não pode ser nula.");
        }

        vendas.add(venda);
    }

    @Override
    public Venda buscarPorId(long id) {
        for (Venda venda : vendas) {
            if (venda.getId() == id) {
                return venda;
            }
        }

        return null;
    }

    @Override
    public List<Venda> listarTodos() {
        return new ArrayList<>(vendas);
    }
}
