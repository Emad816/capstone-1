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
}
