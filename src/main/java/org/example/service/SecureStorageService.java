package org.example.service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Properties;

public class SecureStorageService {

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "qwe";

    private static final int SALT_LENGTH = 16;
    private static final int ITERATIONS = 120_000;
    private static final int KEY_LENGTH = 256;

    private final Path storagePath;

    public SecureStorageService() {
        String userHome = System.getProperty("user.home");

        Path appDirectory = Paths.get(userHome, ".egypt-meters-control-system");

        try {
            Files.createDirectories(appDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create secure storage directory.", e);
        }

        storagePath = appDirectory.resolve("credentials.properties");

        initializeStorage();
    }

    private void initializeStorage() {
        if (Files.exists(storagePath)) {
            return;
        }

        Properties properties = new Properties();

        properties.setProperty("username", DEFAULT_USERNAME);

        properties.setProperty("salt", generateSalt());

        properties.setProperty("passwordHash", hashPassword(DEFAULT_PASSWORD, properties.getProperty("salt")));

        saveProperties(properties);
    }

    public boolean authenticate(String username, String password) {
        Properties properties = loadProperties();

        String storedUsername = properties.getProperty("username");

        String storedSalt = properties.getProperty("salt");

        String storedHash = properties.getProperty("passwordHash");

        if (storedUsername == null || storedSalt == null || storedHash == null) {
            return false;
        }

        if (!storedUsername.equals(username)) {
            return false;
        }

        String enteredHash = hashPassword(password, storedSalt);

        return enteredHash.equals(storedHash);
    }

    public boolean isCurrentPassword(String password) {
        Properties properties = loadProperties();

        String storedSalt = properties.getProperty("salt");

        String storedHash = properties.getProperty("passwordHash");

        if (storedSalt == null || storedHash == null) {
            return false;
        }

        String enteredHash = hashPassword(password, storedSalt);

        return enteredHash.equals(storedHash);
    }

    public void changePassword(String newPassword) {
        Properties properties = loadProperties();

        String newSalt = generateSalt();

        properties.setProperty("salt", newSalt);

        properties.setProperty("passwordHash", hashPassword(newPassword, newSalt));

        saveProperties(properties);
    }

    private String generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];

        SecureRandom secureRandom = new SecureRandom();

        secureRandom.nextBytes(salt);

        return Base64.getEncoder().encodeToString(salt);
    }

    private String hashPassword(String password, String saltBase64) {
        try {
            byte[] salt = Base64.getDecoder().decode(saltBase64);

            PBEKeySpec spec = new PBEKeySpec(
                    password.toCharArray(),
                    salt,
                    ITERATIONS,
                    KEY_LENGTH
            );

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            byte[] hash = factory.generateSecret(spec).getEncoded();

            spec.clearPassword();

            return Base64.getEncoder().encodeToString(hash);

        } catch (Exception e) {
            throw new RuntimeException("Unable to hash password.", e);
        }
    }

    private Properties loadProperties() {
        Properties properties = new Properties();

        try {
            if (Files.exists(storagePath)) {
                try (var inputStream = Files.newInputStream(storagePath)) {
                    properties.load(inputStream);
                }
            }

            return properties;

        } catch (IOException e) {
            throw new RuntimeException("Unable to read secure storage.", e);
        }
    }

    private void saveProperties(Properties properties) {
        try {
            try (var outputStream = Files.newOutputStream(storagePath)) {
                properties.store(outputStream, "Egypt Meters Control System");
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to save secure storage.", e);
        }
    }
}