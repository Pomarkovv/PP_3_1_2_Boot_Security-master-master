package ru.kata.spring.boot_security.demo.services;

import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository ur;
    private ApplicationContext context;

    public UserServiceImpl(UserRepository ur, ApplicationContext context) {
        this.ur = ur;
        this.context = context;
    }

    @Override
    public User getUserById(int id) {
        return ur.getUserById(id);
    }

    @Override
    @Transactional
    public void save(User user) {
        setEncryptedPassword(user);
        ur.save(user);
    }

    @Override
    public User getUserByUsername(String username) {
        return ur.getUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserByUsername(username);
    }

    @Override
    public void setEncryptedPassword(User user) {
        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    @Override
    public List<User> getAllUsers() {
        return ur.getAllUsers();
    }

    @Override
    @Transactional
    public void update(int id, User user) {
        ur.update(id, user);
    }

    @Override
    @Transactional
    public void removeUser(int id) {
        ur.remove(id);
    }
}
