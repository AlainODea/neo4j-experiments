package com.verafin.neo4j

import org.neo4j.kernel.AbstractGraphDatabase
import org.neo4j.graphdb.RelationshipType

object Framework {
  def autoShutdown[T](block: => T)(implicit graphDb: AbstractGraphDatabase): T = {
    try {
      Runtime.getRuntime.addShutdownHook(new Thread() {
        override def run() {
          graphDb.shutdown()
        }
      });
      block
    } finally {
      graphDb.shutdown()
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
}
