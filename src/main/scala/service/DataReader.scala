package service

import domain.{Companies, Company}
import service.Logger.log

import scala.concurrent.Future
import scala.io.Source
import scala.util.{Failure, Success, Try}


trait DataReader {
  def readData(fileName: String, batch: Int = Int.MaxValue): Companies =
    Source.fromResource(fileName).getLines().drop(1).flatMap(_.split(",").toList match {
      case companyName :: revenue :: employeeAmount :: Nil =>
        Try(Company(companyName, revenue.toLong, employeeAmount.toInt)) match {
          case Success(value) => value :: Nil
          case Failure(th) =>
            log(th, s"Failed to parse data in [$fileName]. Corrupted data: [$companyName, $revenue, $employeeAmount}")
            Nil
        }
    }).foldLeft(Companies.empty)(_ ++ _)

  def writeData[A](data: A): Future[Unit] = ???
}

object DataReader extends DataReader
