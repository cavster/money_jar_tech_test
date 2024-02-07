package repo

import entities.{Customer, CustomerId, Money, Transaction, TransactionId, FromAccountId}

class CustomerRepo {
  private var customers: List[Customer] = List(
    Customer(CustomerId(1), "Arisha Barron"),
    Customer(CustomerId(2), "Branden Gibson"),
    Customer(CustomerId(3), "Rhonda Church"),
    Customer(CustomerId(4), "Georgina Hazel")
  )

  def getById(customerId: CustomerId): Option[Customer] = customers.find(_.id == customerId)

  def save(customer: Customer): Unit = {
    customers = customers :+ customer
  }
}
import entities.{BankAccount, BankAccountId}

class BankAccountRepo {
  var accounts: List[BankAccount] = List.empty
  private var nextAccountId: Int = 1
  def getByBankId(accountId: BankAccountId): Option[BankAccount] = accounts.find(_.bankAccountId == accountId)
  def getByCustomerId(customerId: CustomerId): Option[BankAccount] = accounts.find(_.customerId == customerId)

  def createNewAccount(customerId: CustomerId, initialBalance: Money): BankAccount = {
    val newAccountId = generateNewId()
    val newAccount = BankAccount(newAccountId, customerId, initialBalance)
    save(newAccount)
    newAccount
  }
  // Method to generate a new incremental ID
   def generateNewId(): BankAccountId = {
    val newId = nextAccountId
    nextAccountId += 1
    BankAccountId(newId)
  }
  def updateBalance(accountId: BankAccountId, newBalance: Money): Unit = {
    accounts = accounts.map {
      case account if account.bankAccountId == accountId =>
        account.copy(balance = newBalance)
      case otherBankAccount =>
        otherBankAccount
    }

    println("accounts " + accounts + "nextAccountId " + nextAccountId)
  }
  def save(account: BankAccount): Unit = {
    accounts = accounts :+ account
  }
}

class TransactionRepo {
  private var transactions: List[Transaction] = List.empty

  def save(transaction: Transaction): Unit = {
    transactions = transactions :+ transaction
  }

  def getTransactionsForAccount(bankAccountId: BankAccountId): List[Transaction] = {
    transactions.filter(t => t.fromAccountId == FromAccountId(bankAccountId))
  }

  def getNextTransactionId: TransactionId = TransactionId(transactions.length + 1)
}
