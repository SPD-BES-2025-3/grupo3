package br.group3.transformers;

import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class NosqlToSqlTransformer {

    // URL final e confirmada com base no PacienteController.java
    private static final String API_PACIENTE_ENDPOINT = "http://localhost:8083/api/pacientes"; //

    public static void transformarEEnviar(String dadosJsonNoSql) {
        Gson gson = new Gson();
        Map<String, Object> dadosNosql = gson.fromJson(dadosJsonNoSql, Map.class);
        
        // Mapeamento final dos campos com base no PacienteDTO.java
        // Supondo que os nomes dos campos no evento NoSQL sejam os mesmos.
        Map<String, Object> payloadSql = Map.of(
            "nomeCompleto", dadosNosql.get("nomeCompleto"),
            "dataNascimento", dadosNosql.get("dataNascimento"),
            "cpf", dadosNosql.get("cpf"),
            "endereco", dadosNosql.get("endereco"),
            "telefone", dadosNosql.get("telefone")
        );

        String requestBody = gson.toJson(payloadSql);
        
        System.out.println("Enviando para a API Spring Boot: " + requestBody);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_PACIENTE_ENDPOINT))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("API respondeu com Status: " + response.statusCode());
        } catch (Exception e) {
            System.err.println("Erro ao enviar dados para a API: " + e.getMessage());
        }
    }
}