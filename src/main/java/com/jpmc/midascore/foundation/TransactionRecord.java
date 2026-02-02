package com.jpmc.midascore.foundation;

import com.jpmc.midascore.entity.UserRecord;
import jakarta.persistence.*;

@Entity
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private float amount;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private UserRecord sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private UserRecord recipient;

    @Column(nullable = false)
    private float incentive;

    protected TransactionRecord() {}

    public TransactionRecord(float amount, UserRecord sender, UserRecord recipient, float incentive) {
        this.amount = amount;
        this.sender = sender;
        this.recipient = recipient;
        this.incentive = incentive;
    }

    public Long getId() {
        return id;
    }

    public float getAmount() {
        return amount;
    }

    public UserRecord getSender() {
        return sender;
    }

    public UserRecord getRecipient() {
        return recipient;
    }

    public float getIncentive() {
        return incentive;
    }
}
