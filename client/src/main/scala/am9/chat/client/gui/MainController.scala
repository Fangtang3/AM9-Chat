package am9.chat.client.gui

import am9.chat.client.ChatApplication
import am9.chat.client.backend.WebsocketKnock
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.Scene
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import javafx.scene.text.TextFlow

class MainController {

  @FXML
  private var text: TextFlow = null

  @FXML
  private var messageField: TextField = null

  @FXML
  def initialize(): Unit = {
    WebsocketKnock.textFlow = Some(text)
  }

  @FXML
  def onSendPressed(event: MouseEvent): Unit = {
    if (!WebsocketKnock.getPing) {
      DialogueBox.showError("连接已丢失", null, "连接已丢失。", exit = false)
      ConnectController.showConnectMenu()
    }
    if (messageField.getText != null && messageField.getText.nonEmpty) {
      if (messageField.getText.length < 255) {
        WebsocketKnock.sendMessage(messageField.getText)
        messageField.setText("")
      } else {
        DialogueBox.showInfo("消息过长", null, "消息过长。", exit = false)
      }
    } else {
      DialogueBox.showInfo("消息为空", null, "消息为空。", exit = false)
    }
    WebsocketKnock.textFlow = Some(text)
  }
}
object MainController {
  def showMainMenu(): Unit = {
    ChatApplication.stage.get.setScene(null)
    ChatApplication.stage.get.setScene(new Scene(FXMLLoader.load(ClassLoader.getSystemClassLoader.getResource("main.fxml"))))
  }
}
