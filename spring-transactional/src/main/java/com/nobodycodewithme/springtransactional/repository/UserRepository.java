package com.nobodycodewithme.springtransactional.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nobodycodewithme.springtransactional.annotation.CustomRepository;
import com.nobodycodewithme.springtransactional.annotation.CustomTransactional;
import com.nobodycodewithme.springtransactional.entity.Greeting;
import com.nobodycodewithme.springtransactional.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@CustomRepository("userRepository")
//@CustomTransactional
//@org.springframework.stereotype.Repository
public class UserRepository implements Repository<User, Long> {
    private final Map<Long, User> databaseInMemory = new HashMap<>();

    @Getter
    @Setter
    @Autowired
    private Repository<Greeting, Long> greetingRepository;

    @Override
    @CustomTransactional
    public User save(User user) {
        databaseInMemory.put(user.getId(), user);
        greetingRepository.save(user.getGreeting());

        return user;
    }

    @Override
    @CustomTransactional
    public User getById(Long id) {
        Greeting greeting = greetingRepository.getById(id);
        return databaseInMemory.get(id)
                .setGreeting(greeting);
    }

    @Override
    public User delete(Long id) {
        return databaseInMemory.remove(id);
    }
}
