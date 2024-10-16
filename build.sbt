val scafi_core = "it.unibo.scafi" %% "scafi-core" % "1.1.5"
val scafi_simulator = "it.unibo.scafi" %% "scafi-simulator" % "1.1.5"
val scafi_simulator_gui = "it.unibo.scafi" %% "scafi-simulator-gui" % "1.1.5"
val scafi_platform = "it.unibo.scafi" %% "scafi-distributed" % "1.1.5"

ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "test-scafi",
    libraryDependencies ++= Seq(scafi_core)
  )
