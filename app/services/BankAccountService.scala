package services

import javax.inject.Inject
import entities._
import repo.{BankAccountRepo, CustomerRepo, TransactionRepo}

class BankAccountService @Inject()(
                                    customerRepository: CustomerRepo,
                                    bankAccountRepo: BankAccountRepo,
                                    transactionRepo: TransactionRepo
                                  ) {

  def createAccount(customerId: CustomerId, initialDeposit: Money): BankAccount = {
    val account = BankAccount(bankAccountRepo.generateNewId(), customerId, initialDeposit)
    bankAccountRepo.save(account)
    account
  }

  def transferFunds(fromAccountId: FromAccountId, toAccountId: ToAccountId, amount: Money): Option[Transaction] = {
    val fromAccountOpt = bankAccountRepo.getByBankId(fromAccountId.accountId)
    val toAccountOpt = bankAccountRepo.getByBankId(toAccountId.accountId)

    (fromAccountOpt, toAccountOpt) match {
      case (Some(fromAccount), Some(toAccount)) =>
        if (fromAccount.balance.amountInPennies >= amount.amountInPennies) {
          val transaction = Transaction(
            transactionRepo.getNextTransactionId,
            fromAccountId,
            toAccountId,
            amount,
            Debit
          )
          transactionRepo.save(transaction)

          // Create and save a mirrored transaction for the destination account
          val mirroredTransaction = Transaction(
            transactionRepo.getNextTransactionId,
            FromAccountId(toAccountId.accountId),
            ToAccountId(fromAccountId.accountId),
            amount,
            Credit
          )
          transactionRepo.save(mirroredTransaction)

          bankAccountRepo.updateBalance(fromAccount.bankAccountId, fromAccount.balance - amount)
          bankAccountRepo.updateBalance(toAccount.bankAccountId, toAccount.balance + amount)

          Some(transaction)
        } else {
          None // Insufficient funds in the source account
        }

      case _ =>
        None // One or both of the accounts do not exist
    }
  }

  def checkBalance(bankAccountId: BankAccountId): Option[BankAccount] = {
    bankAccountRepo.getByBankId(bankAccountId)
  }

  def getTransactionsForAnAccount(bankAccountId: BankAccountId): List[Transaction] = {
    transactionRepo.getTransactionsForAccount(bankAccountId)
  }
}
