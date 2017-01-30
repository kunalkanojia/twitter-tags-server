package io.github.kkanojia

import akka.actor.{ActorSystem, Props}
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.ActorMaterializer
import io.github.kkanojia.actors.TweetStreamingService
import io.github.kkanojia.actors.TweetStreamingService.StartStreaming

object Main extends App with Config with Services {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  override protected def log = Logging(system, "service")

  override protected def executor = system.dispatcher

  Http().bindAndHandle(routes, httpConfig.interface, httpConfig.port)

  val tweetStreamingServiceProp = Props(new TweetStreamingService)
  val actor = system.actorOf(tweetStreamingServiceProp, TweetStreamingService.NAME)
  actor ! StartStreaming
}

trait Services extends StatusService with TweetService {

  import Directives._

  private val apiVersion = "v1"
  private val allRoutes = Map(
    "status" -> super[StatusService].routes,
    "tweets" -> super[TweetService].routes
  )

  protected override val routes: Route = pathPrefix(apiVersion) {
    allRoutes.map {
      case (k, v) => path(k)(v)
    } reduce (_ ~ _)
  }
}

