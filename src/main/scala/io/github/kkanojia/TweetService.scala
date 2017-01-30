package io.github.kkanojia

import akka.actor.ActorSystem
import akka.http.scaladsl.server.{Directives, Route}
import akka.pattern.ask
import akka.util.Timeout
import com.danielasfregola.twitter4s.entities.Tweet
import io.github.kkanojia.actors.TweetStreamingService
import io.github.kkanojia.actors.TweetStreamingService.{GetTweets, GetTweetsSuccess}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

trait TweetService extends BaseService {

  import Directives._

  implicit val system: ActorSystem

  implicit val timeout = Timeout(15.seconds)

  lazy val tweetStreamingActor = Await.result(system.actorSelection("user/" + TweetStreamingService.NAME).resolveOne(), timeout.duration)


  def getTweets(): Future[Seq[Tweet]] = {
    tweetStreamingActor ? GetTweets map {
      case GetTweetsSuccess(tweets) => tweets
      case _ => Seq.empty
    }
  }

  override protected def routes: Route =
    get {
      log.info("/tweets executed")
      val tweets = getTweets()
      complete(tweets)
    }

}

