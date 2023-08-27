package com.ryanrhee;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Path csvPath = Paths.get("pcspecs.csv");
        List<String[]> lines = readCsvFile(csvPath); //read the csv file into a list of values

        PcPartFactory partFactory = new PcPartFactory();
        ArrayList<PcPart> parts = new ArrayList<>();

        //skip the first line because that's a description row
        for (int i = 1; i < lines.size(); i++) {
            String[] line = lines.get(i);
            PcPart part = partFactory.getPart(line);
            parts.add(part);
        }
    }

    public static List<String[]> readCsvFile(Path csvPath) {
        try (Reader fileReader = Files.newBufferedReader(csvPath)) {
            try (CSVReader csvReader = new CSVReader(fileReader)) {
                return csvReader.readAll();
            }
        }
        catch (IOException ioException) {
            System.out.println("Failed to read file " + csvPath + ": " + ioException.getMessage());
            System.exit(1);
            return null;
        }
    }
}
