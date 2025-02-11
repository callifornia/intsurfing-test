package util

import scala.util.Try


trait ExtendedSyntax {
  implicit class TryOps[A](value: Try[A]) {

    def log(str: String): Try[A] = {
      println(str)
      value
    }

    def andThen[B](f: => B): Try[A] = {
      f
      value
    }
  }
}
object ExtendedSyntax extends ExtendedSyntax
