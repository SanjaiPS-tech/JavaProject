package com.SubNetwork.JavaProject.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String paymentNumber;

    @Column(nullable = false)
    private String paymentType; // "CUSTOMER_PAYMENT" or "VENDOR_PAYMENT"

    @Column(nullable = false)
    private String referenceNumber; // invoiceNumber or billNumber

    @Column(nullable = false)
    private String partnerName; // Customer or Vendor name

    @Column(nullable = false)
    private Double amount;

    private String paymentMethod = "BANK"; // "BANK" or "CASH"

    private LocalDate paymentDate = LocalDate.now();

    public Payment() {
    }

    public Payment(String paymentNumber, String paymentType, String referenceNumber,
            String partnerName, Double amount, String paymentMethod) {
        this.paymentNumber = paymentNumber;
        this.paymentType = paymentType;
        this.referenceNumber = referenceNumber;
        this.partnerName = partnerName;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }
}
