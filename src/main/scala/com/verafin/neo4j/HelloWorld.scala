/*
 *     Licensed to the Apache Software Foundation (ASF) under one
 *     or more contributor license agreements.  See the NOTICE file
 *     distributed with this work for additional information
 *     regarding copyright ownership.  The ASF licenses this file
 *     to you under the Apache License, Version 2.0 (the
 *     "License"); you may not use this file except in compliance
 *     with the License.  You may obtain a copy of the License at
 *       http://www.apache.org/licenses/LICENSE-2.0
 *     Unless required by applicable law or agreed to in writing,
 *     software distributed under the License is distributed on an
 *     "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *     KIND, either express or implied.  See the License for the
 *     specific language governing permissions and limitations
 *     under the License.
 */
package com.verafin.neo4j

import Framework._
import org.neo4j.graphdb.Direction._

object HelloWorld extends App with GraphDatabase {
  autoShutdown {
    val (firstNode, secondNode, relationship) = transaction {
      val firstNode = graphDb.createNode()
      firstNode.setProperty("message", "Hello, ")
      val secondNode = graphDb.createNode()
      secondNode.setProperty("message", "World!")
      val relationship = firstNode.createRelationshipTo(secondNode, KNOWS)
      relationship.setProperty("message", "brave Neo4j ")
      (firstNode, secondNode, relationship)
    }

    (Seq(firstNode, relationship, secondNode)
      map (_.getProperty("message"))
      foreach (print))

    transaction {
      firstNode.getSingleRelationship(KNOWS, OUTGOING).delete()
      firstNode.delete()
      secondNode.delete()
    }
  }

  case object KNOWS extends RelationshipTypeWrapper
}
