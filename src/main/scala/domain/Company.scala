package domain
import java.util.UUID


case class CompanyName(value: String) extends AnyVal
case class Revenue(value: Long)       extends AnyVal
case class Employees(value: Long)     extends AnyVal
case class Average(value: Long)       extends AnyVal

case class Company(uuid: UUID,
                   name: Option[CompanyName],
                   revenue: Revenue,
                   amount: Employees) {
  override def toString: String = s"Company(${name.map(_.value).getOrElse("empty")}, ${revenue.value}, ${amount.value})"
}

case class CompanyWithAverage(uuid: UUID,
                              name: Option[CompanyName],
                              revenue: Revenue,
                              amount: Employees,
                              average: Average) {
  override def toString(): String =
    s"name: ${name.map(_.value).getOrElse("")}, revenue: ${revenue.value}, amount: ${amount.value}, average: ${average.value}"
}

object Company {
  def apply(name: String, revenue: Long, amount: Int): Company =
    new Company(
      uuid = UUID.randomUUID(),
      name = Option(name).map(CompanyName),
      revenue = Revenue(revenue),
      amount = Employees(amount))
}
