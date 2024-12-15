package am9.chat.client.backend

import am9.chat.client.ChatApplication
import am9.chat.client.gui.{ConnectController, DialogueBox, MainController}
import com.netiq.websocket.WebSocketClient
import com.netiq.websocket.drafts.Draft_17
import javafx.application.Platform
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.paint.Color
import javafx.scene.text.{Text, TextFlow}
import upickle.default._

import java.io.IOException
import java.net.URI
import java.time.{Instant, ZoneId}
import java.time.format.DateTimeFormatter

object WebsocketKnock {
  var action: String = ""
  var address: String = ""
  var name: String = ""
  var code: Int = 0
  var textFlow: Option[TextFlow] = None
  var getPing: Boolean = false
  private var ioError: Boolean = false
  private var webSocket: Option[WebSocketClient] = None
  private var registered: Boolean = false
  private val dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.systemDefault)

  def init(): Unit = {
    if (address == "ws://:") {
      DialogueBox.showError("地址为空", null, "请输入服务器地址。", exit = false)
    } else if (address.endsWith(":")) {
      DialogueBox.showError("端口为空", null, "请输入端口。", exit = false)
    } else if (name == "") {
      DialogueBox.showError("用户名为空", null, "请输入用户名。", exit = false)
    } else if (code == 0) {
      DialogueBox.showError("密码为空", null, "请输入密码。", exit = false)
    } else {
      val knockData = Knock(action, address, name, code, 0)
      webSocket = Some(new ChatWebSocket(address))
      webSocket.get.connect()
      ChatApplication.stage.get.setScene(new Scene(FXMLLoader.load(ClassLoader.getSystemClassLoader.getResource("wait.fxml"))))
      Thread.sleep(1000)
      webSocket.get.send(write(knockData))
      MainController.showMainMenu()
    }
  }

  def close(): Unit = {
    webSocket match {
      case None => //do nothing
      case Some(ws) =>
        ws.send(write(Leave(action = "leave", sender = name)))
        ws.close()
    }
  }

  def sendMessage(message: String): Unit = {
    webSocket match {
      case None => //do nothing
      case Some(ws) => ws.send(write(TextMessage(action = "send", sender = name, message = message)))
    }
  }

  private def addJsonText(json: String): Unit = {
    getPing = true
    if (!registered) {
      registered = true
      textFlow match {
        case None =>
        case Some(tf) =>
          Platform.runLater(new Runnable() {
            override def run(): Unit = {
              tf.accessibleTextProperty().addListener(new ChangeListener[String]() {

                override def changed(observableValue: ObservableValue[_ <: String], t: String, t1: String): Unit = {
                  if (t1.length > 400) {
                    tf.getChildren.clear()
                  }
                }
              })
            }
          })
      }
    }
    read[AnyServerToClient](json).action match {
      case "message" =>
        val obj = read[ServerTextMessage](json)
        textFlow match {
          case None => println("textFlow is null")
          case Some(tf) =>
            Platform.runLater(new Runnable() {
              override def run(): Unit = {
                val text = new Text(s"${dateTimeFormatter.format(Instant.ofEpochMilli(obj.time))} ${obj.sender}: \n${obj.message}\n")
                text.setFill(Color.BLACK)
                tf.getChildren.add(text)
              }
            })
        }
      case "welcome" =>
        val obj = read[ServerWelcomeMessage](json)
        textFlow match {
          case None => println("textFlow is null")
          case Some(tf) =>
            println("adding text to textFlow")
            Platform.runLater(new Runnable() {
              override def run(): Unit = {
                val text = new Text(s"${dateTimeFormatter.format(Instant.ofEpochMilli(obj.time))} ${obj.user} 加入了聊天室。\n")
                text.setFill(Color.SKYBLUE)
                tf.getChildren.add(text)
              }
            })
        }
      case "leave" =>
        val obj = read[ServerLeaveMessage](json)
        textFlow match {
          case None => println("textFlow is null")
          case Some(tf) =>
            println("adding text to textFlow")
            Platform.runLater(new Runnable() {
              override def run(): Unit = {
                val text = new Text(s"${dateTimeFormatter.format(Instant.ofEpochMilli(obj.time))} ${obj.user} 离开了聊天室。\n")
                text.setFill(Color.SKYBLUE)
                tf.getChildren.add(text)
              }
            })
        }
      case "kick" =>
        val obj = read[ServerKick](json)
        DialogueBox.showInfo("连接已关闭", "连接已关闭", obj.message, exit = true)
      case _ =>
    }
  }

  case class AnyServerToClient(action: String)
  case class ServerTextMessage(action: String, sender: String, time: Long, message: String)
  case class ServerWelcomeMessage(action: String, time: Long, user: String)
  case class ServerLeaveMessage(action: String, time: Long, user: String)
  case class ServerKick(action: String, time: Long, message: String)

  private final class ChatWebSocket(uri: String) extends WebSocketClient(new URI(uri), new Draft_17()) {

    override def onOpen(): Unit = {}

    override def onMessage(message: String): Unit = {
      addJsonText(message)
    }

    override def onClose(): Unit = {
      ConnectController.showConnectMenu()
    }

    override def onIOError(e: IOException): Unit = {
      if (!ioError) {
        ioError = true
        e.printStackTrace()
        close()
        Platform.runLater(new Runnable() {
          override def run(): Unit = {
            DialogueBox.showError("连接已丢失", "连接已丢失",
              s"""详细信息：
                 |Exception Class: ${e.getClass.getName}
                 |Exception Info: ${e.getMessage}""".stripMargin, exit = false)
            ConnectController.showConnectMenu()
          }
        })
        ioError = false
      }
    }
  }
  case class Knock(action: String, address: String, name: String, code: Int, protocol: Byte)
  case class TextMessage(action: String, sender: String, message: String)
  case class Leave(action: String, sender: String)
}

