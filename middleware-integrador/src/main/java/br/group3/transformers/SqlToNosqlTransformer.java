package br.group3.transformers;

import com.google.gson.Gson;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class SqlToNosqlTransformer {
    
    // Informações de conexão com o MongoDB (atualizadas)
    // Formato: mongodb://<usuario>:<senha>@<host>:<porta>/
    private static final String MONGO_URI = "mongodb://admin:1234@localhost:27017/";
    private static final String DB_NAME = "mongodb"; // <== ATUALIZADO
    
    // Você ainda precisa confirmar o nome da coleção com a equipe!
    private static final String COLLECTION_NAME = "pacientes"; // Exemplo, confirme o nome real

    public static void transformarEEnviar(String dadosJsonSql) {
        // Usa um try-with-resources para garantir que a conexão com o Mongo será fechada
        try (MongoClient mongoClient = MongoClients.create(MONGO_URI)) {
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            Document doc = Document.parse(dadosJsonSql);

            collection.insertOne(doc);
            System.out.println("Dados inseridos na coleção '" + COLLECTION_NAME + "' com o id: " + doc.getObjectId("_id"));

        } catch (Exception e) {
            System.err.println("Erro ao inserir dados no MongoDB: " + e.getMessage());
            e.printStackTrace(); // Ajuda a depurar
        }
    }
}