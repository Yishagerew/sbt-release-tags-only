[![Maven Central](https://maven-badges.herokuapp.com/maven-central/fr.qux/sbt-release-tags-only/badge.svg?kill_cache=1)](https://search.maven.org/artifact/fr.qux/sbt-release-tags-only/)

# sbt-release-tags-only
A small extension to sbt-release, to support version tracking purely through Git tags, without the need for commits
to a `version.sbt` file.  Natively supports independently versioned multi-module projects.

### Using it
1.  Add the plugin:
    ```sbt
    addSbtPlugin("fr.qux" % "sbt-release-tags-only" % "0.5.0")
    ```
2.  Define your release process, including the relevant tasks:
    ```sbt
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      releaseStepCommand(ExtraReleaseCommands.initialVcsChecksCommand),
      setVersionFromTags(releaseTagPrefix.value),
      runClean,
      tagRelease,
      publishArtifacts
      pushTagsOnly
    )
    ```

#### Notes
-  `setVersionFromTags()` replaces `inquireVersions`, `setReleaseVersion`
-  `commitReleaseVersion`, `commitNextVersion`, and `setNextVersion` should no longer be used
-  `pushTagsOnly` replaces `pushChanges`

Look at [the code](src/main/scala/TagsOnly.scala) to go deeper!
