package com.sbiscigl.lambda.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.*;
import com.amazon.ask.model.ui.SimpleCard;
import com.amazon.ask.model.ui.SsmlOutputSpeech;
import com.amazon.ask.response.ResponseBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    when(handlerInput.getResponseBuilder()).thenReturn(new ResponseBuilder());
    Optional<Response> response = noContentHandler.handle(handlerInput);
    assertTrue(response.isPresent());
    Response resp = response.get();
    SimpleCard card = (SimpleCard) resp.getCard();
    SsmlOutputSpeech outputSpeech = (SsmlOutputSpeech) resp.getOutputSpeech();
    assertEquals(StringUtils.EMPTY, card.getTitle());
    assertEquals(StringUtils.EMPTY, card.getContent());
    assertEquals("<speak></speak>", outputSpeech.getSsml());
  }
}
