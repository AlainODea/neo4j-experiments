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
