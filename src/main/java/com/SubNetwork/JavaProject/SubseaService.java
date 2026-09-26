package com.SubNetwork.JavaProject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
public class SubseaService {
    private final JdbcTemplate jdbc;

    public SubseaService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Map<String, Object> getMasters() {
        Map<String, Object> data = new HashMap<>();
        data.put("actors", List.of("Admin", "Invoicing User", "System"));
        data.put("contacts", jdbc.queryForList("SELECT * FROM contacts ORDER BY id"));
        data.put("products", jdbc.queryForList("SELECT * FROM products ORDER BY id"));
        data.put("accounts", jdbc.queryForList("SELECT * FROM accounts ORDER BY code"));
        return data;
    }

    public void addContact(String name, String role) {
        jdbc.update("INSERT INTO contacts (name, role) VALUES (?, ?)", name, role);
    }

    public void addProduct(String name, double price) {
        jdbc.update("INSERT INTO products (name, price) VALUES (?, ?)", name, price);
    }

    public void ingestOtdrAndDispatch(String cableId, double faultKm, String auvName, double diveHours) {
        jdbc.update("INSERT INTO operations (cable_id, fault_km, auv_name, dive_hours, status) VALUES (?, ?, ?, ?, 'COMPLETED')",
                cableId, faultKm, auvName, diveHours);
        jdbc.update("UPDATE budgets SET actual = actual + ? WHERE zone = 'Transatlantic Segment 4' AND category = 'AUV repairs'", diveHours * 250.0);
    }

    public List<Map<String, Object>> getOperations() {
        return jdbc.queryForList("SELECT * FROM operations ORDER BY id DESC");
    }

    public void deleteOperation(int id) {
        jdbc.update("DELETE FROM operations WHERE id = ?", id);
    }

    public Map<String, Object> runSalesFlow(String customer, String product, double amount, String user) {
        String today = LocalDate.now().toString();
        String ref = "SO-INV-" + (System.currentTimeMillis() % 10000);
        // Sales Journal: Debit Accounts Receivable, Credit Revenue
        jdbc.update("INSERT INTO journals (entry_date, journal_type, account_name, debit, credit, ref_no) VALUES (?, 'Sales', 'Accounts Receivable', ?, 0, ?)", today, amount, ref);
        jdbc.update("INSERT INTO journals (entry_date, journal_type, account_name, debit, credit, ref_no) VALUES (?, 'Sales', 'Cable Repair Service Revenue', 0, ?, ?)", today, amount, ref);
        // Bank Journal: Debit Bank, Credit Accounts Receivable
        jdbc.update("INSERT INTO journals (entry_date, journal_type, account_name, debit, credit, ref_no) VALUES (?, 'Bank', 'Bank Account', ?, 0, ?)", today, amount, ref + "-PAY");
        jdbc.update("INSERT INTO journals (entry_date, journal_type, account_name, debit, credit, ref_no) VALUES (?, 'Bank', 'Accounts Receivable', 0, ?, ?)", today, amount, ref + "-PAY");
        // Update Budget for service revenues
        jdbc.update("UPDATE budgets SET actual = actual + ? WHERE zone = 'Transatlantic Segment 4' AND category = 'service revenues'", amount);
        return Map.of("status", "SUCCESS", "flow", "Sales Order -> Customer Invoice -> Bank Payment", "reference", ref, "actor", user);
    }

    public Map<String, Object> runPurchaseFlow(String vendor, String item, double amount, String user) {
        String today = LocalDate.now().toString();
        String ref = "PO-BILL-" + (System.currentTimeMillis() % 10000);
        String expenseAcct = item.toLowerCase().contains("fuel") ? "Vessel Fuel Expense" : "AUV Repairs & Thruster Expense";
        String budgetCat = item.toLowerCase().contains("fuel") ? "vessel fuel" : "AUV repairs";
        // Purchase Journal: Debit Expense, Credit Accounts Payable
        jdbc.update("INSERT INTO journals (entry_date, journal_type, account_name, debit, credit, ref_no) VALUES (?, 'Purchase', ?, ?, 0, ?)", today, expenseAcct, amount, ref);
        jdbc.update("INSERT INTO journals (entry_date, journal_type, account_name, debit, credit, ref_no) VALUES (?, 'Purchase', 'Accounts Payable', 0, ?, ?)", today, amount, ref);
        // Bank Journal: Debit Accounts Payable, Credit Bank
        jdbc.update("INSERT INTO journals (entry_date, journal_type, account_name, debit, credit, ref_no) VALUES (?, 'Bank', 'Accounts Payable', ?, 0, ?)", today, amount, ref + "-PAY");
        jdbc.update("INSERT INTO journals (entry_date, journal_type, account_name, debit, credit, ref_no) VALUES (?, 'Bank', 'Bank Account', 0, ?, ?)", today, amount, ref + "-PAY");
        // Update Budget
        jdbc.update("UPDATE budgets SET actual = actual + ? WHERE zone = 'Transatlantic Segment 4' AND category = ?", amount, budgetCat);
        return Map.of("status", "SUCCESS", "flow", "Purchase Order -> Vendor Bill -> Bank Payment", "reference", ref, "actor", user);
    }

    public List<Map<String, Object>> getJournals() {
        return jdbc.queryForList("SELECT * FROM journals ORDER BY id DESC");
    }

    public void deleteJournal(int id) {
        jdbc.update("DELETE FROM journals WHERE id = ?", id);
    }

    public Map<String, Object> getReports() {
        List<Map<String, Object>> budget = jdbc.queryForList("SELECT zone, category, budgeted, actual, (budgeted - actual) as variance FROM budgets");
        Double totalRev = jdbc.queryForObject("SELECT COALESCE(SUM(credit), 0) FROM journals WHERE account_name = 'Cable Repair Service Revenue'", Double.class);
        Double fuelExp = jdbc.queryForObject("SELECT COALESCE(SUM(debit), 0) FROM journals WHERE account_name = 'Vessel Fuel Expense'", Double.class);
        Double auvExp = jdbc.queryForObject("SELECT COALESCE(SUM(debit), 0) FROM journals WHERE account_name = 'AUV Repairs & Thruster Expense'", Double.class);
        double netIncome = (totalRev != null ? totalRev : 0) - (fuelExp != null ? fuelExp : 0) - (auvExp != null ? auvExp : 0);
        Double bankDebit = jdbc.queryForObject("SELECT COALESCE(SUM(debit - credit), 0) FROM journals WHERE account_name = 'Bank Account'", Double.class);
        Double ar = jdbc.queryForObject("SELECT COALESCE(SUM(debit - credit), 0) FROM journals WHERE account_name = 'Accounts Receivable'", Double.class);
        Double ap = jdbc.queryForObject("SELECT COALESCE(SUM(credit - debit), 0) FROM journals WHERE account_name = 'Accounts Payable'", Double.class);
        return Map.of("budget", budget, "pnl", Map.of("revenue", totalRev != null ? totalRev : 0, "expenses", (fuelExp != null ? fuelExp : 0) + (auvExp != null ? auvExp : 0), "netIncome", netIncome),
                "balanceSheet", Map.of("bank", bankDebit != null ? bankDebit : 0, "ar", ar != null ? ar : 0, "ap", ap != null ? ap : 0, "equity", netIncome));
    }
}
