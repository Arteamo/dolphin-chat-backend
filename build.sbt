name := "dolphin-chat-backend"

version := "0.1"

scalaVersion := "2.13.3"

libraryDependencies ++= {
  val akkaVersion = "2.6.9"
  val akkaHttpVersion = "10.2.0"
  val logbackVersion = "1.2.3"
  val akkaHttpCirceVersion = "1.34.0"
  val slickVersion = "3.3.3"
  val postgresDriverVersion = "42.2.5"
  val circeVersion = "0.13.0"
  val swaggerVersion = "1.2.0"
  Seq(
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.slick" %% "slick" % slickVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "de.heikoseeberger" %% "akka-http-circe" % akkaHttpCirceVersion,
    "ch.qos.logback" % "logback-classic" % logbackVersion,
    "org.postgresql" % "postgresql" % postgresDriverVersion,
    "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
    "com.github.swagger-akka-http" %% "swagger-akka-http" % swaggerVersion
  )
}
