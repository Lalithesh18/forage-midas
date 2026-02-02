package com.jpmc.midascore;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class WaldorfBalanceChecker {
    
    @Autowired
    private UserRepository userRepository;
    
    public float getWaldorfBalance() {
        Optional<UserRecord> waldorfOpt = userRepository.findByName("waldorf");
        if (waldorfOpt.isPresent()) {
            return waldorfOpt.get().getBalance();
        }
        return -1;
    }
}
