package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.exception.NotAccountOwnerException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.service.TransferService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private TransferService transferService;

    public TransferController (TransferService transferService){
        this.transferService = transferService;
    }

    //list of users
    @PostMapping("/user/{userid}/transfers")
    public Transfer createUserTransfer(@PathVariable Long userid, @RequestBody Transfer transfer, Principal user) throws NotAccountOwnerException{
        return transferService.createTransfer(transfer, userid, user.getName());
    }

    //send money
    @PutMapping("/user/{userid}/send") //may need to add more path variables
    public void sendMoney(@PathVariable Long userid, @RequestBody Transfer transfer, Principal user) throws  NotAccountOwnerException{
//        transferService.sendMoney(transfer, userid, user.getName());
        transferService.executeTransaction(userid, transfer, user.getName());
    }

    //list of transfers

    @PreAuthorize("permitAll")
    @GetMapping("/user/{userid}/transfers/list")
    public List<Transfer> viewListOfTransfers(@PathVariable Long userid, Principal user){
        return transferService.getTransferList(userid, user.getName());
    }

    // transfer details
    @GetMapping("/user/{userid}/transfers/{transferid}")
    public Transfer viewTransfer(@PathVariable Long userid, @PathVariable Long transferid, Principal user) throws NotAccountOwnerException{
        return transferService.getTransferByTransferId(userid, transferid, user.getName());
    }

}
