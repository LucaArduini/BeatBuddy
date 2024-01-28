package it.unipi.lsmd.BeatBuddy.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import it.unipi.lsmd.BeatBuddy.model.dummy.AdminStats;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Utility {

    public static boolean isLogged(HttpSession session) {
        Object username = session.getAttribute("username");
        return username != null;
    }

    public static boolean isAdmin(HttpSession session) {
        Object role = session.getAttribute("role");
        return role != null && role.equals("admin");
    }

    public static String getRole(HttpSession session){
        Object role = session.getAttribute("role");
        if(role != null)
            return role.toString();
        else
            return "guest";
    }

    public static String getUsername(HttpSession session){
        Object username = session.getAttribute("username");
        if(username != null)
            return username.toString();
        else
            return "guest"; //###
    }

    public static void writeToFile(Object data, String fileName, String folderName) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        
        // Abilita la formattazione "pretty print" per l'output JSON
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        String json = objectMapper.writeValueAsString(data);

        // Ottieni il percorso della directory corrente
        String currentDir = System.getProperty("user.dir");

        // Percorso della sottocartella
        String subFolderPath = currentDir + File.separator + folderName;

        // Crea la sottocartella se non esiste
        File subFolder = new File(subFolderPath);
        if (!subFolder.exists()) {
            subFolder.mkdirs();
        }

        // Costruisci il percorso completo del file
        Path filePath = Paths.get(subFolder.getPath() + File.separator + fileName);

        // Scrivi nel file
        Files.write(filePath, json.getBytes(), StandardOpenOption.CREATE);
    }

    public static void clearRankingsDirectory(String folderName) throws IOException {
        String currentDir = System.getProperty("user.dir");
        String subFolderPath = currentDir + File.separator + folderName;
        File subFolder = new File(subFolderPath);

        if (subFolder.exists() && subFolder.isDirectory()) {
            File[] files = subFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!file.delete()) {
                        throw new IOException("Failed to delete " + file.getAbsolutePath());
                    }
                }
            }
        }
    }

    public static void writeAdminStats(AdminStats adminStats) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        // Abilita la formattazione "pretty print" per l'output JSON
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        String json = objectMapper.writeValueAsString(adminStats);

        // Ottieni il percorso della directory corrente
        String currentDir = System.getProperty("user.dir");

        // Percorso della sottocartella
        String subFolderPath = currentDir + File.separator + Constants.folderName_AdminStats;

        // Crea la sottocartella se non esiste
        File subFolder = new File(subFolderPath);
        if (!subFolder.exists()) {
            subFolder.mkdirs();
        }

        // Costruisci il percorso completo del file
        Path filePath = Paths.get(subFolder.getPath() + File.separator + Constants.fileName_AdminStats);

        // Scrivi nel file
        Files.write(filePath, json.getBytes(), StandardOpenOption.CREATE);
    }

    public static AdminStats readAdminStats() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        // Ottieni il percorso della directory corrente
        String currentDir = System.getProperty("user.dir");

        // Percorso della sottocartella
        String subFolderPath = currentDir + File.separator + Constants.folderName_AdminStats;

        // Costruisci il percorso completo del file
        Path filePath = Paths.get(subFolderPath + File.separator + Constants.fileName_AdminStats);

        // Verifica se il file esiste
        if (!Files.exists(filePath)) {
            return null; // Il file non esiste, ritorna null
        }

        // Leggi il file
        String json = Files.readString(filePath);

        // Deserializza il file
        return objectMapper.readValue(json, AdminStats.class);
    }

}