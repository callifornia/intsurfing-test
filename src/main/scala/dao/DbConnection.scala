package dao

import util.ExtendedSyntax._

import java.sql.{Connection, DriverManager, Statement}
import scala.util.Try


trait DbConnection {

  private val url = "jdbc:postgresql://localhost:5432/test"
  private val username = "postgres"
  private val password = "admin"

  private def connection: Try[Connection] = Try(DriverManager.getConnection(url, username, password))

  def executeStatement[A](handle: Statement => Try[A]): Try[A] =
    connection.flatMap { connection =>
      Try(connection.createStatement())
        .flatMap { statement =>
          handle(statement)
            .andThen(Try(statement.close()))
            .andThen(Try(connection.close()))
        }
        .andThen {
          connection.isClosed match {
            case false => Try(connection.close())
            case true  => ()
          }
        }
    }
}


object DbConnection extends DbConnection
