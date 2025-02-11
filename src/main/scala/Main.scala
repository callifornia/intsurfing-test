import service.DataReader
import DataReader._
import dao.CompanyRepository._


object Main {
  val fileName = "data.csv"
  def main(args: Array[String]): Unit = {
    val companies = readData(fileName)
    persist(companies)

    println(companies)
  }
}
