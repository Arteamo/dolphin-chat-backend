package com.dolphin.components

import akka.actor.ActorSystem
import com.dolphin.components.auth.AuthService
import com.dolphin.components.room.RoomService
import com.dolphin.db.dao._
import com.dolphin.utils.EnvUtils
import com.typesafe.config.Config
import org.postgresql.Driver
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.ExecutionContext

trait Components extends ContextProvider {
  def components: ComponentsHolder
}

case class ComponentsHolder(
  actorSystem: ActorSystem,
  config: Config,
  userDao: UserDao,
  tokenDao: TokenDao,
  roomDao: RoomDao,
  messageDao: MessageDao,
  userToRoomDao: UserToRoomDao,
  authService: AuthService,
  roomService: RoomService
)

object ComponentsHolder {

  def getDb: Database = {
    if (EnvUtils.isTesting) {
      Database.forConfig("db")
    } else {
      val url = sys.env("DATABASE_URL")
      val regex = "postgres://(.*):(.*)@(.*)".r
      url match {
        case regex(usr, pwd, url) =>
          Database.forDriver(url = "jdbc:postgresql://" + url, user = usr, password = pwd, driver = new Driver)
      }
    }
  }

  def create(system: ActorSystem, config: Config)(implicit ec: ExecutionContext): ComponentsHolder = {
    val db = getDb
    val userDao = new UserDaoImpl(db)
    val tokenDao = new TokenDaoImpl(db)
    val roomDao = new RoomDaoImpl(db)
    val messageDao = new MessageDaoImpl(db, config.getInt("app.pagination.pageSize"))
    val userToRoomDao = new UserToRoomDaoImpl(db)
    ComponentsHolder(
      system,
      config,
      userDao,
      tokenDao,
      roomDao,
      messageDao,
      userToRoomDao,
      new AuthService(userDao, tokenDao),
      new RoomService(userToRoomDao, messageDao)
    )
  }

}
