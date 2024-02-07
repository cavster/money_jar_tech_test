package entities

import play.api.libs.json._

// Domain Entities
case class CustomerId(id: Int) extends AnyVal
object CustomerId {
  implicit val format: Format[CustomerId] = Json.format[CustomerId]
}

case class BankAccountId(id: Int) extends AnyVal
object BankAccountId {
  implicit val format: Format[BankAccountId] = Json.format[BankAccountId]
}

case class TransactionId(id: Int) extends AnyVal
object TransactionId {
  implicit val format: Format[TransactionId] = Json.format[TransactionId]
}

case class Money(amountInPennies: Int) {
  def +(other: Money): Money = Money(this.amountInPennies + other.amountInPennies)
  def -(other: Money): Money = Money(this.amountInPennies - other.amountInPennies)
}
object Money {
  implicit val format: Format[Money] = Json.format[Money]
}

case class Customer(id: CustomerId, name: String)
object Customer {
  implicit val format: Format[Customer] = Json.format[Customer]
}

case class BankAccount(bankAccountId: BankAccountId, customerId: CustomerId, balance: Money)
object BankAccount {
  implicit val format: Format[BankAccount] = Json.format[BankAccount]
}

case class FromAccountId(accountId: BankAccountId)
object FromAccountId {
  implicit val format: Format[FromAccountId] = Json.format[FromAccountId]
}

case class ToAccountId(accountId: BankAccountId)
object ToAccountId {
  implicit val format: Format[ToAccountId] = Json.format[ToAccountId]
}

sealed trait TransactionType
case object Debit extends TransactionType
case object Credit extends TransactionType

object TransactionType {
  implicit val format: Format[TransactionType] = new Format[TransactionType] {
    def reads(json: JsValue): JsResult[TransactionType] = json match {
      case JsString("Debit") => JsSuccess(Debit)
      case JsString("Credit") => JsSuccess(Credit)
      case _ => JsError("Invalid TransactionType")
    }

    def writes(transactionType: TransactionType): JsValue = JsString(transactionType.toString)
  }
}

case class Transaction(id: TransactionId, fromAccountId: FromAccountId, toAccountId: ToAccountId, amount: Money, transactionType: TransactionType)
object Transaction {
  implicit val format: Format[Transaction] = Json.format[Transaction]
}

// Requests
case class CreateBankAccountRequest(customerId: CustomerId, initialBalance: Money)
object CreateBankAccountRequest {
  implicit val format: OFormat[CreateBankAccountRequest] = Json.format[CreateBankAccountRequest]
}

case class TransferFundsRequest(fromAccountId: FromAccountId, toAccountId: ToAccountId, amountInPence: Money)
object TransferFundsRequest {
  implicit val format: OFormat[TransferFundsRequest] = Json.format[TransferFundsRequest]
}
