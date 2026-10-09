# Azani Internet Service Provider Information System

A desktop application built with **Java (Swing)** and **MySQL** for managing
internet service subscriptions for learning institutions (primary, junior,
senior schools, and colleges).

---

## Table of Contents

1. [Overview](#overview)
2. [Features](#features)
3. [Business Rules](#business-rules)
4. [System Architecture](#system-architecture)
5. [Database Schema](#database-schema)
6. [Project Structure](#project-structure)
7. [Installation & Setup](#installation--setup)
8. [Running the Application](#running-the-application)
9. [Screenshots](#screenshots)
10. [System Rules](#system-rules)
11. [Author](#author)

---

## Overview

Azani is a company that provides internet services and internet
infrastructure to learning institutions. Institutions pay a registration
fee, may purchase computers and LAN nodes from Azani, and pay a monthly
bandwidth-based subscription. This system digitizes the whole workflow:

- Registering institutions and their contact persons
- Recording site-visit infrastructure assessments
- Selling computers and LAN nodes to institutions
- Capturing registration, installation, and monthly payments
- Managing billing with automatic fines, upgrades, and disconnection flags
- Computing totals, aggregate revenues, and generating management reports

---

## Features

### Institution Registration
- Register institutions with name, type, location, readiness status
- Record full contact person details (name, phone, email, ID)

### Payment Capture
- Registration fee (KSh 8,500)
- Installation fee (KSh 10,000)
- Monthly bandwidth-based subscription
- Payment methods captured per transaction

### Purchases
- Sell computers to institutions (KSh 40,000 each)
- Sell LAN nodes with banded pricing (see Table 2 below)

### Reports and Lists
- All registered institutions
- Defaulters (unpaid bills)
- Institutions with disconnection issues
- Infrastructure requirements per institution

### Computations
- Total installation cost per institution
- Computer and LAN service cost per institution
- Total upgraded monthly charges
- Monthly charges + fines + reconnection fees by category
- Aggregate amount per institution, sorted

### Reports
- Tabbed interface with six reports
- Sortable tables (click column headers to sort)
- One-click refresh of all reports
- Export any report to CSV

### Billing Engine
- Automatic 15% fine applied when a bill's month ends without payment
- Reconnection fee (KSh 1,000) applied manually
- Plan changes (upgrade / downgrade / same plan)
- Upgrade discount: 10% off the new (higher) plan cost
- Per-month history preservation (past bills remain frozen)
- Future unpaid bills adopt the new plan automatically

### Institution History
- A dedicated tab showing **every bill for one institution across months**
- Proves historical accuracy — you can see past bills remain unchanged

---

## Business Rules

| Rule | Value |
|---|---|
| Institution types | Primary, Junior, Senior, College |
| Registration fee | KSh 8,500 (fixed) |
| Installation fee | KSh 10,000 (only if ready) |
| Computer price | KSh 40,000 each |
| Overdue fine | 15% of monthly fee |
| Fine trigger | **End of billing month** (auto-applied) |
| Reconnection fee | KSh 1,000 (flat, manual) |
| Disconnection trigger | Unpaid past the 10th of the subsequent month |
| Upgrade discount | 10% off the new (higher) plan cost |
| Downgrade | Allowed, no discount |
| Payment methods | M-Pesa, Cash, Bank Transfer, Card, Cheque |

### Bandwidth Costs

| Bandwidth (MBPS) | Cost (KSh / month) |
|---|---|
| 4  | 1,200 |
| 10 | 2,000 |
| 20 | 3,500 |
| 25 | 4,000 |
| 50 | 7,000 |

### LAN Node Costs 

| Number of Nodes | Cost (KSh) |
|---|---|
| 2 – 10   | 10,000 |
| 11 – 20  | 20,000 |
| 21 – 40  | 30,000 |
| 41 – 100 | 40,000 |

---

## System Architecture

The project follows a classic layered architecture:

    ┌─────────────────────────────────────────────┐
    │  UI Layer (Swing forms — src/ui)            │
    │  MainMenu, RegisterInstitutionForm, etc.    │
    └──────────────────────┬──────────────────────┘
                           │
    ┌──────────────────────▼──────────────────────┐
    │  Service Layer (src/services)               │
    │  AzaniService — business logic              │
    └──────────────────────┬──────────────────────┘
                           │
    ┌──────────────────────▼──────────────────────┐
    │  Data Access Layer (src/dao)                │
    │  InstitutionDAO, PaymentDAO, MonthlyPayment │
    └──────────────────────┬──────────────────────┘
                           │
    ┌──────────────────────▼──────────────────────┐
    │  Model Layer (src/models)                   │
    │  Institution, Payment, MonthlyPayment, etc. │
    └──────────────────────┬──────────────────────┘
                           │
    ┌──────────────────────▼──────────────────────┐
    │  Database (MySQL — azani_isp)               │
    └─────────────────────────────────────────────┘

- **Models** — Plain Old Java Objects (POJOs) that mirror database tables.
- **DAOs** — Encapsulate all SQL. Use `PreparedStatement` for safety.
- **Service** — Combines DAO calls and applies business rules.
- **UI** — Swing forms. Talks only to the service layer.

---

## Database Schema

Database name: **azani_isp**

Tables:

1. **institution** — schools and colleges
2. **contact_person** — one or more contacts per institution
3. **registration_payment** — KSh 8,500 records
4. **installation_payment** — KSh 10,000 records
5. **monthly_payment** — bandwidth bills with discount, fine, reconnection
6. **infrastructure** — site-visit data (users, computers, LAN nodes)
7. **computer_purchase** — computers bought from Azani
8. **lan_purchase** — LAN node purchases (banded pricing)

Full schema: see `database/azani_isp_schema.sql`.

---

## Project Structure

    AzaniISP/
    ├── lib/
    │   └── mysql-connector-j-26.7.0.jar
    ├── database/
    │   └── azani_isp_schema.sql
    ├── screenshots/
    │   └── (form screenshots)
    ├── reports/
    │   └── (exported CSV files)
    ├── src/
    │   ├── db/
    │   │   └── DBConnection.java
    │   ├── models/
    │   │   ├── Institution.java
    │   │   ├── ContactPerson.java
    │   │   ├── Payment.java
    │   │   ├── MonthlyPayment.java
    │   │   ├── Infrastructure.java
    │   │   ├── ComputerPurchase.java
    │   │   └── LanPurchase.java
    │   ├── dao/
    │   │   ├── InstitutionDAO.java
    │   │   ├── ContactPersonDAO.java
    │   │   ├── PaymentDAO.java
    │   │   ├── InfrastructureDAO.java
    │   │   ├── ComputerPurchaseDAO.java
    │   │   ├── LanPurchaseDAO.java
    │   │   └── MonthlyPaymentDAO.java
    │   ├── services/
    │   │   └── AzaniService.java
    │   └── ui/
    │       ├── AppLauncher.java
    │       ├── MainMenu.java
    │       ├── RegisterInstitutionForm.java
    │       ├── InfrastructureForm.java
    │       ├── PurchasesForm.java
    │       ├── PaymentForm.java
    │       ├── MonthlyBillingForm.java
    │       └── ReportsForm.java
    └── README.md

---

## Installation & Setup

### Prerequisites

- **Java JDK 17+** — [Download](https://www.oracle.com/java/technologies/downloads/)
- **MySQL Server 8.0+** — [Download](https://dev.mysql.com/downloads/mysql/)
- **MySQL Workbench** — [Download](https://dev.mysql.com/downloads/workbench/)
- **VS Code** with the **Extension Pack for Java** — [Download](https://code.visualstudio.com/)

### Step 1 — Extract the Project

Place the project folder anywhere on your computer.

### Step 2 — Create the Database

1. Open **MySQL Workbench** and connect to your local MySQL Server.
2. Open a new query tab.
3. Paste the contents of `database/azani_isp_schema.sql`.
4. Run the script (⚡).

This creates the `azani_isp` database and all 8 tables.

### Step 3 — Configure the Database Credentials

Open `src/db/DBConnection.java` and update:

    private static final String URL      = "jdbc:mysql://localhost:3306/azani_isp?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER     = "root";
    private static final String PASSWORD = "YOUR_MYSQL_PASSWORD_HERE";

Replace `YOUR_MYSQL_PASSWORD_HERE` with your MySQL root password.

### Step 4 — Add the JDBC Driver

Confirm that `lib/mysql-connector-j-26.7.0.jar` is on the classpath.
In VS Code, look at **JAVA PROJECTS → Referenced Libraries**.

If the JAR is missing, right-click **Referenced Libraries** → **+** →
select the JAR inside the `lib/` folder.

---

## Running the Application

1. Open the project in VS Code.
2. Open `src/ui/AppLauncher.java`.
3. Click the **Run** ▶ button (top right).

The Main Menu appears with six buttons:

1. **Register Institution** — register a learning institution and its contact person
2. **Capture Infrastructure** — record site-visit findings (users, computers, LAN)
3. **Buy Computers & LAN** — sell computers or LAN nodes to an institution
4. **Capture Payments** — record registration, installation, or monthly payment
5. **Monthly Billing** — apply fines, upgrades, reconnection; mark bills paid
6. **Reports** — view and export all reports

### Suggested Workflow for a Full Demo

1. Register 4 institutions (one of each type).
2. Capture infrastructure for each.
3. Record registration and installation payments.
4. Sell computers and LAN nodes to each institution.
5. Create monthly bills (2–3 months per institution).
6. Apply an upgrade, a reconnection, and mark some bills as paid.
7. Open Reports to see all computed values.

---

## Screenshots

### 1. Main Menu

![Main Menu](screenshots/01_main_menu.png)

The application launches with a simple menu of six actions.

### 2. Register Institution

![Register Institution](screenshots/02_register_institution.png)

Registering a new institution captures its name, type, location, readiness,
and full contact-person details.

### 3. Capture Infrastructure

![Capture Infrastructure](screenshots/03_infrastructure.png)

Recording site-visit findings: number of users, computers, and LAN nodes.

### 4. Buy Computers & LAN

![Purchases](screenshots/04_purchases.png)

Selling computers (KSh 40,000 each) and LAN nodes with banded pricing.

### 5. Capture Payments

![Payment Registration](screenshots/05_payment_registration.png)

Recording the registration fee (KSh 8,500).

![Payment Installation](screenshots/06_payment_installation.png)

Recording the installation fee (KSh 10,000).

![Payment Monthly](screenshots/07_payment_monthly.png)

Creating a monthly bill based on bandwidth.

### 6. Monthly Billing

![Monthly Billing](screenshots/08_billing_actions.png)

Applying fines, upgrades, reconnection fees, and marking bills as paid.

### 7. Reports

#### Institutions

![Report Institutions](screenshots/09_report_institutions.png)

All registered institutions with their infrastructure details.

#### Institution History

![Report History](screenshots/10_report_history.png)

Full billing history for one institution — proving past months are frozen.

#### Defaulters

![Report Defaulters](screenshots/11_report_defaulters.png)

Institutions with unpaid bills.

#### Disconnection

![Report Disconnection](screenshots/12_report_disconnection.png)

Bills past the disconnection threshold (10th of the subsequent month).

#### Install Costs

![Report Install Costs](screenshots/13_report_install_costs.png)

Total installation cost per institution.

#### Summary

![Report Summary](screenshots/14_report_summary.png)

### 8. Database Verification

![MySQL Workbench](screenshots/15_workbench_institution.png)

The `institution` table showing registered records.

---

## System Rules

Since the original specification left certain details open, the following
assumptions were made and documented here:

1. **Overdue fine** — 15% of the monthly fee.
2. **Fine trigger** — the day after the **billing month ends**. Institutions
   are expected to pay by the last day of the month in which the bill is
   issued; if unpaid when the next month begins, a 15% fine is auto-applied.
3. **Reconnection fee** — KSh 1,000 flat (applied manually after reconnection).
4. **Disconnection trigger** — a bill that remains unpaid past the **10th
   day of the subsequent month**. This is separate from the fine, which
   kicks in earlier.
5. **Defaulter** — any institution with at least one unpaid monthly bill.
6. **Upgrade** — a move to a higher bandwidth plan. The institution gets a
   **10% discount on the new plan's cost for that month only**.
7. **Downgrade** — allowed but with **no discount**.
8. **Plan-change propagation** — when a plan is changed on a bill, that
   bill is updated (with the discount if it was an upgrade), and **all
   future unpaid bills for the same institution adopt the new plan at full
   price**. Past bills remain frozen.
9. **Payment methods** — M-Pesa, Cash, Bank Transfer, Card, Cheque.
10. **Institution types** — limited to Primary, Junior, Senior, College.
11. **One contact person per institution** at registration (extendable).
12. **Disconnection flag** — an institution is flagged as disconnected when
    it has any unpaid bill. The flag clears automatically once all bills
    are paid.
13. **Aggregate per institution** — the figure shown represents
    the **total amount invoiced** to each institution: registration +
    installation + all monthly bills (whether paid or unpaid) with fines
    and reconnection fees added and discounts subtracted. It does not
    distinguish between settled and outstanding amounts. A separate
    **Defaulters** report lists the unpaid portion.

---

## Author

**Name:** [Polycap Bruce]    
**Date:** [15th October 2026]

---

## License

This project was created for academic purposes. Free to use and modify
for educational contexts.