package com.nobodycodewithme.springtransactional.repository;

import com.nobodycodewithme.springtransactional.annotation.CustomRepository;
import com.nobodycodewithme.springtransactional.annotation.CustomTransactional;
import com.nobodycodewithme.springtransactional.entity.Greeting;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@CustomRepository("greetingRepository")
//@CustomTransactional
//@org.springframework.stereotype.Repository
public class GreetingRepository implements Repository<Greeting, Long> {
    private final Map<Long, Greeting> databaseInMemory = new HashMap<>();

    @Override
    @CustomTransactional
    public Greeting save(Greeting entity) {
        databaseInMemory.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Greeting getById(Long id) {
        return databaseInMemory.get(id);
    }

    @Override
    @CustomTransactional
    public Greeting delete(Long id) {
        return databaseInMemory.remove(id);
    }
}
