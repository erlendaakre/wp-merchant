name := "WpMerchant"
organization := "com.frostvoid"
version := "1.0-SNAPSHOT"

scalaVersion := "2.12.4"

val akkaVersion = "2.5.2"
val akkaHttpVersion = "10.1.0"
val Json4sVersion = "3.2.11"
val scalaTestVersion = "3.0.5"

libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,

    "com.typesafe.akka" %% "akka-stream" % akkaVersion,

    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,

    "org.scalatest" %% "scalatest" % scalaTestVersion % Test
  )

mainClass := Some("com.frostvoid.wpMerchant.impl.HttpServer")