package com.dolphin.components

import akka.actor.ActorSystem
import com.dolphin.components.auth.AuthService
import com.dolphin.components.chat.ChatService
import com.dolphin.db.dao.{TokenDao, TokenDaoImpl, UserDao, UserDaoImpl}
import com.dolphin.utils.EnvUtils
import org.postgresql.Driver
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.ExecutionContext

trait Components {
  def components: ComponentsHolder
}

case class ComponentsHolder(userDao: UserDao, tokenDao: TokenDao, authService: AuthService, chatService: ChatService)

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

  def create(system: ActorSystem)(implicit ec: ExecutionContext): ComponentsHolder = {
    val db = getDb
    val userDao = new UserDaoImpl(db)
    val tokenDao = new TokenDaoImpl(db)
    ComponentsHolder(
      userDao,
      tokenDao,
      new AuthService(userDao, tokenDao),
      new ChatService()
    )
  }

}
