package service

trait Logger {

  def log[A](ex: Throwable, msg: A): Unit = {
    println(msg + "\n" + ex.printStackTrace())
  }
  def log[A](msg: A): Unit = println(msg)
}

object Logger extends Logger
