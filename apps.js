function sendMessage() {
    const input = document.getElementById('user-input');
    const message = input.value.trim();
    if (message === "") {
        return; // Não faz nada se o campo estiver vazio
    }
    input.value = "";

    const chatBox = document.getElementById('chat-box');
    
    // Exibe a mensagem do usuário na interface
    const userMessage = document.createElement('div');
    userMessage.classList.add('message');
    userMessage.textContent = "Você: " + message;
    chatBox.appendChild(userMessage);

    // Enviar o comando para o servidor
    fetch('/InventarioServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({
            command: message // Envia o comando digitado pelo usuário
        })
    })
    .then(response => response.json())
    .then(data => {
        const serverMessage = document.createElement('div');
        serverMessage.classList.add('message');
        serverMessage.textContent = "Servidor: " + data.message;

        // Exibe a resposta do servidor
        chatBox.appendChild(serverMessage);

        // Exibe inventário se o comando "show" for chamado
        if (data.inventario) {
            const inventarioMessage = document.createElement('div');
            inventarioMessage.classList.add('message');
            inventarioMessage.textContent = data.inventario;
            chatBox.appendChild(inventarioMessage);
        }

        // Rola para a última mensagem
        chatBox.scrollTop = chatBox.scrollHeight;
    })
    .catch(error => {
        console.error('Erro:', error);
        const serverMessage = document.createElement('div');
        serverMessage.classList.add('message');
        serverMessage.textContent = "Erro na comunicação com o servidor.";
        chatBox.appendChild(serverMessage);
        chatBox.scrollTop = chatBox.scrollHeight;
    });
}

// Impede o envio de mensagens ao pressionar Enter
document.getElementById('user-input').addEventListener('keypress', function(e) {
    if (e.key === 'Enter') {
        sendMessage();
    }
});
