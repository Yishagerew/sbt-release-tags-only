addSbtPlugin("com.github.gseitz" % "sbt-release"  % "1.0.11")
addSbtPlugin("org.scalameta"     % "sbt-scalafmt" % "2.0.1")
addSbtPlugin("org.foundweekends" % "sbt-bintray"  % "0.5.2")

// This project is its own plugin :)
unmanagedSourceDirectories in Compile += baseDirectory.value.getParentFile / "src" / "main" / "scala"
