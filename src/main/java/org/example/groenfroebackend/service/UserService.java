package org.example.groenfroebackend.service;

import lombok.RequiredArgsConstructor;
import org.example.groenfroebackend.model.Enums.Role;
import org.example.groenfroebackend.model.Enums.JobTitle;
import org.example.groenfroebackend.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.example.groenfroebackend.model.User;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private static final String EXISTING_USERNAME = "UserTest";
    //TODO skal laves om til database
    //dette er en hardcodet bruger
    public User getUserByEmailHardcoded(String username) {
        if (! EXISTING_USERNAME.equals(username)) {
            throw new UsernameNotFoundException("User not found");
        }

        User user = new User();
        user.setId(1);
        user.setEmail(EXISTING_USERNAME);
        user.setPassword("$2a$12$G20tn3gThoAyHKBp6nJyh.SCUivWS7MN5qtc6AU687vkfiOSiSA9C"); // password: "test"
        user.setRole(Role.USER);
        user.setJobTitle(JobTitle.IN_STORE_TRAINER);
        return user;
    }

    public User getUserByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email);
    }

    public User createUser (User user) {
        return userRepository.save(user);
    }

    public User editUser(String email, User updatedUser) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));

        if (user.isPresent()) {
            User currentUser = user.get();

            currentUser.setName(updatedUser.getName());
            currentUser.setEmail(updatedUser.getEmail());
            currentUser.setPhoneNumber(updatedUser.getPhoneNumber());
            currentUser.setStoreId(updatedUser.getStoreId());
            currentUser.setJobTitle(updatedUser.getJobTitle());
            currentUser.setRole(updatedUser.getRole());

            return userRepository.save(updatedUser);
        }else {
            throw new RuntimeException();
        }
    }
}