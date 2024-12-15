package am9.chat.client

import am9.chat.client.backend.WebsocketKnock
import am9.chat.client.gui.{ConnectController, DialogueBox}
import javafx.stage.Stage

class ChatApplication extends javafx.application.Application {

  override def start(s: Stage): Unit = {
    try {
      ChatApplication.stage = Some(s)
      ChatApplication.stage.get.setTitle("客户端")
      ChatApplication.stage.get.setResizable(false)
      ConnectController.showConnectMenu()
      //MainController.showMainMenu()
      ChatApplication.stage.get.show()
    } catch {
      case e: Exception =>
        e.printStackTrace()
        DialogueBox.showError("运行时遇到错误", "运行时遇到错误",
          s"""详细信息：
          |Exception Class: ${e.getClass.getName}
          |Exception Info: ${e.getMessage}""".stripMargin, exit = true)
    }
  }

  override def stop(): Unit = {
    try {
      WebsocketKnock.close()
      super.stop()
    } catch {
      case _: Exception =>
        super.stop()
    }
  }
}
object ChatApplication {
  var stage: Option[Stage] = None
  def start(): Unit = {
    javafx.application.Application.launch(classOf[ChatApplication])
  }
}
