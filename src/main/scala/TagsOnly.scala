package sbtrelease.tagsonly

import sbt.Keys._
import sbt._
import sbtrelease.ReleasePlugin.autoImport.ReleaseKeys.versions
import sbtrelease.ReleasePlugin.autoImport._
import sbtrelease.ReleaseStateTransformations._
import sbtrelease.tagsonly.TagsOnly.{pushTagsOnly, setVersionFromTags}
import sbtrelease.{ExtraReleaseCommands, ReleasePlugin, Vcs, Version}

object TagsOnlyPlugin extends AutoPlugin {

  override def requires: Plugins = ReleasePlugin

  override def trigger: PluginTrigger = allRequirements

  object autoImport {
    val releaseTagPrefix = settingKey[String]("Prefix to use for tags")

    val publishStepTask = settingKey[TaskKey[Unit]]("Task for the publish step (default: publish)")

    lazy val TagsOnly = sbtrelease.tagsonly.TagsOnly
  }

  import autoImport._

  override lazy val projectSettings = Seq[Setting[_]](
    publishStepTask := publish,
    // Defaults for this plugin
    releaseTagPrefix := name.value,
    // Provide new defaults for some settings of the main `sbtrelease` plugin
    releaseUseGlobalVersion := false,
    releaseVersionBump := Version.Bump.Minor,
    releaseTagName := s"${releaseTagPrefix.value}-${version.value}",
    releaseTagComment := s"Releasing version ${version.value} of module: ${name.value}",
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      releaseStepCommand(ExtraReleaseCommands.initialVcsChecksCommand),
      setVersionFromTags(releaseTagPrefix.value),
      runClean,
      tagRelease,
      releaseStepTask(publishStepTask.value),
      pushTagsOnly
    )
  )
}

object TagsOnly {
  def setVersionFromTags(tagPrefix: String): ReleaseStep = { st: State =>
    val bumpVersion = Function.chain(
      Seq(
        Project.extract(st).runTask(releaseNextVersion, st)._2,
        Project.extract(st).runTask(releaseVersion, st)._2
      )
    )

    val git            = getGit(st)
    val gitDescribeCmd = git.cmd("describe", "--match", s"$tagPrefix-*")
    val gitDescription = gitDescribeCmd.! match {
      case 0 =>
        st.log.info("Found existing tag matching the module name")
        git.cmd("describe", "--match", s"$tagPrefix-*").!!.trim
      case 128 =>
        st.log.info("No existing tags matching the module name were found")
        s"$tagPrefix-0.0.0-auto-generated-initial-tag"
      case _ =>
        throw new RuntimeException(s"Unexpected failure running $gitDescribeCmd")
    }
    st.log.info("Most recent tag matching the module was '%s'" format gitDescription)

    val versionRegex = s"$tagPrefix-([0-9]+.[0-9]+.[0-9]+)-?(.*)?".r
    val versionToRelease = gitDescription match {
      case versionRegex(v, "") => v // No changes since last release
      case versionRegex(v, _)  => s"${bumpVersion(v)}" // new version, bumped according to our chosen strategy
      case _                   => sys.error(s"Tag '$gitDescription' failed to match expected format '$versionRegex'")
    }
    st.log.info("Setting release version to '%s'." format versionToRelease)

    st.put(versions, (versionToRelease, "unused 'next version' field"))
    reapply(
      Seq(version := versionToRelease),
      st
    )
  }

  // The standard `pushChanges` release step relies on an upstream being configured.  This doesn't.
  lazy val pushTagsOnly: ReleaseStep = { st: State =>
    val gitPushTags = getGit(st).cmd("push", "origin", "--tags").!!.trim
    st.log.info("'git push origin --tags' returned '%s'" format gitPushTags)
    st
  }

  def getGit(st: State): Vcs =
    Project
      .extract(st)
      .get(releaseVcs)
      .getOrElse(sys.error("Aborting release. Working directory is not a Git repository."))
}
