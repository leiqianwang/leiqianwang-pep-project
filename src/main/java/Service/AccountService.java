package Service;

import DAO.AccountDao;
import Model.Account;

import java.sql.SQLException;

public class AccountService {

    AccountDao accountDao;

    public AccountService() {
        this.accountDao = new AccountDao();
    }

    // Method to register a new account
    public Account registerAccount(Account account) throws SQLException {
        // Validate if the username is not blank
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            return null; // Username cannot be blank
        }
        // Validate if the password is at least 4 characters long
        if (account.getPassword().length() < 4) {
            return null; // Password is too short
        }
        // Directly check if the username already exists
        Account existingAccount = accountDao.getAccountByUsername(account.getUsername());
        if (existingAccount != null) {
            return null; // Username already exists
        }

        // Proceed with registration
        return accountDao.registerAccount(account);
    }

    // Method for user login
    public Account loginAccount(String username, String password) {
       // Fetch the account by username
       Account account = accountDao.getAccountByUsername(username);
       // Check if the account exists and if the provided password matches the stored password
       if (account != null && account.getPassword().equals(password)) {
           return account; // Return the account if credentials are correct: provided password matches the one stored in the database.
       }
       return null; // Return null if login credentials are invalid
   }
}
