package am9.chat.client.gui

import am9.chat.client.ChatApplication
import am9.chat.client.backend.WebsocketKnock
import javafx.application.Platform
import javafx.scene.control.Alert

object DialogueBox {
  def showError(title: String, headerText: String, message: String, exit: Boolean): Unit = {
    Platform.runLater(new Runnable() {
      override def run(): Unit = {
        val alert = new Alert(Alert.AlertType.ERROR)
        alert.setTitle(title)
        alert.setHeaderText(headerText)
        alert.setContentText(message)
        alert.showAndWait()
        if (exit) {
          ChatApplication.stage.get.close()
          ChatApplication.stage = None
          WebsocketKnock.close()
          System.exit(0)
        }
      }
    })
  }

  def showInfo(title: String, headerText: String, message: String, exit: Boolean): Unit = {
    Platform.runLater(new Runnable() {
      override def run(): Unit = {
        val alert = new Alert(Alert.AlertType.INFORMATION)
        alert.setTitle(title)
        alert.setHeaderText(headerText)
        alert.setContentText(message)
        alert.showAndWait()
        if (exit) {
          ChatApplication.stage.get.close()
          ChatApplication.stage = None
          WebsocketKnock.close()
          System.exit(0)
        }
      }
    })
  }
}
