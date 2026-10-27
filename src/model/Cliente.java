package model;

public class Cliente {
    private long id;
    private final String nome;
    private final String email;
    private final String telefone;
    private final String endereco;

    public Cliente(long id, String nome, String email, String telefone, String endereco) {
        if (id < 0) {
            throw new IllegalArgumentException("ID inválido. O ID não pode ser negativo.");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome inválido. O nome não pode ser nulo ou vazio.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email inválido. O email não pode ser nulo ou vazio.");
        }
        if (telefone == null || telefone.isBlank()) {
            throw new IllegalArgumentException("Telefone inválido. O telefone não pode ser nulo ou vazio.");
        }
        if (endereco == null || endereco.isBlank()) {
            throw new IllegalArgumentException("Endereço inválido. O endereço não pode ser nulo ou vazio.");
        }

        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    public Cliente(String nome, String email, String telefone, String endereco) {
        this(0, nome, email, telefone, endereco);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido. O ID deve ser maior que zero.");
        }
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    @Override
    public String toString() {
        return "Cliente [ID=" + id + ", Nome=" + nome + ", Email=" + email
                + ", Telefone=" + telefone + ", Endereço=" + endereco + "]";
    }
}
