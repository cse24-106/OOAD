package com.felix.bankingsystem.view;

import com.felix.bankingsystem.controller.BankService;
import com.felix.bankingsystem.model.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class InvestmentAccountController {

    @FXML
    private TextField accountNumberField;
    @FXML
    private TextField initialDepositField;

    private BankService accountManager = new BankService();
    private Customer customer;

    private void handleCreateInvestmentAccount() {
        String accountNumber = accountNumberField.getText();
        double initialDeposit = Double.parseDouble(initialDepositField.getText());

        accountManager.openInvestmentAccount(customer, accountNumber, initialDeposit);
    }
}