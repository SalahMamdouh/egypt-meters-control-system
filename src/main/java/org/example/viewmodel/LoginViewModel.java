package org.example.viewmodel;

import org.example.service.SecureStorageService;

public class LoginViewModel {
    private final SecureStorageService secureStorageService;

    public LoginViewModel(SecureStorageService secureStorageService) {
        this.secureStorageService = secureStorageService;
    }

    public boolean login(String username, String password) {
        if (username == null || username.isBlank()) {
            return false;
        }

        if (password == null || password.isBlank()) {
            return false;
        }

        return secureStorageService.authenticate(username.trim(), password);
    }
}