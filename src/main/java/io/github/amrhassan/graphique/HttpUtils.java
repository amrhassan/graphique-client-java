package io.github.amrhassan.graphique;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;

import java.io.IOError;
import java.io.IOException;

class HttpUtils {

  static String textualBodyOf(HttpEntity httpEntity) throws IOError {
    try {
      return StringUtils.join(IOUtils.readLines(httpEntity.getContent()), "\n");
    } catch (IOException e) {
      throw new IOError(e);
    }
  }

  static String toString(HttpEntityEnclosingRequest httpRequest) {
    StringBuilder text = new StringBuilder(httpRequest.toString());
    for (Header header : httpRequest.getAllHeaders())
      text.append("\n" + header);

    if (httpRequest.getEntity().getContentLength() > 0) {
      if (httpRequest.getEntity().getContentType() != null &&
          httpRequest.getEntity().getContentType().getValue().startsWith("text")) {

        text.append("\n\n" + HttpUtils.textualBodyOf(httpRequest.getEntity()));
      } else {
        text.append("\n\n[Non-textual Body]");
      }
    }

    return text.toString();
  }

  static String toString(HttpResponse httpResponse) {
    StringBuilder text = new StringBuilder();

    text.append(httpResponse.getStatusLine());

    for (Header header : httpResponse.getAllHeaders())
      text.append("\n" + header);

    if (httpResponse.getEntity().getContentLength() > 0) {
      if (httpResponse.getEntity().getContentType() != null &&
          httpResponse.getEntity().getContentType().getValue().startsWith("text")) {

        text.append("\n\n" + HttpUtils.textualBodyOf(httpResponse.getEntity()));
      } else {
        text.append("\n\n[Non-textual Body]");
      }
    }

    return text.toString();
  }
}
