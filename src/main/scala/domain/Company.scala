package domain
import java.util.UUID


case class CompanyName(value: String) extends AnyVal
case class Revenue(value: Long) extends AnyVal
case class Employees(value: Long) extends AnyVal
case class Company(uuid: UUID, name: Option[CompanyName], revenue: Revenue, amount: Employees) {
  override def toString: String = s"Company(${name.map(_.value).getOrElse("empty")}, ${revenue.value}, ${amount.value})"
}

object Company {
  def apply(name: String, revenue: Long, amount: Int): Company =
    new Company(UUID.randomUUID(), Option(name).map(CompanyName), Revenue(revenue), Employees(amount))
}

case class Companies(values: Set[Company]) {
  def ++(company: Company): Companies = Companies(values + company)
  override def toString: String = values.mkString("\n")
}

object Companies {
  def empty: Companies = Companies(Set.empty[Company])
}

trait PersistanceResult