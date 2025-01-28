package com.example.service;

import com.example.dto.UserDto;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Načtení všech uživatelů jako UserDto
    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserDto(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        null,
                        user.getRole().getName() // Vrátí roli
                ))
                .toList();
    }

    // Načtení uživatele podle ID
    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // Načtení uživatele podle emailu
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    // Uložení nového uživatele
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // Vyhledání nebo vytvoření role
        String roleName = userDto.getRole(); // Název role z UserDto (typ String)
        Role role = roleRepository.findByName(roleName); // Vyhledáme roli podle názvu
        if (role == null) {
            // Pokud role neexistuje, vytvoříme ji
            role = new Role();
            role.setName(roleName);
            roleRepository.save(role); // Uložíme novou roli
        }
        user.setRole(role); // Přiřazení objektu Role do entity User

        // Uložení uživatele do databáze
        userRepository.save(user);
    }


    // Smazání uživatele podle ID
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    // Aktualizace uživatele
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setRole(updatedUser.getRole());
            return userRepository.save(existingUser);
        }).orElse(null);
    }

    // Převod UserDto na User
    public List<User> convertToUserList(List<UserDto> userDtos) {
        return userDtos.stream()
                .map(dto -> {
                    User user = new User();
                    user.setId(dto.getId());
                    user.setEmail(dto.getEmail());
                    user.setName(dto.getName());
                    return user;
                })
                .collect(Collectors.toList());
    }
}