package dao

import domain.{Average, Company, CompanyName, CompanyWithAverage, Employees, Revenue}
import DbConnection._

import java.sql.ResultSet
import java.util.UUID
import scala.annotation.tailrec
import scala.util.Try



trait CompanyRepository {


  def initSql(): Either[Throwable, Unit] =
    for {
      _ <- dropTable()
      _ <- createSchema("records")
      _ <- createTable()
    } yield ()



  def dropTable(): Either[Throwable, Boolean] =
    executeStatement { statement =>
      val query = "DROP table if exists records.company"
      Try(statement.execute(query))
    }.toEither



  def createSchema(name: String): Either[Throwable, Boolean] =
    executeStatement { statement =>
      val query = s"CREATE SCHEMA IF NOT EXISTS $name"
      Try(statement.execute(query))
    }.toEither



  def createTable(): Either[Throwable, Boolean] =
    executeStatement { statement =>
      val query =
        """
          |CREATE TABLE IF NOT EXISTS records.company (
          |  uuid             VARCHAR (255) PRIMARY KEY,
          |  name 			      VARCHAR (255) NULL,
          |  revenue   	      bigint default 0,
          |  employee_amount  bigint default 0)
          |""".stripMargin

      Try(statement.execute(query))
    }.toEither



  def readTopCompany(): Either[Throwable, Option[CompanyWithAverage]] =
    executeStatement { statement =>
      val query =
        """
          | SELECT *,
          | rank() over(order by (revenue / employee_amount) desc) as average_employee
          | FROM records.company
          | LIMIT 1;
          |""".stripMargin

      for {
        rs      <- Try(statement.executeQuery(query))
        company <- Try(parseResultSet(rs))
      } yield company.headOption
    }.toEither



  def persist(companies: Seq[Company]): Either[Throwable, Int] =
    executeStatement { statement =>
      val insert = "INSERT INTO records.company (uuid, name, revenue, employee_amount) VALUES "
      val values = companies.map {
        case Company(uuid, name, revenue, amount) =>
          s"('$uuid', '${name.map(_.value).getOrElse("")}', '${revenue.value}', '${amount.value}')"
      }.mkString(",")

      val query = insert + values
      Try(statement.executeUpdate(query))
    }.toEither



  def readAll(limit: Int = Int.MaxValue): Either[Throwable, Set[CompanyWithAverage]] =
    executeStatement { statement =>
      val query =
        s"""
           | SELECT uuid, name, revenue, employee_amount, (revenue / employee_amount) as average_employee
           | FROM records.company
           | LIMIT $limit
           |""".stripMargin
      for {
        resultSet <- Try(statement.executeQuery(query))
        companies <- Try(parseResultSet(resultSet))
      } yield companies
    }.toEither



  /* throw SQLException */
  @tailrec
  final def parseResultSet(rs: ResultSet, acc: Set[CompanyWithAverage] = Set.empty[CompanyWithAverage]): Set[CompanyWithAverage] =
    rs.next() match {
      case true =>
        parseResultSet(rs, acc + CompanyWithAverage(
          uuid    = UUID.fromString(rs.getString("uuid")),
          name    = Option(rs.getString("name")).map(CompanyName),
          revenue = Revenue(rs.getLong("revenue")),
          amount  = Employees(rs.getLong("employee_amount")),
          average = Average(rs.getLong("average_employee"))))
      case false => acc
    }
}

object CompanyRepository extends CompanyRepository
