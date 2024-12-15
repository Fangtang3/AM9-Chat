package am9.chat.client
package gui

import am9.chat.client.backend.WebsocketKnock
import javafx.application.Platform
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.Scene
import javafx.scene.control.{PasswordField, TextField}
import javafx.scene.input.MouseEvent

class ConnectController {
  @FXML
  private var address: TextField = null
  @FXML
  private var port: TextField = null
  @FXML
  private var name: TextField = null
  @FXML
  private var password: PasswordField = null

  @FXML
  def onRegisterClicked(event: MouseEvent): Unit = {
    WebsocketKnock.action = "register"
    WebsocketKnock.address = s"ws://${address.getText}:${port.getText}"
    WebsocketKnock.name = name.getText
    WebsocketKnock.code = password.getText.hashCode
    WebsocketKnock.init()
  }

  @FXML
  def onLoginClicked(event: MouseEvent): Unit = {
    WebsocketKnock.action = "login"
    WebsocketKnock.address = s"ws://${address.getText}:${port.getText}"
    WebsocketKnock.name = name.getText
    WebsocketKnock.code = password.getText.hashCode.hashCode
    WebsocketKnock.init()
  }
}
object ConnectController {
  def showConnectMenu(): Unit = {
    Platform.runLater(new Runnable() {
      override def run(): Unit = {
        ChatApplication.stage.get.setScene(null)
        ChatApplication.stage.get.setScene(new Scene(FXMLLoader.load(ClassLoader.getSystemClassLoader.getResource("connect.fxml"))))
      }
    })
  }
}
