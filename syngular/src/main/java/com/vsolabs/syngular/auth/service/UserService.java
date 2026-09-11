package com.vsolabs.syngular.auth.service;

import com.vsolabs.syngular.auth.model.Role;
import com.vsolabs.syngular.auth.model.User;
import com.vsolabs.syngular.auth.repository.RoleRepository;
import com.vsolabs.syngular.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public User registerNewUser(User user, String roleName) {
        // Usamos la interfaz RoleRepository para buscar el rol
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Error: El rol '" + roleName + "' no existe en la base de datos."));

        // Asignamos el rol al usuario
        user.setRole(role);

        // Usamos la interfaz UserRepository para guardar en MySQL
        return userRepository.save(user);
    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}