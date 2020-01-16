package de.marcoedenhofer.edenbank.application.interestservice;

import de.marcoedenhofer.edenbank.application.bankaccountservice.IBankAccountService;
import de.marcoedenhofer.edenbank.application.transactionservice.BankTransactionException;
import de.marcoedenhofer.edenbank.application.transactionservice.ITransactionService;
import de.marcoedenhofer.edenbank.application.transactionservice.TransactionData;
import de.marcoedenhofer.edenbank.persistence.entities.BankAccount;
import de.marcoedenhofer.edenbank.persistence.entities.CheckingAccount;
import de.marcoedenhofer.edenbank.persistence.entities.SavingsAccount;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InterestService implements IInterestService {

    private final ITransactionService transactionService;
    private final IBankAccountService bankAccountService;

    public InterestService(ITransactionService transactionService,
                           IBankAccountService bankAccountService) {
        this.transactionService = transactionService;
        this.bankAccountService = bankAccountService;
    }

    // every minute
    @Scheduled(cron = "0 */1 * * * *")
    private void bookInterests() {
        bankAccountService.getAllActiveSavingsAccounts()
                .forEach(this::bookInterest);
    }

    @Transactional
    protected void bookInterest(SavingsAccount bankAccount) {
        if (bankAccount.getBalance() > 0) {
            BankAccount edenbankAccount = bankAccountService.loadBankAccountWithId(2);
            TransactionData transactionData = new TransactionData();
            transactionData.setSenderIban(edenbankAccount.getIban());
            transactionData.setReceiverIban(bankAccount.getIban());
            transactionData.setUsageDetails("Zinsen auf Tagesgeld");
            double amount = computeInterestPayment(bankAccount.getBalance(), bankAccount.getInterestRate());
            transactionData.setAmount(amount);

            try {
                transactionService.requestInternalTransaction(transactionData);
            } catch (BankTransactionException e) {
                e.printStackTrace();
            }
        }
    }

    private double computeInterestPayment(int balance, float interest) {
        return balance * (interest/100.0) / 100.0;
    }
}
