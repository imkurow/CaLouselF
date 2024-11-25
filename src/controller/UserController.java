package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import config.DatabaseConnection;
import model.User;

public class UserController {
    // Validation methods
    public static boolean validateUsername(String username) {
        return username != null && username.length() >= 3;
    }
    
    public static boolean validatePassword(String password) {
        return password != null && password.length() >= 8 && password.matches(".*[!@#$%^&*].*");
    }
    
    public static boolean validatePhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.startsWith("+62") && phoneNumber.length() >= 11;
    }
    
    public static boolean validateAddress(String address) {
        return address != null && !address.trim().isEmpty();
    }
    
    // Registration
    public static String registerUser(String username, String password, String phoneNumber, String address, String role) {
        if (!validateUsername(username) || !validatePassword(password) || 
            !validatePhoneNumber(phoneNumber) || !validateAddress(address)) {
            return "Invalid input data";
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check if username exists
            String checkSql = "SELECT username FROM users WHERE username = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                return "Username already exists";
            }
            
            // Insert new user
            String userId = UUID.randomUUID().toString();
            String sql = "INSERT INTO users (userId, username, password, phoneNumber, address, role) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            pstmt.setString(4, phoneNumber);
            pstmt.setString(5, address);
            pstmt.setString(6, role);
            
            pstmt.executeUpdate();
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Database error";
        }
    }
    
    // Login
    public static User login(String username, String password) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new User(
                    rs.getString("userId"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("phoneNumber"),
                    rs.getString("address"),
                    rs.getString("role")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}