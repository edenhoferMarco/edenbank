package de.marcoedenhofer.edenbank.application.interestservice;

import de.marcoedenhofer.edenbank.application.bankaccountservice.IBankAccountService;
import de.marcoedenhofer.edenbank.application.transactionservice.BankTransactionException;
import de.marcoedenhofer.edenbank.application.transactionservice.ITransactionService;
import de.marcoedenhofer.edenbank.application.transactionservice.TransactionData;
import de.marcoedenhofer.edenbank.persistence.entities.BankAccount;
import de.marcoedenhofer.edenbank.persistence.entities.FixedDepositAccount;
import de.marcoedenhofer.edenbank.persistence.entities.SavingsAccount;
import de.marcoedenhofer.edenbank.persistence.repositories.IBankAccountRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

@Service
@Scope("singleton")
public class InterestService implements IInterestService {

    private final ITransactionService transactionService;
    private final IBankAccountService bankAccountService;
    private final IBankAccountRepository bankAccountRepository;

    public InterestService(ITransactionService transactionService,
                           IBankAccountService bankAccountService, IBankAccountRepository bankAccountRepository) {
        this.transactionService = transactionService;
        this.bankAccountService = bankAccountService;
        this.bankAccountRepository = bankAccountRepository;
    }

    // every minute
    @Scheduled(cron = "0 */1 * * * *")
    private void bookInterestForSavingAccounts() {
        bankAccountService.loadAllActiveSavingsAccounts()
                .forEach(this::bookInterest);
    }

    // every minute
    @Scheduled(cron = "0 */1 * * * *")
    private void bookInterestForFixedDepositAccounts() {
        bankAccountService.loadAllActiveFixedDepositAccounts().forEach(account -> {
            if (!account.isDone()) {
                bookInterest(account);
                if (account.getEndDate().before(Calendar.getInstance().getTime())) {
                    account.setDone(true);
                    bankAccountRepository.save(account);
                }
            }
        });
    }

    @Transactional
    protected void bookInterest(BankAccount bankAccount) {
        final long EDENBANK_ACCOUNT_ID = 2;

        if (!(bankAccount instanceof SavingsAccount || bankAccount instanceof FixedDepositAccount)) {
            return;
        }

        if (bankAccount.getBalance() > 0) {
            BankAccount edenbankAccount = bankAccountService.loadBankAccountWithId(EDENBANK_ACCOUNT_ID);
            TransactionData transactionData = new TransactionData();
            transactionData.setSenderIban(edenbankAccount.getIban());
            transactionData.setReceiverIban(bankAccount.getIban());

            double amount;
            if (bankAccount instanceof SavingsAccount) {
                transactionData.setUsageDetails("Zinsen auf Tagesgeld");
                amount = computeInterestPayment(bankAccount.getBalance(),
                        ((SavingsAccount) bankAccount).getInterestRate());
            } else {
                transactionData.setUsageDetails("Zinsen auf Festgeld");
                amount = computeInterestPayment(bankAccount.getBalance(),
                        ((FixedDepositAccount) bankAccount).getInterestRate());
            }
            transactionData.setAmount(amount);

            try {
                transactionService.requestInternalTransaction(transactionData);
            } catch (BankTransactionException e) {
                e.printStackTrace();
            }
        }
    }

    private double computeInterestPayment(long balance, float interest) {
        return balance * (interest/100.0) / 100.0;
    }
}
