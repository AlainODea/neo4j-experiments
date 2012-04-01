package com.verafin.neo4j

import org.neo4j._
import graphdb.{Direction, RelationshipType, GraphDatabaseService}
import kernel.{AbstractGraphDatabase, EmbeddedGraphDatabase}

object Hello extends App {
  implicit val graphDb = new EmbeddedGraphDatabase("target/neo4j-hello-db")
  autoshutdown {
    val (firstNode, secondNode, relationship) = transaction {
      val firstNode = graphDb.createNode()
      firstNode.setProperty("message", "Hello, ")
      val secondNode = graphDb.createNode()
      secondNode.setProperty("message", "World!")
      val relationship = firstNode.createRelationshipTo(secondNode, KNOWS)
      relationship.setProperty("message", "brave Neo4j ")
      (firstNode, secondNode, relationship)
    }

    transaction {
      firstNode.getSingleRelationship(KNOWS, Direction.OUTGOING).delete()
      firstNode.delete()
      secondNode.delete()
    }

    (Seq(firstNode, relationship, secondNode)
      map (_.getProperty("message"))
      foreach (print))
  }

  def autoshutdown[T](block: => T)(implicit graphDb: AbstractGraphDatabase): T = {
    registerShutdownHook(graphDb)
    try
    {
      block
    } finally {
      graphDb.shutdown()
    }
    def registerShutdownHook(database: GraphDatabaseService) {
      Runtime.getRuntime.addShutdownHook(new Thread() {
        override def run() {
          graphDb.shutdown()
        }
      });
    }
  }

  def transaction[T](block: => T)(implicit graphDb: AbstractGraphDatabase): T = {
    val tx = graphDb.beginTx()
    try {
      val t = block
      tx.success()
      t
    } finally {
      tx.finish()
    }
  }

  trait RelationshipTypeWrapper extends RelationshipType {
    def name = getClass.getSimpleName
  }

  case object KNOWS extends RelationshipTypeWrapper
}
