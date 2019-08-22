useGpg := true

ThisBuild / organization := "fr.qux"
ThisBuild / organizationName := "qux"
ThisBuild / organizationHomepage := Some(url("http://qux.fr/"))

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/dimitriho/sbt-release-tags-only"),
    "scm:git@github.com:dimitriho/sbt-release-tags-only.git"
  )
)
ThisBuild / developers := List(
  Developer(
    id = "dimitriho",
    name = "Dimitri Ho",
    email = "dimitri.ho@gmail.com",
    url = url("http://qux.fr")
  )
)

ThisBuild / description := "A small extension to sbt-release, to support version tracking purely through Git tags, without the need for commits to a version.sbt file"
ThisBuild / licenses := List("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt"))
ThisBuild / homepage := Some(url("https://github.com/dimitriho/sbt-release-tags-only"))

ThisBuild / publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
ThisBuild / publishMavenStyle := true
