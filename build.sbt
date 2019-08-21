import PgpKeys.publishSigned

lazy val `sbt-release-tags-only` = project in file(".")

name := "sbt-release-tags-only"

homepage := Some(url("https://github.com/sbt/sbt-release"))
licenses := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))

crossSbtVersions := Vector("0.13.18", "1.2.8")
sbtPlugin := true
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.11")

// Release
publishStepTask := publishSigned
