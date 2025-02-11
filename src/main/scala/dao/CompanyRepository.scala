package dao

import domain.{Companies, Company}
import service.DbConnection._
import service.Logger.log


//● Calculate the average revenue per employee for each company
//● Filter out companies with fewer than 50 employees
//● Identify and return the company with the highest revenue per employee


trait CompanyRepository {

  def persist(companies: Companies): Either[Exception, Unit] =
    executeStatement { statement =>
      val insert = "INSERT INTO records.company (uuid, name, revenue, employee_amount) VALUES "
      val values =  companies.values.map {
        case Company(uuid, name, revenue, amount) =>
          s"('$uuid', '${name.map(_.value).getOrElse("")}', '${revenue.value}', '${amount.value}')"
      }.mkString(",")

      val query = insert + values
      log("Persisting data into the db: " + query)
      statement.execute(query)
    }


  def readData(limit: Int = Int.MaxValue): Either[Exception, Companies] =
    executeStatement { statement =>
      val query = "SELECT uuid, name, revenue, employee_amount from records.company"
      val rs = statement.executeQuery(query)
      Companies.empty
    }
//  val statement = connection.createStatement()
//  val resultSet = statement.executeQuery("SELECT host, user FROM user")
//  while ( resultSet.next() ) {
//    val host = resultSet.getString("host")
//    val user = resultSet.getString("user")
//    println("host, user = " + host + ", " + user)
//  }
}

object CompanyRepository extends CompanyRepository
