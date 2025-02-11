import service.DataReader
import DataReader._
import dao.CompanyRepository._


object Main {

  def main(args: Array[String]): Unit =
    for {
      _             <- initSql()
      companies     <- readCompaniesFromFile("data.csv")
      _             <- persist(companies)
      topCompany    <- readTopCompany()
      allCompanies  <- readAll()
    } yield {
      println("Companies with fewer than 50 employees: \n\t" + allCompanies.filter(_.amount.value < 50).mkString("\n"))
      println("Top company: \n\t" + topCompany.getOrElse("None"))
    }
}
