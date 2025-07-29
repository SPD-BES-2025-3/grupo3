package br.group3;

import br.group3.transformers.NosqlToSqlTransformer;
import br.group3.transformers.SqlToNosqlTransformer;
import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class Main {

    // Defina os nomes dos canais aqui
    private static final String CANAL_SISTEMA_1 = "eventos:sistema1";
    private static final String CANAL_SISTEMA_2 = "eventos:sistema2";

    public static void main(String[] args) {
        // Conexão com o Redis (ajuste host e port se necessário)
        Jedis jedis = new Jedis("localhost", 6379);
        System.out.println("🚀 Middleware Integrador em Java iniciado. Ouvindo eventos...");

        JedisPubSub jedisPubSub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                System.out.println("Recebido do canal '" + channel + "': " + message);

                // Lógica de Roteamento
                if (channel.equals(CANAL_SISTEMA_1)) {
                    // Evento do Sistema 1 (NoSQL) -> Transformar para SQL
                    System.out.println("Iniciando transformação NoSQL -> SQL");
                    NosqlToSqlTransformer.transformarEEnviar(message);
                } else if (channel.equals(CANAL_SISTEMA_2)) {
                    // Evento do Sistema 2 (SQL) -> Transformar para NoSQL
                    System.out.println("Iniciando transformação SQL -> NoSQL");
                    SqlToNosqlTransformer.transformarEEnviar(message);
                }
            }
        };

        // A chamada subscribe é bloqueante, ela vai manter o programa rodando
        jedis.subscribe(jedisPubSub, CANAL_SISTEMA_1, CANAL_SISTEMA_2);
    }
}