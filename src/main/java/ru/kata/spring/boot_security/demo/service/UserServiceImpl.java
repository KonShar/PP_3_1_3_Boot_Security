package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }



    @Override
    public void saveUser(User user) {
        Optional<User> userFromBD = userRepository.findUserByName(user.getName());
        if (userFromBD.isPresent()) {
            throw new RuntimeException(String.format("User %s already exists", user.getUsername()));
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(int id) {
        return userRepository.getById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByName(String name) {
        return userRepository.findUserByName(name).get();
    }

    @Override
    public void updateUser(int id, User user) {
        Optional<User> updatedUser = userRepository.findById(id);
        user.setPassword(updatedUser.get().getPassword());
        userRepository.save(user);
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

}
