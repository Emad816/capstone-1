# Accounting Ledger

A command-line Java app for tracking deposits and payments for a business or for personal use.
Every transaction is saved to `transactions.csv`, so your ledger is still there the next time you run the app.

## Features

- **Home screen**: add a deposit, make a payment, open the ledger, or exit
- **Ledger screen**: view all entries, only deposits, or only payments (newest first)
- **Reports**: month to date, previous month, year to date, previous year, and search by vendor

## How to run

1. Clone the repo and open it in IntelliJ.
2. Run `LedgerApp.java` (package `com.pluralsight`).
3. Use the letters and numbers on screen to move around.

## File format

Each transaction is one line in `transactions.csv`:

```
date|time|description|vendor|amount
2026-08-14|10:13:25|Ergonomic keyboard|Amazon|-89.50
```

Deposits are positive amounts. Payments are saved as negative amounts.

## Project structure

- `Transaction.java` - holds the date, time, description, vendor, and amount of one transaction
- `LedgerApp.java` - the screens, the file reading and writing, and the reports

## Interesting piece of code

`displayEntries()` loops backwards through the list so the newest transactions show first, without needing to sort.
