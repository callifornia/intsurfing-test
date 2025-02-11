package service

import domain.Company
import scala.io.Source
import scala.util.{Failure, Success, Try}


trait DataReader {

  def readCompaniesFromFile(fileName: String): Either[Throwable, Seq[Company]] =
    Try(
      Source.fromResource(fileName).getLines().drop(1).flatMap(_.split(",").toList match {
        case companyName :: revenue :: employeeAmount :: Nil =>
          Try(Company(companyName, revenue.toLong, employeeAmount.toInt)) match {
            case Success(value) => value :: Nil
            case Failure(th) =>
              println(th, s"Failed to parse data in [$fileName]. Corrupted data: [$companyName, $revenue, $employeeAmount}")
              Nil
          }
      }).foldLeft(Seq.empty[Company])(_ :+ _)
    ).toEither
}

object DataReader extends DataReader
