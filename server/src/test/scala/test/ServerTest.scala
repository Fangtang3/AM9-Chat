package am9.chat.server
package test

import org.junit.jupiter.api.function.Executable
import org.junit.jupiter.api.{Assertions, Test}

class ServerTest {
  @Test
  def jvmFunctionVoid(): Unit = {
    Assertions.assertTrue(true)
  }

  @Test
  def jvmFunctionMaths(): Unit = {
    Assertions.assertEquals(
      new java.math.BigDecimal(31400000000L).add(new java.math.BigDecimal(62800000000L)),
      new java.math.BigDecimal(94200000000L))
  }

  @Test
  def jvmFunctionString(): Unit = {
    val sb = new java.lang.StringBuilder()
    sb.append("Hello")
    sb.append(" World")
    Assertions.assertEquals("Hello World", sb.toString)
  }

  @Test
  def jvmFunctionList(): Unit = {
    val list = java.util.List.of("I", "am", "a", "teapot")
    Assertions.assertEquals(list.get(1) == "am", list.get(3) == "teapot")
  }

  @Test
  def jvmFunctionSet(): Unit = {
    val set = java.util.Set.of("I", "am", "a", "teapot")
    Assertions.assertEquals(set.size(), 4)
  }

  @Test
  def jvmFunctionArray(): Unit = {
    val array = Array("I", "am", "a", "teapot")
    Assertions.assertEquals(array(1) == "am", array(3) == "teapot")
  }

  @Test
  def jvmFunctionMap(): Unit = {
    val map = java.util.Map.of("I", "am", "a", "teapot", "Are", "you", "?", "teapots")
    Assertions.assertEquals(map.get("a") + "s", map.get("?"))
  }

  @Test
  def jvmFunctionEnum(): Unit = {
    Assertions.assertEquals(java.lang.Thread.State.BLOCKED, java.lang.Thread.State.BLOCKED)
  }

  @Test
  def jvmFunctionClass(): Unit = {
    Assertions.assertEquals(java.lang.Integer.TYPE, java.lang.Integer.TYPE)
  }

  @Test
  def jvmFunctionAnnotation(): Unit = {
    Assertions.assertEquals(classOf[java.lang.Deprecated], classOf[java.lang.Deprecated])
  }

  @Test
  def jvmFunctionInterface(): Unit = {
    Assertions.assertEquals(classOf[java.lang.Runnable], classOf[java.lang.Runnable])
  }

  @Test
  def jvmFunctionObject(): Unit = {
    Assertions.assertEquals(classOf[AnyRef], classOf[AnyRef])
  }

  @Test
  def jvmFunctionNull(): Unit = {
    Assertions.assertNull(null)
  }

  @Test
  def jvmFunctionException(): Unit = {
    Assertions.assertThrows(classOf[ArithmeticException], new Executable {
      override def execute(): Unit = {
        var i = 1 / 0
        i += 3
      }
    })
  }

  @Test
  def jvmFunctionFall(): Unit = {
    Assertions.assertEquals(1 + 1, 3, "always falls")
  }
}
