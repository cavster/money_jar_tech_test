package controllers

import javax.inject._
import play.api.libs.json.Json
import play.api.mvc._
import entities.{BankAccount, BankAccountId, CreateBankAccountRequest, CustomerId, Money, TransferFundsRequest}
import services.BankAccountService

/**
 * This controller handles bank account-related actions.
 */
@Singleton
class BankAccountController @Inject()(cc: ControllerComponents,
                                      bankAccountService: BankAccountService) extends AbstractController(cc) {

  /**
   * Action to create a bank account.
   */
  def createAccount = Action(parse.json[CreateBankAccountRequest]) { request =>
    val createRequest = request.body
    val bankAccount = bankAccountService.createAccount(createRequest.customerId, createRequest.initialBalance)
    Ok(Json.toJson(bankAccount))
  }

  /**
   * Action to transfer funds between bank accounts.
   */
  def transferAmounts = Action(parse.json[TransferFundsRequest]) { request =>
    val createRequest = request.body
    val transferFunds = bankAccountService.transferFunds(createRequest.fromAccountId, createRequest.toAccountId, createRequest.amountInPence)
    Ok(Json.toJson(transferFunds))
  }

  /**
   * Action to check the balance of a bank account.
   */
  def checkBalance(accountId: BankAccountId) = Action {
    val bankAccount = bankAccountService.checkBalance(accountId)
    Ok(Json.toJson(bankAccount))
  }

  /**
   * Action to retrieve transactions for a bank account.
   */
  def getTransactions(accountId: BankAccountId) = Action {
    val transactions = bankAccountService.getTransactionsForAnAccount(accountId)
    Ok(Json.toJson(transactions))
  }
}
