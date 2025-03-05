import java.util.*;
import java.io.*;

public class Inventario {
    private Map<String, Double> itens;
    private double pesoLimite;
    private double pesoTotal;

    public Inventario(double pesoLimite) {
        this.itens = new HashMap<>();
        this.pesoLimite = pesoLimite;
        this.pesoTotal = 0.0;
        carregarInventario();
    }

    public void adicionarItem(String nome, double peso) {
        if (pesoTotal + peso <= pesoLimite) {
            itens.put(nome, peso);
            pesoTotal += peso;
            salvarInventario();
        } else {
            System.out.println("Peso limite excedido!");
        }
    }

    public void excluirItem(String nome) {
        if (itens.containsKey(nome)) {
            pesoTotal -= itens.get(nome);
            itens.remove(nome);
            salvarInventario();
        } else {
            System.out.println("Item não encontrado.");
        }
    }

    public void mostrarInventario() {
        if (itens.isEmpty()) {
            System.out.println("Seu inventário está vazio.");
        } else {
            for (Map.Entry<String, Double> entry : itens.entrySet()) {
                System.out.println("Item: " + entry.getKey() + " | Peso: " + entry.getValue() + "kg");
            }
        }
    }

    public void setPesoLimite(double novoLimite) {
        if (novoLimite >= pesoTotal) {
            this.pesoLimite = novoLimite;
            System.out.println("Peso limite alterado para: " + novoLimite);
        } else {
            System.out.println("Não é possível definir um limite menor que o peso atual do inventário.");
        }
    }

    private void salvarInventario() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("inventario.dat"))) {
            out.writeObject(itens);
            out.writeObject(pesoTotal);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void carregarInventario() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("inventario.dat"))) {
            itens = (Map<String, Double>) in.readObject();
            pesoTotal = (double) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Nenhum inventário salvo encontrado.");
        }
    }

    public double getPesoTotal() {
        return pesoTotal;
    }

    public double getPesoLimite() {
        return pesoLimite;
    }
}
