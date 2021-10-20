package com.joao.victor.parcel.simulator.v1.seed;

import com.joao.victor.parcel.simulator.v1.entities.UserEntity;
import com.joao.victor.parcel.simulator.v1.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

@Slf4j
@Configuration
public class LoadData implements CommandLineRunner {

    @Autowired private UserRepository userRepository;

    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void run(String... args) throws Exception {

        String login = "admin";
        if(!userRepository.findByLogin(login).isPresent()) {
            UserEntity user = new UserEntity();
            user.setName("admin");
            user.setLogin("admin");
            user.setCreatedAt(LocalDateTime.now());
            user.setPassword(bCryptPasswordEncoder.encode("admin1234"));
            userRepository.save(user);
            log.info("Admin user being registered...");
        }
        log.info("Admin user successfully registered!");
    }

}
