ThisBuild / version := "0.1.0"

ThisBuild / scalaVersion := "2.10.7"

lazy val root = (project in file("."))
  .aggregate(client, server)
  .dependsOn(client, server)
  .settings(
    name := "AM9Chat",
    idePackagePrefix := Some("am9.chat")
  )

lazy val client = (project in file("./client/"))
  .settings(
    name := "AM9Chat-Client"
  )

lazy val server = (project in file("./server/"))
  .settings(
    name := "AM9Chat-Server"
  )

libraryDependencies ++= Seq(
  "com.lihaoyi" %% "upickle" % "0.4.4",
  "com.netiq" % "websocket" % "0.7",
  "org.scala-lang.modules" %% "scala-java8-compat" % "0.5.0",
  "org.junit.jupiter" % "junit-jupiter-api" % "5.11.3" % Test,
  "org.openjfx" % "javafx-fxml" % "24-ea+19" % Provided
)
scalacOptions ++= Seq("-target:jvm-1.8")
root / mainClass := Some("am9.chat.AutoSelect")
client / mainClass := Some("am9.chat.client.ChatApplication")
server / mainClass := Some("am9.chat.server.Main")