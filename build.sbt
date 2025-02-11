ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.16"
val postgresVersion = "42.7.5"

lazy val root = (project in file(".")).settings(name := "intsurfing-test")

libraryDependencies += "org.postgresql" % "postgresql" % postgresVersion
