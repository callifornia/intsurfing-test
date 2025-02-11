package service
import java.sql.{Connection, DriverManager, SQLException, Statement}
import Logger._


trait DbConnection {
  private val url = "jdbc:postgresql://localhost:5432/test"
  private val username = "postgres"
  private val password = "admin"

  private var connection: Connection = null

  private def getConnection(): Either[Exception, Connection] =
    try {
      connection = DriverManager.getConnection(url, username, password)
      log("Connection established ...")
      Right(connection)
    } catch {
      case ex: SQLException =>
        log(ex, "Failed to establish connection due to error")
        Left(ex)
    } finally {
      log("Close connection ...")
      connection.close()
    }

  def executeStatement[A](handle: Statement => A): Either[Exception, A] =
    try {
      getConnection().map(connection => handle(connection.createStatement()))
    } catch {
      case ex: SQLException =>
        log(ex, "Failed to create statement due to error: ")
        Left(ex)
    }
}

object DbConnection extends DbConnection
