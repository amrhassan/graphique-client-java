import io.github.amrhassan.graphique.Image;
import org.junit.Test;

import static org.junit.Assert.*;

public class ImageTest {

  @Test
  public void testAwsS3ServableUrl() throws Exception {
    Image image = Image.taggedWith("superimage.jpg");
    assertEquals(
        "http://example.org:8080/superimage-cae556a36c4943f2cdbb592ddc536388.jpg",
        image.formattedToJpeg(0.95).sizedWithin(200, 200).servableUrl("example.org", 8080)
    );
    assertEquals(
        "http://example.org:8080/superimage-2afc3be7d8e2ec9079cdd6cbd4543991.png",
        image.formattedToPng().sizedWithin(200, 200).servableUrl("example.org", 8080)
    );
    assertEquals(
        "http://example.org:8080/superimage.jpg",
        image.servableUrl("example.org", 8080)
    );
  }

  @Test
  public void testServableUrl() throws Exception {
    Image image = Image.taggedWith("superimage.jpg");
    assertEquals(
        "https://mybucket.s3.amazonaws.com/myprefix/superimage-cae556a36c4943f2cdbb592ddc536388.jpg",
        image.formattedToJpeg(0.95).sizedWithin(200, 200).awsS3ServableUrl("mybucket", "myprefix/")
    );
    assertEquals(
        "https://mybucket.s3.amazonaws.com/myprefix/superimage-2afc3be7d8e2ec9079cdd6cbd4543991.png",
        image.formattedToPng().sizedWithin(200, 200).awsS3ServableUrl("mybucket", "myprefix/")
    );
    assertEquals(
        "https://mybucket.s3.amazonaws.com/myprefix/superimage.jpg",
        image.awsS3ServableUrl("mybucket", "myprefix/")
    );
  }
}