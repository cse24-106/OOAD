package com.felix.bankingsystem.controller;

import com.felix.bankingsystem.model.*;

import java.io.*;
import java.util.*;

public class DatabaseHandler {
    private static final String Customers_Details = "src/main/java/com/felix/bankingsystem/data/CustomerDetails";
    private static final String Accounts = "src/main/java/com/felix/bankingsystem/data/Accounts";
    private static final String Transactions = "src/main/java/com/felix/bankingsystem/data/Transactions";

    public void saveCustomers(Collection<Customer> customers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Customers_Details))) {
            for (Customer c : customers) {
                if (c instanceof IndividualCustomer ic) {
                    writer.write(String.format("IND,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                            ic.getCustomerId(),
                            ic.getFirstName(),
                            ic.getSurname(),
                            ic.getNationalID(),
                            ic.getAddress(),
                            ic.getPhoneNumber(),
                            ic.getEmail(),
                            ic.getSourceOfFunds(),
                            ic.getPassword()));
                } else if (c instanceof CompanyCustomer cc) {
                    writer.write(String.format("ORG,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                            cc.getCustomerId(),
                            cc.getCompanyName(),
                            cc.getRegistrationNumber(),
                            cc.getAddress(),
                            cc.getContactPerson(),
                            cc.getContactPhone(),
                            cc.getTax_ID(),
                            cc.getIncoporation_Date(),
                            cc.getPassword()));
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveAccounts(Collection<Customer> customers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Accounts))) {
            for (Customer c : customers) {
                for (Account acc : c.getAccounts()) {
                    String type = acc.getClass().getSimpleName();
                    writer.write(String.format("%s,%s,%s,%.2f",
                            acc.getAccountNumber(),
                            c.getCustomerId(),
                            type,
                            acc.getBalance()));
                    writer.newLine();
                }
            }
            System.out.println("Accounts saved to " + Accounts);
        } catch (IOException e) {
            System.err.println("Error saving accounts: " + e.getMessage());
        }
    }

    public void saveTransactions(Collection<Customer> customers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Transactions))) {
            for (Customer c : customers) {
                for (Account acc : c.getAccounts()) {
                    for (Transaction t : acc.getTransactions()) {
                        writer.write(String.format("%s,%s,%s,%.2f,%.2f,%s",
                                t.getTransactionID(),
                                acc.getAccountNumber(),
                                t.getType(),
                                t.getAmount(),
                                t.getBalanceAfter(),
                                t.getDate()));
                        writer.newLine();
                    }
                }
            }
            System.out.println("Transactions saved to " + Transactions);
        } catch (IOException e) {
            System.err.println("Error saving transactions: " + e.getMessage());
        }
    }

    public Map<String, Customer> loadCustomers() {
        Map<String, Customer> customers = new HashMap<>();

        File file = new File(Customers_Details);
        if (!file.exists()) {
            System.out.println("No customers file found yet.");
            return customers;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(Customers_Details))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if(data.length == 0) continue;

                if (data[0].equals("IND")) {
                    // IND, ID, first, surname, nationalID, address, phone, email
                    IndividualCustomer c = new IndividualCustomer(
                            data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9]);
                    customers.put(c.getCustomerId(), c);
                } else if (data[0].equals("ORG")) {
                    // ORG, ID, name, regNo, address, contact, phone, tax, incorporation date
                    CompanyCustomer c = new CompanyCustomer(
                            data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8]);
                    c.setPassword(data[9]);
                    customers.put(c.getCustomerId(), c);
                }
            }
            System.out.println("Customers loaded: " + customers.size());
        } catch (IOException e) {
            System.err.println("Error loading customers: " + e.getMessage());
        }
        return customers;
    }

    public void loadAccounts(Map<String, Customer> customers) {
        File file = new File(Accounts);
        if (!file.exists()) {
            System.out.println("No accounts file found yet.");
            return;
        }

        try(BufferedReader reader = new BufferedReader(new FileReader(Accounts))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String accNum = data[0];
                String customerId = data[1];
                String type = data[2];
                double amount = Double.parseDouble(data[3]);

                Customer c = customers.get(customerId);
                if (c == null) continue;

                Account acc = switch (type) {
                    case "SavingsAccount" -> new SavingsAccount(accNum, c, 0);
                    case "InvestmentAccount" -> new InvestmentAccount(accNum, c, 0);
                    case "ChequeAccount" -> new ChequeAccount(accNum, 0, c, "N/A", "N/A");
                    default -> null;
                };
                if (acc != null) {
                    acc.deposit(amount);
                    c.addAccount(acc);
                }
            }
            System.out.println("Accounts loaded and linked to customers.");
        } catch (IOException e) {
            System.err.println("Error loading accounts: " + e.getMessage());
        }
    }

    public void loadTransactions(Map<String, Customer> customers) {
        File file = new File(Transactions);
        if (!file.exists()) {
            System.out.println("No transactions file found yet.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(Transactions))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String accNum = data[1];
                String customerId = findCustomerByAccount(customers, accNum);
                if (customerId != null) {
                    Customer c = customers.get(customerId);
                    if (c !=null) {
                        Account acc = c.getAccounts().stream().filter(a -> a.getAccountNumber().equals(accNum)).findFirst().orElse(null);
                        if (acc != null) {
                            Transaction t = new Transaction(
                                    data[0], data[5], data[2], Double.parseDouble(data[3]), Double.parseDouble(data[4]));
                            acc.addTransaction(t);
                        }
                    }
                }
            }
            System.out.println("Transactions loaded successfully.");
        } catch (IOException e) {
            System.err.println("Error loading transactions: " + e.getMessage());
        }
    }

    private String findCustomerByAccount(Map<String, Customer> customers, String accNum) {
        for (var entry : customers.entrySet()) {
            for (Account a : entry.getValue().getAccounts()) {
                if (a.getAccountNumber().equals(accNum)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }
}