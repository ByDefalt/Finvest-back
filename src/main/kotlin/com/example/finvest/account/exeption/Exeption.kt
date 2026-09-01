package com.example.finvest.account.exeption

class AccountNotCreatedException :
    RuntimeException("Account not created")

class AccountNotDeleteException :
    RuntimeException("Account not found or you are not the owner of this account")

class AccountNotUpdateException :
    RuntimeException("Account not found or you are not the owner of this account")