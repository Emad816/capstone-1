package pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class LedgerApp {

    static String fileName = "src/main/resources/transactions.csv";
    static ArrayList<Transaction> transactions = new ArrayList<>();
    static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
    static Scanner input = new Scanner(System.in);

    /////////////////////file reading and writing methods//////////////////////

    //read transactions file into transaction list
    public static void loadTransactions() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;

            while ((line = reader.readLine())!= null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] transaction = line.split("\\|");

                LocalDate date = LocalDate.parse((transaction[0]));
                LocalTime time = LocalTime.parse(transaction[1], timeFormat);
                String description = transaction[2];
                String vendor = transaction[3];
                double amount = Double.parseDouble(transaction[4]);

                transactions.add( new Transaction(date, time, description, vendor, amount));
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException("Error reading file " + fileName, e);
        }
    }

    //Adds one transaction to the end of transaction.csv
    public static void saveTransaction(Transaction t) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName,true));
            writer.write(t.toFileLine());
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Error writing to file");
        }
    }
}
