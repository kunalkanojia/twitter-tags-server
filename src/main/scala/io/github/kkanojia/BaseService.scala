package io.github.kkanojia

import akka.event.LoggingAdapter
import akka.http.scaladsl.server.Route
import io.github.kkanojia.serializers.JsonSupport

import scala.concurrent.ExecutionContext

trait BaseService extends BaseComponent with JsonSupport{
  protected def routes: Route
}


trait BaseComponent extends Config {
  protected implicit def log: LoggingAdapter

  protected implicit def executor: ExecutionContext
}



