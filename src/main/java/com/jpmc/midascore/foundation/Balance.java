package com.jpmc.midascore.foundation;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Balance {

    @Id
    private String id;   // ← userId like "waldorf"

    private double amount;

    public String getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
