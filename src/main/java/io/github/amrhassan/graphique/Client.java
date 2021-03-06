package io.github.amrhassan.graphique;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import java.io.ByteArrayInputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class Client {

  private final String hostname;
  private final int port;

  public Client(String hostname, int port) {
    checkArgument(port > 0);
    checkNotNull(hostname);

    this.hostname = hostname;
    this.port = port;
  }

  /**
   * Submits an image to the server.
   *
   * @param imageContent the image content in raw bytes
   * @return
   * @throws InvalidImageError when the submitted image content is not a valid image
   * @throws ServerError
   * @throws IOError
   */
  public Image submitImage(byte[] imageContent) {

    if (imageContent.length == 0)
      throw new InvalidImageError();

    final String uri = String.format("http://%s:%d/images", hostname, port);

    try (final CloseableHttpClient client = HttpClients.createDefault()) {

      final HttpPost request = new HttpPost(uri);
      request.setEntity(new ByteArrayEntity(imageContent));

      try (CloseableHttpResponse response = client.execute(request)) {

        final int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode == HttpStatus.SC_CREATED)
          return Image.fromLocation(response.getHeaders("Location")[0].getValue());
        else if (statusCode == HttpStatus.SC_BAD_REQUEST)
          throw new InvalidImageError();
        else if (statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR)
          throw ServerError.forResponse(response);
        else
          throw new RuntimeException("Unexpected response. Request was:\n" +
              HttpUtils.toString(request) + "\n\nResponse was: " + HttpUtils.toString(response));
      }

    } catch (IOException e) {
      throw new IOError(e);
    }
  }

  /**
   * Submits an image to the server.
   *
   * @param inputStream image content as a stream of raw bytes
   * @return
   * @throws InvalidImageError when the submitted image content is not a valid image
   * @throws ServerError
   * @throws IOError
   */
  public Image submitImage(InputStream inputStream) {
    try {
      return submitImage(IOUtils.toByteArray(inputStream));
    } catch (IOException e) {
      throw new IOError(e);
    }
  }

  /**
   * Creates the image on the server.
   *
   * @throws ImageNotFoundError when the source image is not available on the server
   * @throws ServerError
   */
  public void create(Image image) {

    URIBuilder uriBuilder =
        (new URIBuilder()).setScheme("http").setPort(port).setHost(hostname).setPath("/image/" + image.tag());

    if (image.sizeWithin().isPresent())
      uriBuilder = uriBuilder.addParameter("size-within", String.format("%dx%d", image.sizeWithin().get().toArray()));
    if (image.format().isPresent())
      uriBuilder = uriBuilder.addParameter("format", image.format().get());

    final String uri = uriBuilder.toString();

    try (CloseableHttpClient client = HttpClients.createDefault()) {

      final HttpPatch request = new HttpPatch(uri);

      try (CloseableHttpResponse response = client.execute(request)) {

        final int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode == HttpStatus.SC_OK)
          return;
        else if (statusCode == HttpStatus.SC_NOT_FOUND)
          throw new ImageNotFoundError();
        else if (statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR)
          throw ServerError.forResponse(response);
        else
          throw new RuntimeException("Unexpected response. Request was:\n" +
              HttpUtils.toString(request) + "\n\nResponse was: " + HttpUtils.toString(response));
      }
    } catch (IOException e) {
      throw new IOError(e);
    }
  }
}
