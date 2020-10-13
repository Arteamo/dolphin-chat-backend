package com.dolphin.components

import akka.actor.ActorSystem
import com.dolphin.components.auth.AuthService
import com.dolphin.components.chat.ChatService
import com.dolphin.db.dao.{TokenDao, TokenDaoImpl, UserDao, UserDaoImpl}
import com.dolphin.utils.EnvUtils
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.ExecutionContext

trait Components {
  def components: ComponentsHolder
}

case class ComponentsHolder(userDao: UserDao, tokenDao: TokenDao, authService: AuthService, chatService: ChatService)

object ComponentsHolder {

  def create(system: ActorSystem)(implicit ec: ExecutionContext): ComponentsHolder = {
    val db = if (EnvUtils.isTesting) {
      Database.forConfig("db")
    } else Database.forURL(sys.env("DATABASE_URL"))

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
