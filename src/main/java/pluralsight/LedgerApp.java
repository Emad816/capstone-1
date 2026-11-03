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

    // Keeps asking until the user types a real number
    public static double askForAmount() {
        System.out.print("Amount: ");

        while (!input.hasNextDouble()) {
            System.out.println("Please enter a valid amount");
            input.nextLine(); //eats leftover
            System.out.print("Amount: ");
        }
        double amount = input.nextDouble();
        input.nextLine(); //eats leftover
        return amount;
    }

    ////////////////Ledger Screen///////////////
    public static void ledgerScreen() {
        boolean running = true;
        while (running) {
            System.out.println("---------------------------------");
            System.out.println("----------LEDGER SCREEN----------");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");
            System.out.print("Choose an option: ");

            String choice = input.nextLine().trim().toUpperCase();

            switch (choice) {
                case "A" -> displayTransactions(transactions);
                case "D" -> displayDeposits();
                case "P" -> displayPayments();
                case "R" -> reportsScreen();
                case "H" -> running = false;
                default -> System.out.println("Invalid choice");

            }
        }
    }

    //Displays transactions in reverse order newest to older
    public static void displayTransactions(ArrayList<Transaction> transactions) {
        System.out.println("-----------------");

        if (transactions.isEmpty()) {
            System.out.println("No transactions found!");
            return;
        }
        for (int i = transactions.size() - 1; i >= 0; i--) {
            System.out.println(transactions.get(i).toFileLine());
        }
    }

    //Makes a new list and adds positive transactions only and displays it
    public static void displayDeposits() {

        ArrayList<Transaction> deposits = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getAmount() > 0) {
                deposits.add(t);
            }
        }
        displayTransactions(deposits);
    }

    //Makes a new list and adds negative transactions only and displays it
    public static void displayPayments() {
        ArrayList<Transaction> payments = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getAmount() < 0) {
                payments.add(t);
            }
        }
        displayTransactions(payments);
    }

    //////////////////Reports Screen////////////////
    public static void reportsScreen() {
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("------------ REPORTS ------------");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");
            System.out.print("Choose an option: ");

            String choice = input.nextLine().trim();

            switch (choice) {
                case "1" -> monthToDate();
                case "2" -> previousMonth();
                case "3" -> yearToDate();
                case "4" -> previousYear();
                case "5" -> searchByVendor();
                case "0" -> running = false;
                default -> System.out.println("Invalid choice");
            }
        }
    }

    // Everything from the 1st of this month up to today
    public static void monthToDate() {
        LocalDate today = LocalDate.now();
        ArrayList<Transaction> results = new ArrayList<>();

        for (Transaction t : transactions) {
            LocalDate d = t.getDate();
            if (d.getYear() == today.getYear() && d.getMonth() == today.getMonth() && !d.isAfter(today)) {
                results.add(t);
            }
        }
        displayTransactions(results);
    }

    // Everything from last month (minusMonths handles January -> December of last year)
    public static void previousMonth() {
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        ArrayList<Transaction> results = new ArrayList<>();

        for (Transaction t : transactions) {
            LocalDate d = t.getDate();
            if (d.getYear() == lastMonth.getYear() && d.getMonth() == lastMonth.getMonth()) {
                results.add(t);
            }
        }
        displayTransactions(results);
    }

    // Everything from January 1st of this year up to today
    public static void yearToDate() {
        LocalDate today = LocalDate.now();
        ArrayList<Transaction> results = new ArrayList<>();

        for (Transaction t : transactions) {
            LocalDate d = t.getDate();
            if (d.getYear() == today.getYear() && !d.isAfter(today)) {
                results.add(t);
            }
        }
        displayTransactions(results);
    }

    public static void previousYear() {
        int lastYear = LocalDate.now().getYear() - 1;
        ArrayList<Transaction> results = new ArrayList<>();

        for (Transaction t : transactions) {
            if (t.getDate().getYear() == lastYear) {
                results.add(t);
            }
        }
        displayTransactions(results);
    }


    public static void searchByVendor() {
        System.out.print("Enter the vendor name: ");
        String vendor = input.nextLine().trim();
        ArrayList<Transaction> results = new ArrayList<>();

        for (Transaction t : transactions) {
            if (t.getVendor().equalsIgnoreCase(vendor)) {
                results.add(t);
            }
        }
        displayTransactions(results);
    }
}
