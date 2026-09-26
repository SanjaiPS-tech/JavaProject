package com.SubNetwork.JavaProject;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class JavaProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaProjectApplication.class, args);
    }

    @Bean
    public CommandLineRunner initDatabase(JdbcTemplate jdbc) {
        return args -> {
            // Day 2 beginner: setup initial subsea tables
            jdbc.execute("CREATE TABLE IF NOT EXISTS contacts (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100), role VARCHAR(50))");
            jdbc.execute("CREATE TABLE IF NOT EXISTS products (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100), price DOUBLE)");
            jdbc.execute("CREATE TABLE IF NOT EXISTS accounts (id INT AUTO_INCREMENT PRIMARY KEY, code VARCHAR(20), name VARCHAR(100), type VARCHAR(50))");
            jdbc.execute("CREATE TABLE IF NOT EXISTS journals (id INT AUTO_INCREMENT PRIMARY KEY, entry_date VARCHAR(30), journal_type VARCHAR(50), account_name VARCHAR(100), debit DOUBLE, credit DOUBLE, ref_no VARCHAR(100))");
            jdbc.execute("CREATE TABLE IF NOT EXISTS budgets (id INT AUTO_INCREMENT PRIMARY KEY, zone VARCHAR(100), category VARCHAR(100), budgeted DOUBLE, actual DOUBLE)");
            jdbc.execute("CREATE TABLE IF NOT EXISTS operations (id INT AUTO_INCREMENT PRIMARY KEY, cable_id VARCHAR(50), fault_km DOUBLE, auv_name VARCHAR(50), dive_hours DOUBLE, status VARCHAR(50))");

            // Seed Contact Master if empty
            Integer contactCount = jdbc.queryForObject("SELECT COUNT(*) FROM contacts", Integer.class);
            if (contactCount == null || contactCount == 0) {
                jdbc.update("INSERT INTO contacts (name, role) VALUES ('Telecom Fiber Consortium', 'Customer')");
                jdbc.update("INSERT INTO contacts (name, role) VALUES ('AUV Thruster Manufacturer', 'Vendor')");
            }

            // Seed Product Master if empty
            Integer productCount = jdbc.queryForObject("SELECT COUNT(*) FROM products", Integer.class);
            if (productCount == null || productCount == 0) {
                jdbc.update("INSERT INTO products (name, price) VALUES ('Subsea Cable Splicing', 15000.0)");
                jdbc.update("INSERT INTO products (name, price) VALUES ('Trenching Drone Deployment', 25000.0)");
                jdbc.update("INSERT INTO products (name, price) VALUES ('Fiber Optic Repeater', 12000.0)");
            }

            // Seed Chart of Accounts if empty
            Integer accountCount = jdbc.queryForObject("SELECT COUNT(*) FROM accounts", Integer.class);
            if (accountCount == null || accountCount == 0) {
                jdbc.update("INSERT INTO accounts (code, name, type) VALUES ('101', 'Bank Account', 'Assets')");
                jdbc.update("INSERT INTO accounts (code, name, type) VALUES ('102', 'Accounts Receivable', 'Assets')");
                jdbc.update("INSERT INTO accounts (code, name, type) VALUES ('201', 'Accounts Payable', 'Liabilities')");
                jdbc.update("INSERT INTO accounts (code, name, type) VALUES ('301', 'Cable Repair Service Revenue', 'Income')");
                jdbc.update("INSERT INTO accounts (code, name, type) VALUES ('401', 'Vessel Fuel Expense', 'Expenses')");
                jdbc.update("INSERT INTO accounts (code, name, type) VALUES ('402', 'AUV Repairs & Thruster Expense', 'Expenses')");
            }

            // Seed Budget for Transatlantic Segment 4 if empty
            Integer budgetCount = jdbc.queryForObject("SELECT COUNT(*) FROM budgets", Integer.class);
            if (budgetCount == null || budgetCount == 0) {
                jdbc.update("INSERT INTO budgets (zone, category, budgeted, actual) VALUES ('Transatlantic Segment 4', 'vessel fuel', 50000.0, 18500.0)");
                jdbc.update("INSERT INTO budgets (zone, category, budgeted, actual) VALUES ('Transatlantic Segment 4', 'AUV repairs', 40000.0, 12000.0)");
                jdbc.update("INSERT INTO budgets (zone, category, budgeted, actual) VALUES ('Transatlantic Segment 4', 'service revenues', 120000.0, 40000.0)");
            }

            // Seed Initial Journal Entries to have realistic balanced books
            Integer journalCount = jdbc.queryForObject("SELECT COUNT(*) FROM journals", Integer.class);
            if (journalCount == null || journalCount == 0) {
                jdbc.update("INSERT INTO journals (entry_date, journal_type, account_name, debit, credit, ref_no) VALUES ('2026-09-20', 'Bank', 'Bank Account', 150000.0, 0.0, 'INIT-CAP')");
                jdbc.update("INSERT INTO journals (entry_date, journal_type, account_name, debit, credit, ref_no) VALUES ('2026-09-20', 'Bank', 'Accounts Payable', 0.0, 150000.0, 'INIT-CAP')");
            }

            // Seed Initial Operation if empty
            Integer opCount = jdbc.queryForObject("SELECT COUNT(*) FROM operations", Integer.class);
            if (opCount == null || opCount == 0) {
                jdbc.update("INSERT INTO operations (cable_id, fault_km, auv_name, dive_hours, status) VALUES ('TAT-14-NORTH', 142.8, 'AUV-Orca-01', 18.5, 'COMPLETED')");
            }
        };
    }
}
