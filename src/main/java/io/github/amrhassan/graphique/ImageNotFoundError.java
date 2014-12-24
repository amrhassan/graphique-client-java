package io.github.amrhassan.graphique;

public class ImageNotFoundError extends GraphiqueError {
  public ImageNotFoundError() {
    super("The requested image is not available");
  }
}
