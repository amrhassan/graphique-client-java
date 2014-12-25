package io.github.amrhassan.graphique;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;

import java.io.IOError;
import java.io.IOException;

public class ServerError extends GraphiqueError {
  private ServerError(String message) {
    super(message);
  }

  public static ServerError forResponse(HttpResponse httpResponse) {
    return new ServerError("The Graphique server is having some trouble.\n" + readBody(httpResponse));
  }

  private static String readBody(HttpResponse response) throws IOError {
    try {
      return StringUtils.join(IOUtils.readLines(response.getEntity().getContent()), "\n");
    } catch (IOException e) {
      throw new IOError(e);
    }
  }
}
