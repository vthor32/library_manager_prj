package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.*;

public class AuthManager {
    private List<User> users;
    private Gson gson;

    public AuthManager() {
        users = new ArrayList<>();
        gson = new GsonBuilder()
                .registerTypeAdapter(Role.class, new RoleAdapter())
                .setPrettyPrinting()
                .create();
        loadUsers();
    }

    public List<User> getUsers() {
        return users;
    }

    public boolean addUser(User user) {
        for (User existingUser : users) {
            if (existingUser.getUsername().equals(user.getUsername())) {
                return false; // Username already exists
            }
        }
        users.add(user);
        saveUsers();
        return true;
    }

    public void deleteUser(String username) {
        users.removeIf(user -> user.getUsername().equals(username));
        saveUsers();
    }

    public User authenticate(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public void saveUsers() {
        try (Writer writer = new FileWriter("users.json")) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadUsers() {
        try (Reader reader = new FileReader("users.json")) {
            User[] loadedUsers = gson.fromJson(reader, User[].class);
            if (loadedUsers != null) {
                users.clear();
                users.addAll(Arrays.asList(loadedUsers));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}