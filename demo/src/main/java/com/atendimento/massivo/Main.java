package com.atendimento.massivo;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;

import java.util.ArrayList;
import java.util.Date;

public class Main {

    public static void main(String[] args) {

        // Caminhos das pastas
        Path pastaEntrada = Paths.get("C:/java/fstp/entrada/");
        Path pastaProcessados = Paths.get("C:/java/fstp/processados/");
        Path pastaSaida = Paths.get("C:/java/fstp/saida/");

        // Verifica se as pastas existem, caso contrário cria
        try {
            if (!Files.exists(pastaEntrada)) {
                Files.createDirectories(pastaEntrada);
            }
            if (!Files.exists(pastaProcessados)) {
                Files.createDirectories(pastaProcessados);
            }
            if (!Files.exists(pastaSaida)) {
                Files.createDirectories(pastaSaida);
            }
            File file = new File(pastaEntrada.toString());
            File[] arquivos = file.listFiles();
          
            if (arquivos != null) {
                for (File arquivo : arquivos) {
                    if (arquivo.isFile() && arquivo.getName().matches("^.*model_\\d{8}-\\d+\\.csv$")) {
                        System.out.println("Arquivo CSV encontrado: " + arquivo.getName());
                        
                        try {
                                long lastModified = arquivo.lastModified();
                                LocalDate lastModifiedDate = new Date(lastModified).toInstant()
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate();
                
                                // Obtendo a data de hoje
                                LocalDate today = LocalDate.now();
                                // Lê o arquivo CSV
                                if (lastModifiedDate.equals(today)) {
                                    System.out.println("Arquivo recente encontrado.");
                                } else {
                                    System.out.println("Arquivo inválido.");
                                    throw new IllegalArgumentException("Arquivo inválido.");
                                }
                                try (BufferedReader br = new BufferedReader(
                                        new InputStreamReader(new FileInputStream(arquivo), StandardCharsets.UTF_8))) {
                                    String linha;
                                    while ((linha = br.readLine()) != null) {
                                        System.out.println(linha);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Path arquivoCSV = Paths.get(pastaEntrada.toString(), arquivo.getName());

                                Path arquivoProcessado = pastaProcessados.resolve(arquivoCSV.getFileName());
                                Files.move(arquivoCSV, arquivoProcessado, StandardCopyOption.REPLACE_EXISTING);
                                System.out.println("Arquivo movido para 'processados'.");
                            System.out.println("Processando o arquivo CSV...");
                            
                            File sss = new File(pastaProcessados+"/"+arquivoProcessado.getFileName().toString());                    
                                CSVReader reader = new CSVReader(new FileReader(sss));
                                List<String[]> rows = reader.readAll();
                                reader.close();
                    
                                CSVWriter writer = new CSVWriter(new FileWriter(sss));
                    
                                String[] header = new String[] {"id", "nome", "idade", "status", "mensagem"};
                    
                                writer.writeNext(header);
                    
                                for (int i = 1; i < rows.size(); i++) { // Começa da segunda linha
                                    String[] row = rows.get(i); // Obtém a linha
                                    String[] valores = row[0].split(";");
                                        String id = valores[0];
                                        String nome = valores[1];
                                        String idade = valores[2];
                    
                                        String status = "ativo"; 
                                        String mensagem = "Cadastro válido";
                    
                                        String[] outputRow = {id, nome, idade, status, mensagem};
                                        writer.writeNext(outputRow);
                                }
                    
                                writer.close();
                                System.out.println("Arquivo CSV modificado com sucesso!");
            
                                Path arquivoCSVSaida = Paths.get(pastaSaida.toString(), arquivo.getName());

                                    Path arquivoSaida = pastaSaida.resolve(arquivoCSVSaida);
                                    Files.move(arquivoProcessado, arquivoSaida, StandardCopyOption.REPLACE_EXISTING);
                                    System.out.println("Arquivo movido para 'saida'.");
                               
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
            } else {
                System.out.println("A pasta não contém arquivos validos");
        } 
        
    }

} 
    } catch (Exception e) {
        System.out.println("LAST");
        e.printStackTrace();
    }
}
}
