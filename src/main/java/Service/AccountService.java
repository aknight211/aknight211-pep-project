package Service;
import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        this.accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account register(Account account){
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()){
            return null;
        }
        if(account.getPassword() == null || account.getPassword().length() < 4){
            return null;
        }
        Account exist = accountDAO.logged(account.getUsername(), account.getPassword());
        if(exist != null){
            return null;
        }
        return accountDAO.register(account);
    }

    public Account logged(Account account){
        if(account.getUsername() == null || account.getPassword() == null){
            return null;
        }
        return accountDAO.logged(account.getUsername(), account.getPassword());
    }

    public Account getAccountById(int account_id){
        return accountDAO.findById(account_id);
    }
}
