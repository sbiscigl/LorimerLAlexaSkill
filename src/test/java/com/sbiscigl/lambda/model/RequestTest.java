package com.sbiscigl.lambda.model;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class RequestTest {

  @Test
  public void shouldConstruct() {
    String id = RandomStringUtils.randomAlphanumeric(8);
    String message = RandomStringUtils.randomAlphanumeric(8);
    Request request = new Request(id, message);
    assertEquals(id, request.getId());
    assertEquals(message, request.getMessage());
  }

  @Test
  public void shouldConstructNullEmptyObject() {
    Request request = new Request();
    assertNull(request.getMessage());
    assertNull(request.getId());
  }
}
