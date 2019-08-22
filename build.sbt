name := "sbt-release-tags-only"

organization := "fr.qux"
organizationName := "qux"
organizationHomepage := Some(url("http://qux.fr/"))

description := "A small extension to sbt-release, to support version tracking purely through Git tags, without the need for commits to a version.sbt file"
licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))
homepage := Some(url("https://github.com/dimitriho/sbt-release-tags-only"))

scmInfo := Some(
  ScmInfo(
    url("https://github.com/dimitriho/sbt-release-tags-only"),
    "scm:git@github.com:dimitriho/sbt-release-tags-only.git"
  )
)
developers := List(
  Developer(
    id = "dimitriho",
    name = "Dimitri Ho",
    email = "dimitri.ho@gmail.com",
    url = url("http://qux.fr")
  )
)

sbtPlugin := true
crossSbtVersions := Vector("0.13.18", "1.2.8")
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.11")

publishMavenStyle := false
bintrayRepository := "maven-releases"
bintrayOrganization in bintray := None
