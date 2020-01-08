package de.marcoedenhofer.edenbank.application.transactionservice;

public class TransactionData {
    private double amount;
    private String senderIban;
    private String receiverIban;
    private String usageDetails;
    private long senderCustomerAccountId;
    private String senderPassword;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getSenderIban() {
        return senderIban;
    }

    public void setSenderIban(String senderIban) {
        this.senderIban = senderIban;
    }

    public String getReceiverIban() {
        return receiverIban;
    }

    public void setReceiverIban(String receiverIban) {
        this.receiverIban = receiverIban;
    }

    public String getUsageDetails() {
        return usageDetails;
    }

    public void setUsageDetails(String usageDetails) {
        this.usageDetails = usageDetails;
    }

    public long getSenderCustomerAccountId() {
        return senderCustomerAccountId;
    }

    public void setSenderCustomerAccountId(long senderCustomerAccountId) {
        this.senderCustomerAccountId = senderCustomerAccountId;
    }

    public String getSenderPassword() {
        return senderPassword;
    }

    public void setSenderPassword(String senderPassword) {
        this.senderPassword = senderPassword;
    }
}
