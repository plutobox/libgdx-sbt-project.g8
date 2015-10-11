import com.google.common.base.Charsets
import com.google.common.io.Files
import scala.collection.convert.wrapAll._
import sbtrobovm.RobovmPlugin.ManagedNatives

val libgdxVersion = "$libgdx_version$"

lazy val sharedSettings: Seq[Def.Setting[_]] = Seq(
  name := "$name;format="norm"$",
  version := "0.1",
  scalaVersion := "$scala_version$",
  assetsDirectory := {
    val r = file("assets")
    IO.createDirectory(r)
    r
  },
  libraryDependencies ++= Seq(
    "com.badlogicgames.gdx" % "gdx" % libgdxVersion
  ),
  javacOptions ++= Seq(
    "-Xlint",
    "-encoding", "UTF-8",
    "-source", "1.6",
    "-target", "1.6"
  ),
  scalacOptions ++= Seq(
    "-Xlint",
    "-Ywarn-dead-code",
    "-Ywarn-value-discard",
    "-Ywarn-numeric-widen",
    "-Ywarn-unused",
    "-Ywarn-unused-import",
    "-unchecked",
    "-deprecation",
    "-feature",
    "-encoding", "UTF-8",
    "-target:jvm-1.6"
  ),
  cancelable := true,
  exportJars := true
)

lazy val core = project in file("core") settings (sharedSettings: _*)

lazy val desktop = project in file("desktop") settings (sharedSettings: _*) dependsOn core settings(
    name := (name in core).value + "-desktop",
    libraryDependencies ++= Seq(
      "net.sf.proguard" % "proguard-base" % "5.1" % "provided",
      "com.badlogicgames.gdx" % "gdx-backend-lwjgl" % libgdxVersion,
      "com.badlogicgames.gdx" % "gdx-platform" % libgdxVersion classifier "natives-desktop"
    ),
    fork in Compile := true,
    baseDirectory in run := assetsDirectory.value,
    unmanagedResourceDirectories in Compile += assetsDirectory.value
  )

lazy val android = project in file("android") settings (sharedSettings ++ androidBuild: _*) dependsOn core settings(
    name := (name in core).value + "-android",
    ivyConfigurations += ManagedNatives,
    libraryDependencies ++= Seq(
      "com.badlogicgames.gdx" % "gdx-backend-android" % libgdxVersion,
      "com.badlogicgames.gdx" % "gdx-platform" % libgdxVersion % ManagedNatives classifier "natives-armeabi",
      "com.badlogicgames.gdx" % "gdx-platform" % libgdxVersion % ManagedNatives classifier "natives-armeabi-v7a",
      "com.badlogicgames.gdx" % "gdx-platform" % libgdxVersion % ManagedNatives classifier "natives-x86"
    ),
    extractNatives := {
      sbtrobovm.RobovmPlugin.AndroidManagedNatives.value.head //TODO
    },
    /*nativeExtractions <<= (baseDirectory) { base => Seq(
        ("natives-armeabi.jar", new ExactFilter("libgdx.so"), base / "libs" / "armeabi"),
        ("natives-armeabi-v7a.jar", new ExactFilter("libgdx.so"), base / "libs" / "armeabi-v7a"),
        ("natives-x86.jar", new ExactFilter("libgdx.so"), base / "libs" / "x86")
      )},*/
    projectLayout := new ProjectLayout.Wrapped(ProjectLayout.Gradle(baseDirectory.value, target.value)) {
      override def assets = assetsDirectory.value
    },
    platformTarget in Android := "android-$api_level$",
    proguardOptions in Android ++=
      Files.readLines(file("core/proguard-project.txt"), Charsets.UTF_8) ++
        Files.readLines(file("android/proguard-project.txt"), Charsets.UTF_8)
  )

lazy val ios = project in file("ios") settings (sharedSettings ++ iOSRoboVMSettings: _*) dependsOn core settings (
    name := (name in core).value + "-ios",
    libraryDependencies ++= Seq(
      "com.badlogicgames.gdx" % "gdx-backend-robovm" % libgdxVersion notTransitive(),
      "org.robovm" % "robovm-cocoatouch" % RoboVMVersion,
      "org.robovm" % "robovm-objc" % RoboVMVersion,
      "org.robovm" % "robovm-rt" % RoboVMVersion,
      "com.badlogicgames.gdx" % "gdx-platform" % libgdxVersion classifier "natives-ios"
    ),
    robovmProperties := Right(Map[String,String](
      "app.version" -> version.value,
      "app.mainclass" -> (mainClass in Compile).value.getOrElse({System.err.println("Main class is required for iOS project."); ""}),
      "app.executable" -> "$name;format="Camel"$",
      "app.build" -> "0",
      "app.name" -> "$name$",
      "app.assetdir" -> assetsDirectory.value.getCanonicalPath
    )),
    robovmConfiguration := Left(baseDirectory.value / "robovm.xml")
  )

lazy val assetsDirectory = settingKey[File]("Directory with game's assets")

lazy val extractNatives = taskKey[File]("Extracts natives from jars")

lazy val all = project in file(".") aggregate(core, desktop, android, ios)