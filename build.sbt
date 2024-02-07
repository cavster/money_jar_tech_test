name := "money_jar_tech_test_colm"
 
version := "1.0" 
      
lazy val `money_jar_tech_test_colm` = (project in file(".")).enablePlugins(PlayScala)

javacOptions ++= Seq("-source", "11", "-target", "11")

resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"
      
scalaVersion := "2.13.5"

libraryDependencies ++= Seq( jdbc ,
  ehcache,
  ws ,
  specs2 % Test ,
  guice ,
  "org.scalatest" %% "scalatest" % "3.2.10" % Test,
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test,
  "com.typesafe.play" %% "play-json" % "2.9.4")
      