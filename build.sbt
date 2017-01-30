enablePlugins(JavaAppPackaging)


name := """twitter-tags-server"""
organization := "io.github.kunalkanojia"
version := "0.0.1"
scalaVersion := "2.12.1"
scalacOptions := Seq("-unchecked", "-feature", "-deprecation", "-encoding", "utf8")

resolvers += Resolver.jcenterRepo

libraryDependencies ++= {
  val catsV = "0.9.0"
  val akkaHttpV = "10.0.1"
  val ficusV = "1.4.0"
  val scalaMockV = "3.4.2"
  val catsScalatestV = "2.2.0"

  Seq(
    "com.danielasfregola" %% "twitter4s" % "4.2",
    "org.typelevel" %% "cats-core" % catsV,
    "com.iheart" %% "ficus" % ficusV,

    "com.typesafe.akka" %% "akka-http" % akkaHttpV,

    "com.typesafe.akka" % "akka-slf4j_2.12" % "2.4.16",
    "ch.qos.logback" % "logback-classic" % "1.1.9",

    "org.scalamock" %% "scalamock-scalatest-support" % scalaMockV % "it,test",
    "com.ironcorelabs" %% "cats-scalatest" % catsScalatestV % "it,test",
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV % "it,test"
  )
}

lazy val root = project.in(file(".")).configs(IntegrationTest)
Defaults.itSettings
Revolver.settings
enablePlugins(JavaAppPackaging)

initialCommands :=
  """|import scalaz._
     |import Scalaz._
     |import akka.actor._
     |import akka.pattern._
     |import akka.util._
     |import scala.concurrent._
     |import scala.concurrent.duration._""".stripMargin

Revolver.settings
