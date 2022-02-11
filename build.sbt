ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.1"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.9" % "test"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.9" % "test"

lazy val root = (project in file("."))
  .settings(
    name := "Homework 1"
  )
