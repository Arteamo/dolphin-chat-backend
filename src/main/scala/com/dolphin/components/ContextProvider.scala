package com.dolphin.components

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.global

trait ContextProvider {
  implicit lazy val ec: ExecutionContext = global
}
