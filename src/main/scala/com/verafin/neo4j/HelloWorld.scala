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
