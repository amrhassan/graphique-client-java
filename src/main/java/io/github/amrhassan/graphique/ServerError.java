package io.github.amrhassan.graphique;

public class ServerError extends GraphiqueError {
  public ServerError() {
    super("The Graphique server is having some trouble. " +
        "Please check the server's log files and report any issues you come across.");
  }
}
