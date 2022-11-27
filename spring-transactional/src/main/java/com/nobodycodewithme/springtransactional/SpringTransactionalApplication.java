package com.nobodycodewithme.springtransactional;

import com.nobodycodewithme.springtransactional.annotation.EnableCustomRepository;
import com.nobodycodewithme.springtransactional.entity.Greeting;
import com.nobodycodewithme.springtransactional.entity.User;
import com.nobodycodewithme.springtransactional.repository.Repository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
@EnableCustomRepository

@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableAspectJAutoProxy
public class SpringTransactionalApplication implements CommandLineRunner {

    @Autowired
    private Repository<User, Long> userRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringTransactionalApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("");
        Greeting greeting = new Greeting()
                .setId(1L)
                .setMessage("Hello NoBodyCodeWithMe");

        User user = new User()
                .setId(1L)
                .setName("NoBodyCodeWithMe")
                .setGreeting(greeting);

        userRepository.save(user);

        log.info("{}", userRepository.getById(1L));
    }
}
