package com.jpmc.midascore.services;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.foundation.TransactionRecord;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;
    private final IncentiveService incentiveService;

    public TransactionService(UserRepository userRepository,
                              TransactionRecordRepository transactionRecordRepository,
                              IncentiveService incentiveService) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
        this.incentiveService = incentiveService;
    }

    public void process(Transaction transaction) {
        // Validate senderId exists - use Long to call CrudRepository's findById
        Optional<UserRecord> senderOpt = userRepository.findById(Long.valueOf(transaction.getSenderId()));
        if (senderOpt.isEmpty()) {
            return; // invalid senderId
        }

        // Validate recipientId exists - use Long to call CrudRepository's findById
        Optional<UserRecord> recipientOpt = userRepository.findById(Long.valueOf(transaction.getRecipientId()));
        if (recipientOpt.isEmpty()) {
            return; // invalid recipientId
        }

        UserRecord sender = senderOpt.get();
        UserRecord recipient = recipientOpt.get();

        // Validate sender has sufficient balance
        if (sender.getBalance() < transaction.getAmount()) {
            return; // insufficient funds
        }

        // Get incentive from API
        Incentive incentive = incentiveService.getIncentive(transaction);
        float incentiveAmount = incentive != null ? incentive.getAmount() : 0.0f;

        // Update balances
        // Deduct transaction amount from sender
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        // Add transaction amount and incentive to recipient (incentive not deducted from sender)
        recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentiveAmount);

        // Persist transaction with relationships and incentive
        TransactionRecord record = new TransactionRecord(
                transaction.getAmount(),
                sender,
                recipient,
                incentiveAmount
        );

        transactionRecordRepository.save(record);
        userRepository.save(sender);
        userRepository.save(recipient);
    }
}
