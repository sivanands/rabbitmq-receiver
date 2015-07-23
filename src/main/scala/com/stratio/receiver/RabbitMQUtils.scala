/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stratio.receiver

import scala.reflect.ClassTag

import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.api.java.{JavaReceiverInputDStream, JavaStreamingContext}
import org.apache.spark.streaming.dstream.ReceiverInputDStream

object RabbitMQUtils {

  /**
   * Create an input stream that receives messages from a RabbitMQ queue.
   * @param ssc                StreamingContext object
   * @param rabbitMQHost       Url of remote RabbitMQ server
   * @param rabbitMQPort       Port of remote RabbitMQ server
   * @param rabbitMQQueueName  Queue to subscribe to
   * @param storageLevel       RDD storage level. Defaults to StorageLevel.MEMORY_AND_DISK_SER_2.
   */
  def createStreamFromAQueue(ssc: StreamingContext,
                   rabbitMQHost: String,
                   rabbitMQPort: Int,
                   rabbitMQQueueName: String,
                   storageLevel: StorageLevel = StorageLevel.MEMORY_AND_DISK_SER_2
                    ): ReceiverInputDStream[String] = {
    new RabbitMQInputDStream(
      ssc,
      Some(rabbitMQQueueName),
      rabbitMQHost,
      rabbitMQPort,
      None,
      None,
      false,
      Seq(),
      storageLevel)
  }

  /**
   * Create an input stream that receives messages from a RabbitMQ queue.
   * @param jssc               JavaStreamingContext object
   * @param rabbitMQHost       Url of remote RabbitMQ server
   * @param rabbitMQPort       Port of remote RabbitMQ server
   * @param rabbitMQQueueName  Queue to subscribe to
   * @param storageLevel       RDD storage level. Defaults to StorageLevel.MEMORY_AND_DISK_SER_2.
   */
  def createJavaStreamFromAQueue(jssc: JavaStreamingContext,
                   rabbitMQHost: String,
                   rabbitMQPort: Int,
                   rabbitMQQueueName: String,
                   storageLevel: StorageLevel = StorageLevel.MEMORY_AND_DISK_SER_2
                    ): JavaReceiverInputDStream[String] = {
    implicitly[ClassTag[AnyRef]].asInstanceOf[ClassTag[String]]
    createStreamFromAQueue(jssc.ssc, rabbitMQHost, rabbitMQPort, rabbitMQQueueName)
  }

  /**
   * Create an input stream that receives messages from a RabbitMQ queue.
   * @param ssc              StreamingContext object
   * @param rabbitMQHost     Url of remote RabbitMQ server
   * @param rabbitMQPort     Port of remote RabbitMQ server
   * @param exchangeName     Exchange name to subscribe to
   * @param routingKeys      Routing keys to subscribe to
   * @param storageLevel     RDD storage level. Defaults to StorageLevel.MEMORY_AND_DISK_SER_2.
   */
  def createStreamFromRoutingKeys(ssc: StreamingContext,
                   rabbitMQHost: String,
                   rabbitMQPort: Int,
                   exchangeName: String,
                   exchangeType: String = "direct",
                   durable: Boolean = false,
                   routingKeys: Seq[String],
                   storageLevel: StorageLevel = StorageLevel.MEMORY_AND_DISK_SER_2
                    ): ReceiverInputDStream[String] = {
    new RabbitMQInputDStream(
      ssc,
      None,
      rabbitMQHost,
      rabbitMQPort,
      Some(exchangeName),
      Some(exchangeType),
      durable,
      routingKeys,
      storageLevel)
  }

  /**
   * Create an input stream that receives messages from a RabbitMQ queue.
   * @param jssc             JavaStreamingContext object
   * @param rabbitMQHost     Url of remote RabbitMQ server
   * @param rabbitMQPort     Port of remote RabbitMQ server
   * @param exchangeName     Exchange name to subscribe to
   * @param routingKeys      Routing keys to subscribe to
   * @param storageLevel     RDD storage level. Defaults to StorageLevel.MEMORY_AND_DISK_SER_2.
   */
  def createJavaStreamFromRoutingKeys(jssc: JavaStreamingContext,
                                  rabbitMQHost: String,
                                  rabbitMQPort: Int,
                                  exchangeName: String,
                                  exchangeType: String,
                                  durable: Boolean,
                                  routingKeys: java.util.List[String],
                                  storageLevel: StorageLevel = StorageLevel.MEMORY_AND_DISK_SER_2
                                   ): JavaReceiverInputDStream[String] = {
    implicitly[ClassTag[AnyRef]].asInstanceOf[ClassTag[String]]
    createStreamFromRoutingKeys(jssc.ssc, rabbitMQHost, rabbitMQPort, exchangeName, exchangeType, durable, scala.collection.JavaConversions
      .asScalaBuffer(routingKeys), storageLevel)
  }
}
