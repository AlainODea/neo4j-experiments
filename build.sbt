name := "neo4j"

version := "1.0"

organization := "com.verafin.neo4j"

scalaVersion := "2.9.1-1"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.7.1" withSources() withJavadoc()

libraryDependencies += "org.neo4j" % "neo4j" % "1.7.M02" withSources() withJavadoc()

scalacOptions ++= Seq("-unchecked", "-deprecation", "-g:vars", "-explaintypes", "-optimise", "-encoding", "UTF8")
