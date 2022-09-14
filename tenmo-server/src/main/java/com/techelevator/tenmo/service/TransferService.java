package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.SecureUserDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.NotAccountOwnerException;
import com.techelevator.tenmo.model.SecureUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

@Component
public class TransferService {

    private TransferDao transferDao;
    private SecureUserDao secureUserDao;
    private AccountService accountService;
    private AccountDao accountDao;


    public TransferService(TransferDao transferDao, SecureUserDao secureUserDao, AccountService accountService, AccountDao accountDao) {
        this.transferDao = transferDao;
        this.secureUserDao = secureUserDao;
        this.accountService = accountService;
        this.accountDao = accountDao;
    }

    public List<SecureUser> secureUserList() {
        return secureUserDao.listOfOtherUsers();
    }

    public List<Transfer> getTransferList(Long userId, String username) {
        accountService.validateAccountOwner(userId, username);
        return transferDao.getTransfersByUserId(userId);
    }   

    public Transfer getTransferByTransferId(Long userId, Long transferId, String username) {
        accountService.validateAccountOwner(userId, username);
        return transferDao.getTransferDetails(transferId);
    }


    public Transfer createTransfer(Transfer transfer, Long userId, String username) {
        accountService.validateAccountOwner(userId, username);
        return transferDao.createTransfer(transfer);
    }

    public void sendMoney(Transfer transfer, Long userId, String username) {
        accountService.validateAccountOwner(userId, username);
        transferDao.sendMoney(transfer);
    }


//TODO wrap all of these in Transactional Exception

    @Transactional
    public void executeTransaction(Long userId, Transfer transfer, String username) throws NotAccountOwnerException {

        Long fromAccountId = accountDao.getAccountByUserId(transfer.getAccountFrom());
        Long toAccountId = accountDao.getAccountByUserId(transfer.getAccountTo());
        transfer.setAccountFrom(fromAccountId);
        transfer.setAccountTo(toAccountId);

        createTransfer(transfer, userId, username);

        BigDecimal accountBalance = accountDao.getAccountBalance(username);

        if (transfer.getAmount().compareTo(accountDao.getAccountBalance(username)) <= 0) {
            if (!transfer.getAccountFrom().equals(transfer.getAccountTo())) {
                sendMoney(transfer, userId, username);
            }

//    public void validateAndSend(BigDecimal amount, Long accountTo, Long accountFrom, AuthenticatedUser user) {
//        AccountService accountService = new Acco untService(baseUrl);
//        MathContext mc = new MathContext(2);
//        Long userId = user.getUser().getId();
//        BigDecimal currentUserBalance = accountService.getBalance(userId, user);
//        BigDecimal receiverUserBalance = accountService.getUserBalance(accountTo);
//        if (currentUserBalance.compareTo(amount) == 0 || currentUserBalance.compareTo(amount) == 1) {
//            if(!accountTo.equals(accountFrom)) {
//                currentUserBalance.subtract(amount, mc);
//                receiverUserBalance.add(amount, mc);
//            }
        }

    }
}

