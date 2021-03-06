[![Build Status](https://travis-ci.org/amrhassan/graphique-client-java.svg)](https://travis-ci.org/amrhassan/graphique-client-java) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.amrhassan/graphique-client/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/io.github.amrhassan/graphique-client)

Java Client for [Graphique](https://amrhassan.github.io/graphique)
=========================

### Usage ####

**Creating a Client instance**
```java
// Client instances are thread-safe, and also very lightweight.
// Use however you like.
Client client = new Client("localhost", 8980);
```

**Image Submission**
```java
InputStream inputStream = Files.newInputStream(Paths.get("/home/amr/an_image.jpg"));
Image image = client.submitImage(inputStream);
System.out.println(image.servableUrl());
```

**Image Variant Creation**
```java
Image variant = Image.taggedWith("137a07962e49a58b6161ace95bb1b07d.jpg")
    .sizedWithin(500, 400).formattedToJpeg(0.75);
client.create(image);
System.out.println(variant.servableUrl());
```

**Directly Obtaining a Servable URL for an Image**
```java
// If you are sure that an image variant already exists on the server, you can obtain its
// publicly-servable URL directly without making an explicit request to the server.
Image variant = Image.taggedWith("137a07962e49a58b6161ace95bb1b07d.jpg")
    .sizedWithin(500, 400).formattedToJpeg(0.75);
System.out.println(variant.servableUrl());
```
