# Automated Subsea Fiber Optic Cable Repair & Drone Trenching Network

A simple, beginner-friendly web application built with **Spring Boot, MySQL/MariaDB, HTML, CSS, and Vanilla JavaScript**.

---

## 🌊 Overview
Monitors subsea optical telecommunications cables using Autonomous Underwater Vehicles (AUVs), tracks underwater trenching & cable splicing operations, bills telecom consortia, manages vendor purchases for thrusters and vessel fuel, and tracks marine operational budgets.

---

## 👥 Actors
* **Admin**: Complete system visibility and purchase flow execution.
* **Invoicing User**: Generates sales orders, invoices, and payment collections.
* **System**: Autonomous telemetry and OTDR fault ingest.

---

## 📦 Master Data
* **Contact Master**:
  * `Telecom Fiber Consortium` (Customer)
  * `AUV Thruster Manufacturer` (Vendor)
* **Product Master**:
  * `Subsea Cable Splicing` ($15,000)
  * `Trenching Drone Deployment` ($25,000)
  * `Fiber Optic Repeater` ($12,000)
* **Chart of Accounts**:
  * **Assets**: Bank Account (101), Accounts Receivable (102)
  * **Liabilities**: Accounts Payable (201)
  * **Income**: Cable Repair Service Revenue (301)
  * **Expenses**: Vessel Fuel Expense (401), AUV Repairs & Thruster Expense (402)

---

## 🔄 Automated Accounting & Workflows
* **Purchase Flow**: Purchase Order &rarr; Vendor Bill &rarr; Bank Payment
  * Auto-generates balanced dual-entry journals (Debit Expense, Credit AP &rarr; Debit AP, Credit Bank).
* **Sales Flow**: Sales Order &rarr; Customer Invoice &rarr; Bank Payment
  * Auto-generates balanced dual-entry journals (Debit AR, Credit Revenue &rarr; Debit Bank, Credit AR).
* **Budget Tracking**: Real-time Budget vs Actual for **Transatlantic Segment 4** (Vessel Fuel, AUV Repairs, and Service Revenues).
* **Reports**: Live Balance Sheet, Profit & Loss Account, and Budget Variance.
* **HTTP Methods**: Clean REST API strictly utilizing `GET`, `POST`, and `DELETE`.

---

## 🚀 How to Run
1. Ensure MariaDB / MySQL is running on port 3306.
2. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
3. Open your browser:
   ```
   http://localhost:8080
   ```