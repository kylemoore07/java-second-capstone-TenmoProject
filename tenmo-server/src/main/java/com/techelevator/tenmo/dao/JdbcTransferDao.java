package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;
    private AccountDao accountDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate, AccountDao accountDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.accountDao = accountDao;
    }


    @Override
    public List<Transfer> getTransfersByUserId (Long userId) {

        List<Transfer> transfers = new ArrayList<>();

        String sql = "SELECT * FROM transfer t JOIN account a ON a.account_id = t.account_from OR a.account_id = t.account_to WHERE a.user_id = ?";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
        while (rowSet.next()){
            Transfer transfer = mapRowToTransfer(rowSet);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public Transfer getTransferDetails(Long transferId) {

        String sql = "SELECT * FROM transfer WHERE transfer_id = ?";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, transferId);

        try {
        if(rowSet.next()) {
            Transfer transfer = mapRowToTransfer(rowSet);
            return transfer;
        }
//            return jdbcTemplate.queryForRowSet(sql, transferId);
        } catch (EmptyResultDataAccessException ex) {
            System.out.println("Transfer Id Not found");
        }
        return null;
    }


    @Override
    public Transfer createTransfer(Transfer transfer){
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES ('2', '2', ?, ?, ?) RETURNING transfer_id";

        Long transferId = jdbcTemplate.queryForObject(sql, Long.class, transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());

        transfer.setTransferId(transferId);

        return transfer;
    }



    //send money
    @Override
    public void sendMoney(Transfer transfer) {
        String sql = "UPDATE account SET balance = balance - ? WHERE account_id = ?; UPDATE account SET balance = balance + ? WHERE account_id = ?;";
        try {
            jdbcTemplate.update(sql, transfer.getAmount(), transfer.getAccountFrom(), transfer.getAmount(), transfer.getAccountTo());;// transfer.getAccountFrom(), transfer.getAmount(), transfer.getAccountTo());

        } catch(Exception ex){ //think about this...necessary?
            System.out.println(ex.getMessage());
        }
    }



    private Transfer mapRowToTransfer(SqlRowSet rowSet){
        Long transferId = rowSet.getLong("transfer_id");
        int transferType = rowSet.getInt("transfer_type_id");
        int transferStatusId = rowSet.getInt("transfer_status_id");
        Long accountFrom = rowSet.getLong("account_from");
        Long accountTo = rowSet.getLong("account_to");
        BigDecimal amount = rowSet.getBigDecimal("amount");

        return new Transfer(transferId, transferType, transferStatusId, accountFrom, accountTo, amount);

    }
}
