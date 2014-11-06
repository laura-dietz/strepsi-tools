strepsirrhine tool suite
========================

Basic components for the Strepsirrhini project - a modular suite for IR applications in scala.
The strepsirrihi project contains the following sub-projects:

- strepsitools (basic utilities without further dependencies, mostly for scala)
- strepsirank (fork of RankLib with a few bug fixes and scala interface wrappers)
- strepsimur (scala interface wrappers for working with [Galago][lemurproject.org/galago]; requires dependency on galago 3.7-SNAPSHOT)
- strepsipand (query expansion framework)
- strepsent (entity-based loading merging processing; includes loader for [FACC1 annotations][http://lemurproject.org/clueweb12/FACC1/])

All these sub-projects can stand on their own (they only need a dependency on strepsitools). 
They are available individually and together.

Maven dependency through Nexus
===============================
All sub-projects are available through our Nexus server

        <repository>
            <id>edu.umass.ciir.releases</id>
            <name>CIIR Nexus Releases</name>
            <url>http://scm-ciir.cs.umass.edu:8080/nexus/content/repositories/releases/</url>
        </repository>

Dependency: 

  <properties>
    <scala.version>2.10.2</scala.version>
    <javaVersion>1.6</javaVersion>
    <lemur.version>3.7-SNAPSHOT</lemur.version>
    <strepsi.version>1.4</strepsi.version>
  </properties>
  
  <dependency>
      <groupId>edu.umass.ciir</groupId>
      <artifactId>strepsirrhine</artifactId>
      <version>s${scala.version}-g${lemur.version}-${strepsi.version}</version>
  </dependency>

Download
=========
You can download the Jars directly from my web page
  [strepsirrhini][http://ciir.cs.umass.edu/~dietz/strepsirrhine/strepsirrhine-s2.10.2-g3.7-SNAPSHOT-1.4-jar-with-dependencies.jar]

  [sources][http://ciir.cs.umass.edu/~dietz/strepsirrhine/sources.zip]


Ethymology
===========
strepsirrhine
    n.
    Any member of the clade Strepsirrhini, one of the two suborders of primates.

More at Wordnik from Wiktionary, Creative Commons Attribution/Share-Alike License
