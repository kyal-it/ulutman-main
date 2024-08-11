package com.ulutman.service;

import com.ulutman.model.entities.Admin;
import com.ulutman.model.enums.ServiceRole;
import com.ulutman.repository.AdminRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminService {

    private final AdminRepository adminRepository;
    private  final PasswordEncoder passwordEncoder;

    public Admin createAdmin(String username, String password, Set<ServiceRole> roles) {
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(password)); // Убедитесь, что пароль хэшируется перед сохранением
        admin.setRoles(roles);

        return adminRepository.save(admin);
    }

    public Optional<Admin> findAdminByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    public void updateAdminRoles(String username, Set<ServiceRole> roles) {
        Optional<Admin> adminOpt = findAdminByUsername(username);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            System.out.println("Updating roles for admin: " + admin.getUsername());
            admin.setRoles(roles);
            adminRepository.save(admin);
            System.out.println("Roles updated: " + admin.getRoles());
        } else {
            System.out.println("Admin not found with username: " + username);
        }
    }
}
