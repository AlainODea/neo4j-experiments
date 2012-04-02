package com.verafin.neo4j

import org.neo4j.kernel.EmbeddedGraphDatabase

trait GraphDatabase {
  implicit val graphDb =
    new EmbeddedGraphDatabase("target/" + super.getClass.getSimpleName)
}
