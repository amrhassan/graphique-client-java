package io.github.amrhassan.graphique;

import org.apache.http.HttpResponse;

public class ServerError extends GraphiqueError {
  private ServerError(String message) {
    super(message);
  }

  public static ServerError forResponse(HttpResponse httpResponse) {
    return new ServerError("The Graphique server is having some trouble.\n" +
        HttpUtils.textualBodyOf(httpResponse.getEntity()));
  }
}
