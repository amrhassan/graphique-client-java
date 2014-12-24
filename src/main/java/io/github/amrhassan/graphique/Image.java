package io.github.amrhassan.graphique;

import com.google.common.base.Optional;
import org.apache.commons.codec.digest.DigestUtils;
import org.javatuples.Pair;

public class Image {

  private final String tag;
  private final Optional<Pair<Integer, Integer>> sizeWithin;
  private final Optional<String> format;

  private Image(String tag, Optional<Pair<Integer, Integer>> sizeWithin, Optional<String> format) {
    this.tag = tag;
    this.sizeWithin = sizeWithin;
    this.format = format;
  }

  public String tag() {
    return tag;
  }

  Optional<Pair<Integer, Integer>> sizeWithin() {
    return sizeWithin;
  }

  Optional<String> format() {
    return format;
  }

  /**
   * Creates an Image with the specified tag.
   */
  public static Image taggedWith(String tag) {
    return new Image(tag, Optional.<Pair<Integer,Integer>>absent(), Optional.<String>absent());
  }

  /**
   * Returns a mutation of Image with a request to fit the image within the specified size.
   *
   * @param width the width in pixels
   * @param height the height in pixels
   */
  public Image sizedWithin(int width, int height) {
    return new Image(tag, Optional.of(Pair.with(width, height)), format);
  }

  /**
   * Returns a mutation of Image with a request to format the image to JPEG.
   */
  public Image formattedToJpeg(double quality) {
    return new Image(tag, sizeWithin, Optional.of(String.format("jpeg(%.2f)", quality)));
  }

  /**
   * Returns a mutation of Image with a request to format the image to PNG.
   */
  public Image formattedToPng() {
    return new Image(tag, sizeWithin, Optional.of("png"));
  }

  /**
   * Returns a mutation of Image with a request to format the image to JPEG.
   */
  public Image formattedToJpeg() {
    return new Image(tag, sizeWithin, Optional.of("jpeg"));
  }

  static Image fromLocation(String location) {
    final String tag = location.substring(location.lastIndexOf('/') + 1);
    return Image.taggedWith(tag);
  }

  /**
   * Returns the publicly-servable URL of the image.
   *
   * This call does not guarantee that the image is actually present on the server.
   *
   * @param bucket the AWS S3 bucket
   * @param prefix the specified S3 Backend prefix configured on the Graphique server
   */
  public String awsS3ServableUrl(String bucket, String prefix) {
    final String key = prefix + qualifiedTag();
    return String.format("https://%s.s3.amazonaws.com/%s", bucket, key);
  }

  /**
   * Returns the publicly-servable URL of the image.
   *
   * @param hostname the hostname configured on the Graphique Local Backend
   * @param port the port configured on the Graphique Local Backend
   */
  public String servableUrl(String hostname, int port) {
    return String.format("http://%s:%d/%s", hostname, port, qualifiedTag());
  }

  /**
   * Returns the publicly-servable URL of the image.
   */
  public String servableUrl() {
    return servableUrl("localhost", 9806);
  }

  private String hashedAttributes() {

    if (isOriginal())
      return "";

    String sizeAttribute = "";
    if (sizeWithin.isPresent())
      sizeAttribute = String.format("size-within=%dx%d", sizeWithin.get().toArray());

    String formatAttribute = format.or("");

    return DigestUtils.md5Hex(formatAttribute + sizeAttribute);
  }

  private boolean isOriginal() {
    return !sizeWithin.isPresent() && !format.isPresent();
  }

  private String qualifiedTag() {
    if (isOriginal())
      return tag;
    else {
      final String extensionlessTag = tag.substring(0, tag.lastIndexOf('.'));
      final String tagExtension = tag.substring(tag.lastIndexOf('.'));
      return extensionlessTag + "-" + hashedAttributes() + tagExtension;
    }
  }
}
