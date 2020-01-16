package de.marcoedenhofer.edenbank.application.customeraccountservice;

import de.marcoedenhofer.edenbank.application.transactionservice.ITransactionService;
import de.marcoedenhofer.edenbank.persistence.entities.*;
import de.marcoedenhofer.edenbank.persistence.repositories.ICustomerAccountRepository;
import de.marcoedenhofer.edenbank.persistence.repositories.ICustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Qualifier("security")
public class CustomerAccountService implements ICustomerAccountService {
    private final long EDENBANK_ACCOUNT_ID = 1;
    private final double PRIVATE_MANAGEMENT_FEE = 0.0;
    private final double BUSINESS_MANAGEMENT_FEE = 150;

    private final ICustomerRepository customerRepository;
    private final ICustomerAccountRepository customerAccountRepository;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public CustomerAccountService(ICustomerRepository customerRepository,
                                  ICustomerAccountRepository customerAccountRepository,
                                  BCryptPasswordEncoder encoder) {
        this.customerRepository = customerRepository;
        this.customerAccountRepository = customerAccountRepository;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public CustomerAccount createPrivateCustomerAccount(PrivateCustomer customer)
            throws PostIdentException {
        customer = customerRepository.save(customer);

        if (!customerIsIdentifiedViaPostIdent(customer)) {
            throw new PostIdentException(customer.getPersonalData().getFormOfAddress() + " "
                    + customer.getPersonalData().getFirstname() + " "
                    + customer.getPersonalData().getLastname() +
                    " konnte nicht durch unseren Partner identifiziert werden"
            );
        }
        customer.setIdentified(true);
        customer = customerRepository.save(customer);
        CustomerAccount account = buildCustomerAccount(PRIVATE_MANAGEMENT_FEE, customer);
        return customerAccountRepository.save(account);
    }

    @Override
    @Transactional
    public CustomerAccount createBusinessCustomerAccount(BusinessCustomer customer) {
        customer = customerRepository.save(customer);

        CustomerAccount account = buildCustomerAccount(BUSINESS_MANAGEMENT_FEE, customer);
        return customerAccountRepository.save(account);
    }

    @Override
    public void callGiveawayService(CustomerAccount customerAccount) throws GiveawayException {
        // TODO call fatih for giveaway
        boolean giveawayServiceFailed = true;
        if (giveawayServiceFailed) {
            throw new GiveawayException("Es tut uns leid, unser Partnerservice für Geschenke ist ausgefallen. " +
                    "Sie erhalten Ihr Geschenk sobald er wieder verfügbar ist!");
        }
    }

    @Override
    public CustomerAccount loadCustomerAccountWithId(long customerAccountId) throws UsernameNotFoundException {
        return loadCustomerAccountWithId(String.valueOf(customerAccountId));
    }

    @Override
    public CustomerAccount loadCustomerAccountWithId(String customerAccountId) throws UsernameNotFoundException {
        UserDetails userDetails = loadUserByUsername(customerAccountId);

        if (!(userDetails instanceof CustomerAccount)) {
            throw new UsernameNotFoundException("Account mit Nummer " + customerAccountId + " ist kein Kundenaccount");
        }

        return (CustomerAccount) userDetails;
    }

    @Override
    public UserDetails loadUserByUsername(String customerAccountId) throws UsernameNotFoundException {
        CustomerAccount account = customerAccountRepository.findById(Long.parseLong(customerAccountId))
                .orElseThrow( () -> {
                    throw new UsernameNotFoundException("Kundenaccount mit Nummer: " + customerAccountId + " existiert nicht");
                });

        return account;
    }


    @Override
    public boolean customerAccountOwnsBankAccountWithId(CustomerAccount customerAccount, long bankAccountId) {
        for (BankAccount bankAccount : customerAccount.getBankAccounts()) {
            if (bankAccount.getBankAccountId() == bankAccountId) {
                return true;
            }
        }

        return false;
    }

    private CustomerAccount buildCustomerAccount(double managementFee, Customer customer) {
        CustomerAccount account = new CustomerAccount();
        account.setArchived(false);
        // for now use firstname as password
        String password = customer.getPersonalData().getFirstname();
        account.setPassword(encoder.encode(password));
        account.setManagementFee(managementFee);
        account.setCustomerDetails(customer);

        return account;
    }

    /*
    private void checkForNewIdentifiedCustomers() {
        customerRepository.findAllByIdentifiedTrue()
                .forEach(customer -> {
                    if (customerIsIdentifiedViaPostIdent(customer)) {
                        customer.setIdentified(true);
                        customer = customerRepository.save(customer);
                        CustomerAccount customerAccount = buildCustomerAccount(PRIVATE_MANAGEMENT_FEE, customer);
                        customerAccountRepository.save(customerAccount);

                        // If a customer is identified, he will receive an email with his credential.
                        // This functionality, however, is out of scope for this project.
                        // sendEmailNotification(customer.getEmail());
                    }
                });
    } */

    private boolean customerIsIdentifiedViaPostIdent(Customer customer) {
        // TODO: call elyes for postident

        return true;
    }

    // TODO: implement management fee booking
    private void bookManagementFee() {
        customerAccountRepository.findById(EDENBANK_ACCOUNT_ID).ifPresent(edenbankAccount -> {
            if (edenbankAccount.getBankAccounts().isEmpty()) {
                return;
            } else {
                BankAccount edenbankCheckingAccount = edenbankAccount.getBankAccounts().get(0);
                customerAccountRepository.findAllByCustomerAccountIdNot(EDENBANK_ACCOUNT_ID)
                        .forEach(customerAccount -> {

                        });
            }
        });

    }
}