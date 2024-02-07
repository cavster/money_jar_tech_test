package controllers



import entities.{BankAccount, BankAccountId, CreateBankAccountRequest, Credit, CustomerId, Debit, FromAccountId, Money, ToAccountId, Transaction, TransactionId, TransferFundsRequest}
import org.scalatest.funsuite.AnyFunSuite
import models._
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.test.Helpers._
import play.api.test._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.AnyContentAsEmpty

class BankAccountControllerSpec extends PlaySpec with GuiceOneAppPerSuite {


  private val createAccountEndpoint = "/createAccount"
  private val transactionsEndpoint = "/transactions"
  private val balanceEndpoint = "/balance"
  private val transfersEndpoint = "/accounts"

  private def makeFakeRequest(method: String, endpoint: String, body: Option[JsValue] = None) = {
    FakeRequest(method, endpoint).withBody(body.getOrElse(Json.obj()))
  }

  private def createAccount(requestJson: JsValue, expectedResult: BankAccount): Unit = {
    val fakeRequest = makeFakeRequest(POST, createAccountEndpoint, Some(requestJson))
    val result = route(app, fakeRequest).get
    status(result) mustBe OK
    contentType(result) mustBe Some("application/json")
    contentAsJson(result) mustBe Json.toJson(expectedResult)
  }
  private def createTransaction(requestJson: JsValue, expectedResult: Transaction): Unit = {
    val fakeRequest = makeFakeRequest(POST, transactionsEndpoint, Some(requestJson))
    val result = route(app, fakeRequest).get
    status(result) mustBe OK
    contentType(result) mustBe Some("application/json")
    contentAsJson(result) mustBe Json.toJson(expectedResult)
  }

  private def retrieveBalance(accountId: BankAccountId, expectedResult: BankAccount): Unit = {
    val fakeRequest = makeFakeRequest(GET, s"$balanceEndpoint/${accountId.id}")
    val result = route(app, fakeRequest).get
    status(result) mustBe OK
    contentType(result) mustBe Some("application/json")
    contentAsJson(result) mustBe Json.toJson(expectedResult)
  }

  private def retrieveTransfers(accountId: BankAccountId, expectedResult: List[Transaction]): Unit = {
    val fakeRequest = makeFakeRequest(GET, s"$transfersEndpoint/${accountId.id}/transactions")
    val result = route(app, fakeRequest).get
    status(result) mustBe OK
    contentType(result) mustBe Some("application/json")
    contentAsJson(result) mustBe Json.toJson(expectedResult)
  }
  "BankAccountController" should {

    "Create a Bank account for a customer" in {
      val createRequest = CreateBankAccountRequest(CustomerId(1), Money(1000))
      createAccount(Json.toJson(createRequest), BankAccount(BankAccountId(1), CustomerId(1), Money(1000)))
    }

    "A single Customer can have multiply bank accounts " in {
      val createRequest = CreateBankAccountRequest(CustomerId(1), Money(500))
      createAccount(Json.toJson(createRequest), BankAccount(BankAccountId(2), CustomerId(1), Money(500)))
    }

    "Customers can send money to each other " in {

      createAccount(Json.toJson(CreateBankAccountRequest(CustomerId(3), Money(500))), BankAccount(BankAccountId(3), CustomerId(3), Money(500)))

      val createTransactionRequest = TransferFundsRequest(FromAccountId(BankAccountId(1)), ToAccountId(BankAccountId(3)), Money(200))
      createTransaction(Json.toJson(createTransactionRequest), Transaction(TransactionId(1), FromAccountId(BankAccountId(1)), ToAccountId(BankAccountId(3)), Money(200), Debit))
    }
    "Retrieve balances for a given account." in {
      retrieveBalance(BankAccountId(1), BankAccount(BankAccountId(1), CustomerId(1), Money(800)))
    }

    "Show a list of all transactions from" in {
      retrieveTransfers(BankAccountId(1), List(Transaction(TransactionId(1), FromAccountId(BankAccountId(1)), ToAccountId(BankAccountId(3)), Money(200), Debit)))
    }
    "Show a list of all transactions to" in {
      retrieveTransfers(BankAccountId(3), List(Transaction(TransactionId(2), FromAccountId(BankAccountId(3)), ToAccountId(BankAccountId(1)), Money(200), Credit)))
    }

    }
  }


