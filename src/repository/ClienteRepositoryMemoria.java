package repository;

import model.Cliente;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepositoryMemoria implements ClienteRepository {
    private final List<Cliente> clientes = new ArrayList<>();

    @Override
    public void salvar(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("O cliente não pode ser nulo.");
        }

        boolean atualizado = false;

        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getId() == cliente.getId()) {
                clientes.set(i, cliente);
                atualizado = true;
            }
        }

        if (!atualizado) {
            clientes.add(cliente);
        }
    }

    @Override
    public Cliente buscarPorId(long id) {
        for (Cliente cliente : clientes) {
            if (cliente.getId() == id) {
                return cliente;
            }
        }

        return null;
    }

    @Override
    public List<Cliente> listarTodos() {
        return new ArrayList<>(clientes);
    }

    @Override
    public void remover(long id) {
        Cliente clienteEncontrado = null;

        for (Cliente cliente : clientes) {
            if (cliente.getId() == id) {
                clienteEncontrado = cliente;
            }
        }

        if (clienteEncontrado == null) {
            throw new IllegalArgumentException("Cliente não encontrado.");
        }

        clientes.remove(clienteEncontrado);
    }
}
