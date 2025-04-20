package com.etms.model;

public class PaymentMethod {
    private int paymentMethodId;
    private int paymentId;
    private MethodType methodType;
    private String upiId;
    private String bankName;
    private String creditCardNumber;

    public enum MethodType {
        CreditCard,
        UPI,
        BankTransfer
    }

    public PaymentMethod() {
    }

    public PaymentMethod(int paymentMethodId, int paymentId, MethodType methodType,
            String upiId, String bankName, String creditCardNumber) {
        this.paymentMethodId = paymentMethodId;
        this.paymentId = paymentId;
        this.methodType = methodType;
        this.upiId = upiId;
        this.bankName = bankName;
        this.creditCardNumber = creditCardNumber;
    }

    // Getters and Setters
    public int getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(int paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public MethodType getMethodType() {
        return methodType;
    }

    public void setMethodType(MethodType methodType) {
        this.methodType = methodType;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    @Override
    public String toString() {
        return "PaymentMethod{" +
                "paymentMethodId=" + paymentMethodId +
                ", paymentId=" + paymentId +
                ", methodType=" + methodType +
                ", upiId='" + upiId + '\'' +
                ", bankName='" + bankName + '\'' +
                ", creditCardNumber='" + creditCardNumber + '\'' +
                '}';
    }
}