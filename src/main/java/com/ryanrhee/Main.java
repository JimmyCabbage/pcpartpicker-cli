package com.ryanrhee;

import com.opencsv.CSVReader;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Command(name = "pcpartpicker-cli", mixinStandardHelpOptions = true, version = "pcpartpicker-cli 0.1")
public class Main implements Runnable {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    @Option(names = { "-c", "--csv" }, description = "Set the CSV file that this program reads PC Parts from.")
    String csvFilename = "pcspecs.csv";

    @Override
    public void run() {
        //read our pc parts from our csv
        Path csvPath = Paths.get(csvFilename);
        List<PcPart> pcParts = readPcParts(csvPath);

        //read user input to determine their preferences
        int userBudget = getUserBudget();
        PcWorkType userWorkType = getUserWorkType();
        boolean userSmallPreference = getUserSmallPreference();
        int userStorageSpace = getUserStorageSpace();
        boolean userFastFileSpeed = getUserFastFileSpeed();

        //pick out some parts based on preferences
        PcPartPicker pcPartPicker = new PcPartPicker(pcParts);
        List<PcPart> chosenParts = pcPartPicker.pickParts(userBudget, userWorkType,
                                                          userSmallPreference, userStorageSpace,
                                                          userFastFileSpeed);

        //we couldn't find a combo of parts that matched the given requirements
        if (chosenParts == null) {
            System.out.println("Couldn't build a PC that met the given requirements!");
            System.exit(2);
        }

        System.out.print("\nChosen Parts:\n==========================\n");

        //print out the parts we chose
        printOutPartType(chosenParts, PcPartType.CPU);
        printOutPartType(chosenParts, PcPartType.MOTHERBOARD);
        printOutPartType(chosenParts, PcPartType.GPU);
        printOutPartType(chosenParts, PcPartType.PSU);
        printOutPartType(chosenParts, PcPartType.RAM);
        printOutPartType(chosenParts, PcPartType.CASE);
        printOutPartType(chosenParts, PcPartType.STORAGE);

        System.exit(0);
    }

    private List<PcPart> readPcParts(Path csvPath) {
        List<String[]> lines = readCsvFile(csvPath); //read the csv file into a list of values

        PcPartFactory partFactory = new PcPartFactory();
        ArrayList<PcPart> parts = new ArrayList<>();

        //skip the first line because that's a description row
        for (int i = 1; i < lines.size(); i++) {
            String[] line = lines.get(i);
            PcPart part = partFactory.getPart(line);
            parts.add(part);
        }

        return parts;
    }

    private List<String[]> readCsvFile(Path csvPath) {
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

    private int getUserBudget() {
        int budget;
        while (true) {
            try {
                System.out.print("Please enter your desired budget ($): ");

                Scanner inScanner = new Scanner(System.in);
                budget = inScanner.nextInt();
                if (budget <= 300) {
                    throw new IllegalArgumentException("You need at least $300");
                }

                break;
            } catch (InputMismatchException | IllegalArgumentException e) {
                System.out.println("Invalid input (" + e.getMessage() + ") please try again");
            }
        }

        return budget;
    }

    private PcWorkType getUserWorkType() {
        PcWorkType workType;

        StringJoiner commaJoiner = new StringJoiner(",");
        for (PcWorkType availableType : PcWorkType.values()) {
            commaJoiner.add(availableType.name());
        }
        String availableTypes = commaJoiner.toString();

        while (true) {
            try {
                System.out.printf("Please enter the work you want your computer to do (%s): ", availableTypes);

                Scanner inScanner = new Scanner(System.in);
                workType = PcWorkType.valueOf(inScanner.next().toUpperCase());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input, please try again");
            }
        }

        return workType;
    }

    private boolean getUserSmallPreference() {
        boolean smallPreference;

        System.out.print("Do you prefer a small computer (y/n)?: ");

        Scanner inScanner = new Scanner(System.in);
        smallPreference = inScanner.next().equalsIgnoreCase("Y");

        return smallPreference;
    }

    private int getUserStorageSpace() {
        int storageSpace;
        while (true) {
            try {
                System.out.print("How much storage space do you need (# in gigabytes)?: ");

                Scanner inScanner = new Scanner(System.in);
                storageSpace = inScanner.nextInt();
                if (storageSpace < 150) {
                    throw new IllegalArgumentException("The computer requires at least 150 GBs of space");
                }

                break;
            } catch (InputMismatchException | IllegalArgumentException e) {
                System.out.println("Invalid input (" + e.getMessage() + ") please try again");
            }
        }

        return storageSpace;
    }

    private boolean getUserFastFileSpeed() {
        boolean fastFileSpeed;

        System.out.print("Do you need fast file access speed (y/n)?: ");

        Scanner inScanner = new Scanner(System.in);
        fastFileSpeed = inScanner.next().equalsIgnoreCase("Y");

        return fastFileSpeed;
    }

    private void printOutPartType(List<PcPart> parts, PcPartType partType) {
        List<PcPart> filteredParts = parts.stream()
                .filter(p -> p.getType() == partType)
                .toList();

        for (PcPart part : filteredParts) {
            System.out.printf("%s: %s\n", partType.toString(), part.getName());
        }
    }
}
