package com.sbiscigl.lambda.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class NoContentHandlerTest extends HandlerTestBase {

  private NoContentHandler noContentHandler;

  @Before
  public void before() {
    noContentHandler = new NoContentHandler();
  }

  @Test
  public void shouldHandleCorrectContents() {
    HandlerInput fallbackInput = createHandlerInputWithIntent("AMAZON.FallbackIntent");
    HandlerInput cancelInput = createHandlerInputWithIntent("AMAZON.CancelIntent");
    HandlerInput helpInput = createHandlerInputWithIntent("AMAZON.HelpIntent");
    HandlerInput stopInput = createHandlerInputWithIntent("AMAZON.StopIntent");
    HandlerInput navigateHomeInput = createHandlerInputWithIntent("AMAZON.NavigateHomeIntent");
    HandlerInput sessionEndedInput = createHandlerInputWithSessionEndedRequest();
    HandlerInput launchInput = createHandlerInputWithLaunchRequest();
    HandlerInput badInput = createHandlerInputWithIntent(RandomStringUtils.randomAlphanumeric(8));
    assertFalse(noContentHandler.canHandle(fallbackInput));
    assertTrue(noContentHandler.canHandle(cancelInput));
    assertTrue(noContentHandler.canHandle(helpInput));
    assertTrue(noContentHandler.canHandle(stopInput));
    assertTrue(noContentHandler.canHandle(navigateHomeInput));
    assertTrue(noContentHandler.canHandle(sessionEndedInput));
    assertFalse(noContentHandler.canHandle(launchInput));
    assertFalse(noContentHandler.canHandle(badInput));
  }

  @Test
  public void shouldReturnNothingForAnyRequest() {
    HandlerInput handlerInput = mock(HandlerInput.class);
    assertFalse(noContentHandler.handle(handlerInput).isPresent());
  }
}
