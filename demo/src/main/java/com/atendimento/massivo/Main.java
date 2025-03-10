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
import java.util.ArrayList;
import java.util.Date;

public class Main {

    public static void main(String[] args) {
        // Caminhos das pastas
        Path pastaEntrada = Paths.get("C:/java/fstp/entrada");
        Path pastaProcessados = Paths.get("C:/java/fstp/processados");
        Path pastaSaida = Paths.get("C:/java/fstp/saida");

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
            File arquivo = new File("C:/java/fstp/entrada/model_20250309-3.csv");
            File arquivoProcessados = new File("C:/java/fstp/processados/model_20250309-3.csv");

            Path arquivoCSV = Paths.get(pastaEntrada.toString(), arquivo.getName());

            String nomeCsv = arquivoCSV.toString();

            if (nomeCsv.matches("^.*model_\\d{8}-\\d+\\.csv$")) {
                System.out.println("Nome do arquivo válido.");
            } else {
                System.out.println("Nome do arquivo inválido.");
                throw new IllegalArgumentException("Nome do arquivo inválido.");
            }

            // Lê o arquivo CSV e processa
            if (Files.exists(arquivoCSV)) {
                System.out.println("Lendo arquivo CSV...");
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

                Path arquivoProcessado = pastaProcessados.resolve(arquivoCSV.getFileName());
                Files.move(arquivoCSV, arquivoProcessado, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Arquivo movido para 'processados'.");

            

        try {
            // Abrindo o arquivo CSV para leitura
            CSVReader reader = new CSVReader(new FileReader(arquivoProcessados));
            List<String[]> rows = reader.readAll();
            reader.close();

            // Preparando para escrever no arquivo CSV de saída
            CSVWriter writer = new CSVWriter(new FileWriter(arquivoProcessados));

            String[] header = new String[] {"id", "nome", "idade", "status", "mensagem"};

            writer.writeNext(header);

            for (String[] row : rows) {
                if (row.length == 3) {
                    String id = row[0];
                    String nome = row[1];
                    String idade = row[2];

                    String status = "ativo"; 
                    String mensagem = "Cadastro válido";

                    String[] newRow = {id, nome, idade, status, mensagem};
                    writer.writeNext(newRow);
                }
            }

            writer.close();
            System.out.println("Arquivo CSV modificado com sucesso!");

        } catch (IOException e) {
            e.printStackTrace();
        }

                Path arquivoSaida = pastaSaida.resolve(arquivoCSV.getFileName());
                Files.move(arquivoProcessado, arquivoSaida, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Arquivo movido para 'saida'.");
            } else {
                System.out.println("Arquivo 'model.csv' não encontrado na pasta 'entrada'.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}