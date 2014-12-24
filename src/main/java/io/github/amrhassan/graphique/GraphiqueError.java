package io.github.amrhassan.graphique;

public abstract class GraphiqueError extends RuntimeException {
  public GraphiqueError(Throwable e) {
    super(e);
  }

  public GraphiqueError(String message) {
    super(message);
  }
}
