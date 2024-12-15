package am9.chat

import client.ChatApplication

object AutoSelect {
  def main(args: Array[String]): Unit = {
    if (args.length >= 1) {
      args(0) match {
        case "client" =>
          try {
            ChatApplication.start()
          } catch {
            case _: NoClassDefFoundError =>
              println("JavaFX not found. To run the client, please install JavaFX.")
              System.exit(0)
          }
        case "server" => server.Main.main(args)
      }
    } else {
      println("""
                |Usage:
                |  java -jar <AM9-Chat jar> [client|server]
                |""".stripMargin)
      System.exit(0)
    }
  }
}
