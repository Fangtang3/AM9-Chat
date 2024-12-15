
name := "client"

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.10.7"

libraryDependencies ++= Seq(
  "com.lihaoyi" %% "upickle" % "0.4.4",
  "org.openjfx" % "javafx-fxml" % "24-ea+19" % Provided,
  "com.netiq" % "websocket" % "0.7",
  "org.scala-lang.modules" %% "scala-java8-compat" % "0.5.0"
)