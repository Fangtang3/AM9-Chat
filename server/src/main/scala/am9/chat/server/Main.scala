package am9.chat.server

import upickle.default._

import java.io.{BufferedReader, File, InputStreamReader}
import java.nio.file.{Files, Paths}
import java.util.function.Consumer
import java.util.logging.Logger
import scala.collection.mutable

object Main {
  val logger: Logger = Logger.getLogger("ChatServer")
  val connectedNames: mutable.ListBuffer[String] = mutable.ListBuffer()
  private var config: Config = Config(8080)
  val users: mutable.ListBuffer[User] = mutable.ListBuffer()
  var server: Option[ChatWebSocketServer] = None
  def main(args: Array[String]): Unit = {
    logger.info("正在启动AM9-Chat 0.1.0")
    if (new File("config.json").exists()) {
      config = read[Config](new String(Files.readAllBytes(Paths.get("config.json"))))
    } else {
      val inputStream = ClassLoader.getSystemClassLoader.getResourceAsStream("config.json")
      val inputStreamReader = new InputStreamReader(inputStream)
      val bufferedReader = new BufferedReader(inputStreamReader)
      val sb = new StringBuilder()
      bufferedReader.lines().forEach(new Consumer[String](){
        override def accept(t: String): Unit = sb.append(t)
      })
      bufferedReader.close()
      inputStreamReader.close()
      inputStream.close()
      Files.createFile(Paths.get("config.json"))
      Files.writeString(Paths.get("config.json"), sb)
    }
    if (new File("users.json").exists()) {
      read[Users](new String(Files.readAllBytes(Paths.get("users.json")))).users.foreach(user => users += user)
    } else {
      Files.createFile(Paths.get("users.json"))
      Files.writeString(Paths.get("users.json"), "{\"users\":[]}")
    }
    server = Some(new ChatWebSocketServer(config.port))
    server.get.start()
    logger.info(s"服务器监听${config.port}端口")
  }

  def updateUsers(): Unit = {
    Files.writeString(Paths.get("users.json"), write(Users(users)))
  }
  final case class Config(port: Int)
  final case class User(name: String, code: Int)
  final case class Users(users: mutable.ListBuffer[User])
}

