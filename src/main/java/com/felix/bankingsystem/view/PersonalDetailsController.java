package com.felix.bankingsystem.view;

import com.felix.bankingsystem.model.Customer;
import com.felix.bankingsystem.model.IndividualCustomer;
import com.felix.bankingsystem.model.CompanyCustomer;
import com.felix.bankingsystem.model.Account;
import com.felix.bankingsystem.controller.BankService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

public class PersonalDetailsController {

    @FXML private Label Available_balance;
    @FXML private Label CustomerID;
    @FXML private Button Logout_btn;
    @FXML private TextField address_txt;
    @FXML private Button dashboard_btn;
    @FXML private Button deposit_btn;
    @FXML private TextField email_txt;
    @FXML private TextField firstname_txt;
    @FXML private TextField nationalID_txt;
    @FXML private TextField number_txt;
    @FXML private Button open_acc_btn;
    @FXML private Button pers_det_btn;
    @FXML private TextField surname_txt;
    @FXML private Button transaction_hist_btn;
    @FXML private Button withdraw_btn;
    @FXML private TextField source_of_funds_txt;
    @FXML private TextField conatct_person_txt;

    private Customer customer;
    private BankService bankService;

    @FXML
    private void initialize() {
        setupButtonActions();
        disableEditableFields();
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        updateView();
    }

    public void setBankService(BankService bankService) {
        this.bankService = bankService;
    }

    @FXML
    private void setupButtonActions() {
        dashboard_btn.setOnAction(e -> showDashboard());
        deposit_btn.setOnAction(e -> showDepositScreen());
        withdraw_btn.setOnAction(e -> showWithdrawScreen());
        open_acc_btn.setOnAction(e -> showOpenAccountScreen());
        pers_det_btn.setOnAction(e -> showPersonalDetailsScreen());
        transaction_hist_btn.setOnAction(e -> showTransactionHistoryScreen());
        Logout_btn.setOnAction(e -> logout());
    }

    private void disableEditableFields() {
        // Make fields read-only for viewing
        firstname_txt.setEditable(false);
        surname_txt.setEditable(false);
        nationalID_txt.setEditable(false);
        address_txt.setEditable(false);
        number_txt.setEditable(false);
        email_txt.setEditable(false);
        source_of_funds_txt.setEditable(false);
        conatct_person_txt.setEditable(false);
    }

    private void updateView() {
        if (customer != null) {
            // Update balance
            double totalBalance = customer.getAccounts().stream()
                    .mapToDouble(Account::getBalance)
                    .sum();
            Available_balance.setText(String.format("P%.2f", totalBalance));

            // Update customer details
            CustomerID.setText(customer.getCustomerId());

            if (customer instanceof IndividualCustomer) {
                IndividualCustomer indCustomer = (IndividualCustomer) customer;
                firstname_txt.setText(indCustomer.getFirstName());
                surname_txt.setText(indCustomer.getSurname());
                nationalID_txt.setText(indCustomer.getNationalID());
                address_txt.setText(indCustomer.getAddress());
                number_txt.setText(indCustomer.getPhoneNumber());
                email_txt.setText(indCustomer.getEmail());
                source_of_funds_txt.setText(indCustomer.getSourceOfFunds());
                conatct_person_txt.setText("N/A");

            } else if (customer instanceof CompanyCustomer) {
                CompanyCustomer compCustomer = (CompanyCustomer) customer;
                firstname_txt.setText(compCustomer.getCompanyName());
                surname_txt.setText("N/A");
                nationalID_txt.setText(compCustomer.getRegistrationNumber());
                address_txt.setText(compCustomer.getAddress());
                number_txt.setText(compCustomer.getContactPhone());
                email_txt.setText("N/A");
                source_of_funds_txt.setText("N/A");
                conatct_person_txt.setText(compCustomer.getContactPerson());
            }
        }
    }

    @FXML
    private void showDashboard() {
        navigateTo("/com/felix/bankingsystem/FXML files/Dashboard.fxml", "Dashboard");
    }

    @FXML
    private void showDepositScreen() {
        navigateTo("/com/felix/bankingsystem/FXML files/Deposit.fxml", "Deposit Funds");
    }

    @FXML
    private void showWithdrawScreen() {
        navigateTo("/com/felix/bankingsystem/FXML files/Withdraw.fxml", "Withdraw Funds");
    }

    @FXML
    private void showOpenAccountScreen() {
        navigateTo("/com/felix/bankingsystem/FXML files/OpenAccount.fxml", "Open Account");
    }

    @FXML
    private void showPersonalDetailsScreen() {
        // Already on personal details screen
    }

    @FXML
    private void showTransactionHistoryScreen() {
        navigateTo("/com/felix/bankingsystem/FXML files/TransationHistory.fxml", "Transaction History");
    }

    @FXML
    private void logout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix/bankingsystem/FXML files/LoginView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) Logout_btn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Capital Bank - Login");
            stage.centerOnScreen();

        } catch (IOException e) {
            showAlert("Error", "Cannot logout: " + e.getMessage());
        }
    }

    private void navigateTo(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Object controller = loader.getController();
            setControllerProperties(controller);

            Stage stage = (Stage) dashboard_btn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);

        } catch (IOException e) {
            showAlert("Error", "Cannot navigate: " + e.getMessage());
        }
    }

    private void setControllerProperties(Object controller) {
        if (controller instanceof DashboardController) {
            ((DashboardController) controller).setCustomer(customer);
            ((DashboardController) controller).setBankService(bankService);
        } else if (controller instanceof DepositController) {
            ((DepositController) controller).setCustomer(customer);
            ((DepositController) controller).setBankService(bankService);
        } else if (controller instanceof WithdrawController) {
            ((WithdrawController) controller).setCustomer(customer);
            ((WithdrawController) controller).setBankService(bankService);
        } else if (controller instanceof TransactionHistoryController) {
            ((TransactionHistoryController) controller).setCustomer(customer);
            ((TransactionHistoryController) controller).setBankService(bankService);
        } else if (controller instanceof OpenAccountController) {
            ((OpenAccountController) controller).setCustomer(customer);
            ((OpenAccountController) controller).setBankService(bankService);
        }  else if (controller instanceof PersonalDetailsController) {
            ((PersonalDetailsController) controller).setCustomer(customer);
            ((PersonalDetailsController) controller).setBankService(bankService);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
