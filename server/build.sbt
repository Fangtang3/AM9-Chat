name := "server"

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.10.7"

libraryDependencies ++= Seq(
  "com.lihaoyi" %% "upickle" % "0.4.4",
  "com.netiq" % "websocket" % "0.7",
  "org.scala-lang.modules" %% "scala-java8-compat" % "0.5.0",
  "org.junit.jupiter" % "junit-jupiter-api" % "5.11.3" % Test
)