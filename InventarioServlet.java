import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import org.json.JSONObject;

public class InventarioServlet extends HttpServlet {
    private Inventario inventario;

    @Override
    public void init() throws ServletException {
        // Inicializa o inventário com o peso limite inicial de 50 kg
        inventario = new Inventario(50.0);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String command = request.getParameter("command");
        JSONObject responseJson = new JSONObject();
        StringBuilder inventarioStr = new StringBuilder();

        if (command != null) {
            if (command.startsWith("set limit")) {
                // Exemplo de comando: set limit 60
                String[] commandParts = command.split(" ");
                if (commandParts.length == 3) {
                    try {
                        double novoLimite = Double.parseDouble(commandParts[2]);
                        inventario.setPesoLimite(novoLimite);
                        responseJson.put("message", "Peso limite atualizado para: " + novoLimite);
                    } catch (NumberFormatException e) {
                        responseJson.put("message", "Por favor, insira um valor válido para o limite.");
                    }
                } else {
                    responseJson.put("message", "Comando inválido. Use: set limit [peso]");
                }
            } else {
                switch (command) {
                    case "show":
                        inventario.mostrarInventario();
                        responseJson.put("message", "Inventário mostrado.");
                        inventarioStr.append(inventario);
                        break;
                    case "add":
                        String nomeItem = request.getParameter("nome");
                        double pesoItem = Double.parseDouble(request.getParameter("peso"));
                        inventario.adicionarItem(nomeItem, pesoItem);
                        responseJson.put("message", "Item adicionado com sucesso!");
                        break;
                    case "remove":
                        nomeItem = request.getParameter("nome");
                        inventario.excluirItem(nomeItem);
                        responseJson.put("message", "Item removido com sucesso!");
                        break;
                    default:
                        responseJson.put("message", "Comando desconhecido.");
                        break;
                }
            }
        }

        responseJson.put("inventario", inventarioStr.toString());
        response.setContentType("application/json");
        response.getWriter().write(responseJson.toString());
    }
}
