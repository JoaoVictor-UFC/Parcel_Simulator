package com.joao.victor.parcel.simulator.v1.service;

import com.joao.victor.parcel.simulator.errorExceptions.ResourceBadRequestException;
import com.joao.victor.parcel.simulator.v1.dtos.CreateUserRequest;
import com.joao.victor.parcel.simulator.v1.dtos.UserResponse;
import com.joao.victor.parcel.simulator.v1.entities.UserEntity;
import com.joao.victor.parcel.simulator.v1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired private UserRepository userRepository;

    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserEntity saveUser(UserEntity user) {
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Transactional
    public UserResponse createUser(@Valid @NotNull CreateUserRequest req){

        if (checkLogin(req.getLogin())){
            throw new ResourceBadRequestException("Login existing");
        }

        UserEntity res = new UserEntity();
        res.setLogin(req.getLogin());
        res.setPassword(bCryptPasswordEncoder.encode(req.getPassword()));
        res.setName(req.getName());

        return fromUserEntityToUserResponse(saveUser(res));
    }

    public boolean checkLogin(@Valid @NotNull String login){
        Optional<UserEntity> user = userRepository.findByLogin(login);
        if (user.isPresent()){
            return true;
        }
        return false;
    }

    public UserResponse fromUserEntityToUserResponse(@Valid @NotBlank UserEntity user) {
        userRepository.findById(user.getId());
        UserResponse res = new UserResponse();

        res.setId(user.getId());
        res.setLogin(user.getLogin());
        res.setName(user.getName());

        return res;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findByLogin(login);
        if (!user.isPresent())
            throw new UsernameNotFoundException("Usuário não encontrado, por favor cheque suas credenciais.");
        return new User(user.get().getLogin(), user.get().getPassword(),
                true, true, true, true, new ArrayList<>());
    }

    public @Valid @NotBlank UserEntity findUserByLogin(String login) {
        return userRepository.findByLogin(login).get();
    }
}
