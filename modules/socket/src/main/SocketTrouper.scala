package lila.socket

import scala.concurrent.duration._

import lila.hub.actorApi.HasUserIdP
import lila.hub.Trouper

abstract class SocketTrouper[M <: SocketMember](
    uidTtl: Duration
) extends SocketBase[M] with Trouper {

  override def stop() = {
    super.stop()
    members foreachKey ejectUidString
  }

  protected val receiveTrouper: PartialFunction[Any, Unit] = {
    case HasUserIdP(userId, promise) => promise success hasUserId(userId)
  }

  val process = receiveSpecific orElse receiveTrouper orElse receiveGeneric
}
