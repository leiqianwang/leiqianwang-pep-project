package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDao {

    // Method to get an account by username
    public Account getAccountByUsername(String username) {
        Account account = null;

        try (Connection connection = ConnectionUtil.getConnection()) {
            String query = "SELECT * FROM account WHERE username = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                account = new Account();
                account.setAccount_id(rs.getInt("account_id"));
                account.setUsername(rs.getString("username"));
                account.setPassword(rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return account;
    
    }

   // Method to register a new account
public Account registerAccount(Account account) throws SQLException {
    Account registeredAccount = null;

    try (Connection connection = ConnectionUtil.getConnection()) {
        // Insert the new account into the account table
        String insertQuery = "INSERT INTO account (username, password) VALUES (?, ?)";
        PreparedStatement insertSt = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);
        insertSt.setString(1, account.getUsername());
        insertSt.setString(2, account.getPassword());

        int rowsAffected = insertSt.executeUpdate();

        if (rowsAffected > 0) {
            ResultSet generatedKeys = insertSt.getGeneratedKeys();
            if (generatedKeys.next()) {
                account.setAccount_id(generatedKeys.getInt(1));
                registeredAccount = account;
            }
        }
    }

    return registeredAccount;
}

// Method to get an account by account_id
public Account getAccountById(int accountId) {
    Account account = null;

    try (Connection connection = ConnectionUtil.getConnection()) {
        String query = "SELECT * FROM account WHERE account_id = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, accountId);  // Set the account_id
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            account = new Account();
            account.setAccount_id(rs.getInt("account_id"));
            account.setUsername(rs.getString("username"));
            account.setPassword(rs.getString("password"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return account;
}

}
