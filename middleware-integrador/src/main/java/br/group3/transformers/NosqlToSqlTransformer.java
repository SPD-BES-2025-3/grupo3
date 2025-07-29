package br.group3.transformers;

import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class NosqlToSqlTransformer {

    // Endereço da API do Sistema 1 (Spring Boot), agora com a porta correta.
    // Você ainda precisa confirmar o caminho exato do endpoint (ex: /pacientes, /dados, etc.)
    private static final String API_ENDPOINT = "http://localhost:8083/api/pacientes"; // <== ATUALIZADO (confirme o caminho)

    public static void transformarEEnviar(String dadosJsonNoSql) {
        Gson gson = new Gson();
        Map<String, Object> dadosNosql = gson.fromJson(dadosJsonNoSql, Map.class);
        
        // A lógica de mapeamento aqui é um exemplo.
        // Você deve ajustá-la para corresponder aos campos reais do seu modelo.
        Map<String, Object> payloadSql = Map.of(
            "nomeCompleto", dadosNosql.getOrDefault("nome", ""),
            "dataNascimento", dadosNosql.getOrDefault("nascimento", ""),
            "idExterno", dadosNosql.getOrDefault("_id", "")
        );

        String requestBody = gson.toJson(payloadSql);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_ENDPOINT))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody)) // Ou .PUT(...)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Dados enviados para a API. Status: " + response.statusCode());
        } catch (Exception e) {
            System.err.println("Erro ao enviar dados para a API: " + e.getMessage());
        }
    }
}