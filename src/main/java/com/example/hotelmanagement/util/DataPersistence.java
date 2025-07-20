package com.example.hotelmanagement.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DataPersistence {

    private static final String DATA_DIR = "data";

    static {
        // Cria o diretório data se não existir
        try {
            Path dataPath = Paths.get(DATA_DIR);
            if (!Files.exists(dataPath)) {
                Files.createDirectories(dataPath);
            }
        } catch (IOException e) {
            System.err.println("Erro ao criar diretório de dados: " + e.getMessage());
        }
    }

    /**
     * Salva uma lista de objetos em arquivo .dat
     */
    public static <T> void saveToFile(List<T> data, String fileName) {
        String filePath = DATA_DIR + File.separator + fileName + ".dat";

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filePath))) {
            oos.writeObject(data);
            System.out.println("Dados salvos em: " + filePath);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados em " + filePath + ": " + e.getMessage());
        }
    }

    /**
     * Carrega uma lista de objetos de arquivo .dat
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> loadFromFile(String fileName) {
        String filePath = DATA_DIR + File.separator + fileName + ".dat";

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filePath))) {
            return (List<T>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo " + filePath + " não encontrado. Retornando lista vazia.");
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar dados de " + filePath + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Verifica se um arquivo existe
     */
    public static boolean fileExists(String fileName) {
        String filePath = DATA_DIR + File.separator + fileName + ".dat";
        return Files.exists(Paths.get(filePath));
    }
}