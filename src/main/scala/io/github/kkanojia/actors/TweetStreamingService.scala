package io.github.kkanojia.actors

import akka.actor.{Actor, ActorLogging}
import com.danielasfregola.twitter4s.TwitterStreamingClient
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken, Tweet}
import com.danielasfregola.twitter4s.entities.enums.Language
import io.github.kkanojia.actors.TweetStreamingService.{GetTweets, GetTweetsSuccess, SaveTweet, StartStreaming}

import scala.collection.mutable

object TweetStreamingService {

  val NAME = "TweetStreamingService"

  object StartStreaming
  case class SaveTweet(t: Tweet)
  object GetTweets
  case class GetTweetsSuccess(tweets: Seq[Tweet])

}

class TweetStreamingService extends Actor with ActorLogging {


  var tweets: Seq[Tweet] = mutable.Seq.empty
  val returnTweetLimit = 10

  override def receive: Receive = {

    case StartStreaming => startStreaming()

    case SaveTweet(t) =>
      if (tweets.size > 1000) tweets = tweets.drop(1) :+ t else tweets = tweets :+ t

    case GetTweets =>
      sender() ! GetTweetsSuccess(tweets.takeRight(returnTweetLimit))

  }

  private def startStreaming(): Unit = {
     val streamingClient = TwitterStreamingClient(context.system)

    streamingClient.filterStatuses(
      track = Seq("scala", "akka", "java", "programming", "javascript"),
      languages = Seq(Language.English)){
      case t: Tweet => self ! SaveTweet(t)
    }
  }

}
