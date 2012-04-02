package com.verafin.neo4j

import org.neo4j.graphdb.Node
import Framework._

object IndexedUserDatabase extends App with GraphDatabase {

  case object USERS_REFERENCE extends RelationshipTypeWrapper

  case object USER extends RelationshipTypeWrapper

  def idToUserName(id: Int) = "user" + id.toString + "@neo4j.org"

  val USERNAME_KEY: String = "username"

  autoShutdown {
    val nodeIndex = graphDb.index().forNodes("nodes")
    def createAndIndexUser(username: String): Node = {
      val node = graphDb.createNode()
      node.setProperty(USERNAME_KEY, username)
      nodeIndex.add(node, USERNAME_KEY, username)
      node
    }
    val userIds = 0 until 100000
    transaction {
      if (!nodeIndex.get(USERNAME_KEY, idToUserName(1)).iterator().hasNext) {
        val usersReferenceNode = graphDb.createNode()
        graphDb.getReferenceNode.createRelationshipTo(
          usersReferenceNode, USERS_REFERENCE)
        for (id <- userIds) {
          val userNode = createAndIndexUser(idToUserName(id))
          usersReferenceNode.createRelationshipTo(
            userNode, USER)
        }
      }
    }

    val idToFind = 45
    val start = System.currentTimeMillis()
    for (id <- userIds) {
      nodeIndex.get(
        USERNAME_KEY, idToUserName(idToFind)).getSingle
    }
    val end = System.currentTimeMillis()
    println("Looked up every user sequentially (" + (end - start) + "ms)")
  }
}
