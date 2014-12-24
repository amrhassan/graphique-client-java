package io.github.amrhassan.graphique;

public class InvalidImageError extends GraphiqueError {
  public InvalidImageError() {
    super("The submitted image is invalid");
  }
}
