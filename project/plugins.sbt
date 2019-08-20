addSbtPlugin("com.github.gseitz" % "sbt-release"  % "1.0.11")
addSbtPlugin("com.jsuereth"      % "sbt-pgp"      % "1.1.0")
addSbtPlugin("org.scalameta"     % "sbt-scalafmt" % "2.0.1")

// This project is its own plugin :)
unmanagedSourceDirectories in Compile += baseDirectory.value.getParentFile / "src" / "main" / "scala"
