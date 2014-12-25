Java Client for [Graphique](https://amrhassan.github.io/graphique)
=========================

### Get from Maven Central ###
Check out the available releases at [The Central Repository](http://search.maven.org/#search|ga|1|a%3A%22graphique-client%22%20g%3A%22io.github.amrhassan%22).

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
System.out.println(image.servableUrl());
```

**Directly Obtaining a Servable URL for an Image**
```java
// If you are sure that an image variant already exists on the server, you can obtain its
// publicly-servable URL directly without making an explicit request to the server.
Image variant = Image.taggedWith("137a07962e49a58b6161ace95bb1b07d.jpg")
    .sizedWithin(500, 400).formattedToJpeg(0.75);
System.out.println(image.servableUrl());
```
