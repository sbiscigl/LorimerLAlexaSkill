package com.sbiscigl.lambda.model;

public class Request {
  private String id;
  private String message;

  public Request() {}

  public Request(final String id, final String message) {
    this.id = id;
    this.message = message;
  }

  public String getId() {
    return id;
  }

  public String getMessage() {
    return message;
  }
}
