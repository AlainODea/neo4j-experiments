import sbt._
import sbt.{Build => _}

object Build extends sbt.Build {
  lazy val root = Project("Neo4j Experiments", file("."))
}
