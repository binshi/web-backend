package models

import com.amazonaws.regions.Regions
import com.kifi.franz.{QueueName, SimpleSQSClient}
import play.api.Play._
import scala.concurrent.{Future, Await}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object SQSQueue {
  private val accessKey = current.configuration.getString("aws.sqs.ACCESSKEY").get
  private val secretKey = current.configuration.getString("aws.sqs.SECRETKEY").get
  private val sqs = SimpleSQSClient(accessKey, secretKey, Regions.US_EAST_1)
  private val queueName = current.configuration.getString("aws.sqs.QUEUE").get
  private val queue = sqs.simple(QueueName(queueName), true)

  def sqsSample {
    //Send a simple message
   val future1 = Future.traverse(1 to 5) {a => queue.send(s"sample ${a}").map(messageID => println(s"sample ${a} -> ${messageID}"))}

    //Set delay in seconds
    val future2 = queue.send("sample with a delay", 2).map(messageID => println(s"sample with a delay of 2 seconds -> ${messageID}"))
    //Message attributes allow you to provide structured metadata items (such as timestamps, geospatial data, signatures, and identifiers) about the message.
    val future3 = queue.send("sample with message attribute", Some(Map("sampleType" -> "play-example"))).map(messageID => println(s"sample with message attribute -> ${messageID}"))

    Await.ready(future1, Duration.Inf)
    Await.ready(future2, Duration.Inf)
    Await.ready(future3, Duration.Inf)

    //get message
    queue.next(concurrent.ExecutionContext.Implicits.global).map(message => {
      val sqsMessage = message.get
      println(sqsMessage.body)
      sqsMessage.consume()
    }
    )

    //get message and make it invisible for 10 seconds
    queue.nextWithLock(10 seconds)(concurrent.ExecutionContext.Implicits.global).map(message => {
      val sqsMessage = message.get
      sqsMessage.consume { body =>
        println(body)
      }
    })
    //max messages in a batch is 10
    queue.nextBatch(2).map(messages => messages.foreach(message => {
      println(message.body)
      message.consume()
    }))

    //Obtain messages and set visibility timeout
    queue.nextBatchWithLock(3, 10 seconds).map(messages => messages.foreach(message => {
      println(s"attributes:${message.attributes.getOrElse("sampleType", "")} body:${message.body}")
      message.consume()
  }))
  }
}
