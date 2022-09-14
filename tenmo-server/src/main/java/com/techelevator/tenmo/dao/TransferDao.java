package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    List<Transfer> getTransfersByUserId (Long userId);

    Transfer getTransferDetails (Long transferId);

    Transfer createTransfer(Transfer transfer);

    void sendMoney(Transfer transfer);

}
