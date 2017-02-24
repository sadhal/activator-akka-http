enablePlugins(sbtdocker.DockerPlugin, JavaAppPackaging)

organization := "se.sadhal"

name := "contacts-akka-http"

version := "1.0"

scalaVersion := "2.11.8"

scalacOptions := Seq("-unchecked", "-feature", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaV       = "2.4.8"
  val scalaTestV  = "2.2.6"
  Seq(
    "com.typesafe"       % "config"                               % "1.3.0",
    "com.typesafe.akka" %% "akka-http-core"                       % akkaV,
    "com.typesafe.akka" %% "akka-stream"                          % akkaV,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental"    % akkaV,
    "com.typesafe.akka" %% "akka-http-testkit-experimental"       % "2.4.2-RC3",
    "com.google.inject"  % "guice"                                % "4.1.0",
    "org.scalatest"     %% "scalatest"                            % scalaTestV % "test",
    "org.mongodb.scala" %% "mongo-scala-driver"                   % "1.2.1"
  )
}

dockerfile in docker := {
  val appDir: File = stage.value
  val targetDir = "/app"

  new Dockerfile {
    from("java")
    expose(8778,9000)
    entryPoint(s"$targetDir/bin/${executableScriptName.value}")
    copy(appDir, targetDir)
  }
}

buildOptions in docker := BuildOptions(
  cache = false,
  removeIntermediateContainers = BuildOptions.Remove.Always,
  pullBaseImage = BuildOptions.Pull.Always
)

imageNames in docker := Seq(
  // Sets the latest tag
  //ImageName(s"${organization.value}/${name.value}:latest"),
  ImageName(s"172.30.1.1:5000/contacts-be-dev/${name.value}:latest")

  // Sets a name with a tag that contains the project version
  //ImageName(
  //  namespace = Some(organization.value),
  //  repository = name.value,
   // tag = Some("v" + version.value)
  //)
)