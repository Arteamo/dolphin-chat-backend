package com.dolphin.utils

import com.dolphin.enums.Environment
import com.dolphin.enums.Environment.{Environment, Testing}

object EnvUtils {

  def env: Environment = {
    Environment.values.find(_.toString == sys.env.getOrElse("ENV_TYPE", "Testing")).getOrElse(Testing)
  }

  def isTesting: Boolean = env == Testing
}
