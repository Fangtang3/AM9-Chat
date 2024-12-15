package am9.chat.server

import am9.chat.server.Main.User
import com.netiq.websocket.drafts.Draft_17
import com.netiq.websocket.{WebSocket, WebSocketServer}
import upickle.default._

import scala.collection.JavaConverters.asScalaSetConverter

class ChatWebSocketServer(port: Int) extends WebSocketServer(port, new Draft_17()) {

  override def onClientOpen(conn: WebSocket): Unit = {}

  override def onClientClose(conn: WebSocket): Unit = {}

  override def onClientMessage(conn: WebSocket, message: String): Unit = {
    read[AnyServerToClient](message).action match {
      case "register" =>
        val knock = read[Knock](message)
        if (knock.protocol != 0) {
          conn.send(write(ServerKick(action = "kick", time = System.currentTimeMillis(), message = "您的客户端不支持服务端版本0.1.0！")))
          conn.close()
          return
        }
        Main.users.find(i => i.name == knock.name) match {
          case Some(_) =>
            conn.send(write(ServerKick(action = "kick", time = System.currentTimeMillis(), message = "用户已存在")))
            conn.close()
          case None =>
            Main.users += User(knock.name, knock.code)
            Main.updateUsers()
            if (!Main.connectedNames.contains(knock.name)) Main.connectedNames += knock.name
            sendToEveryone(write(ServerWelcomeMessage("welcome", System.currentTimeMillis(), knock.name)))
        }
      case "login" =>
        val knock = read[Knock](message)
        if (knock.protocol != 0) {
          conn.send(write(ServerKick(action = "kick", time = System.currentTimeMillis(), message = "您的客户端不支持服务端版本0.1.0！")))
          conn.close()
          return
        }
        Main.users.find(i => i.name == knock.name && i.code.hashCode == knock.code) match {
          case Some(_) =>
            if (!Main.connectedNames.contains(knock.name)) Main.connectedNames += knock.name
            sendToEveryone(write(ServerWelcomeMessage("welcome", System.currentTimeMillis(), knock.name)))
          case None =>
            conn.send(write(ServerKick(action = "kick", time = System.currentTimeMillis(), message = "用户不存在")))
            conn.close()
        }
      case "send" =>
        val msg = read[TextMessage](message)
        val repackaged = ServerTextMessage(
          action = "message",
          sender = msg.sender,
          time = System.currentTimeMillis(),
          message = msg.message)
        if (Main.connectedNames.contains(msg.sender)) {
          sendToEveryone(write(repackaged))
        } else {
          conn.send(write(ServerKick(action = "kick", time = System.currentTimeMillis(), message = "不在用户列表")))
          conn.close()
        }
      case "leave" =>
        val msg = read[Leave](message)
        val repackaged = ServerLeaveMessage(
          action = "leave",
          user = msg.sender,
          time = System.currentTimeMillis)
        if (Main.connectedNames.contains(msg.sender)) {
          sendToEveryone(write(repackaged))
          Main.connectedNames -= msg.sender
        }
      case _ => Main.logger.info("Unknown action: " + read[AnyServerToClient](message))
    }
  }

  override def onError(ex: Throwable): Unit = {}

  private def sendToEveryone(message: String): Unit = {
    for (i <- this.connections.asScala) {
      i.send(message)
    }
  }
  case class AnyServerToClient(action: String)
  case class Knock(action: String, address: String, name: String, code: Int, protocol: Byte)
  case class Leave(action: String, sender: String)
  case class TextMessage(action: String, sender: String, message: String)
  case class ServerTextMessage(action: String, sender: String, time: Long, message: String)
  case class ServerWelcomeMessage(action: String, time: Long, user: String)
  case class ServerLeaveMessage(action: String, time: Long, user: String)
  case class ServerKick(action: String, time: Long, message: String)
}
