package org.example.groenfroebackend.service;

import lombok.RequiredArgsConstructor;
import org.example.groenfroebackend.model.Enums.Role;
import org.example.groenfroebackend.model.Enums.JobTitle;
import org.example.groenfroebackend.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.example.groenfroebackend.model.User;

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

}