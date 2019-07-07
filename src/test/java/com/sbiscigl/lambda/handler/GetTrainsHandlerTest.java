package com.sbiscigl.lambda.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ui.SimpleCard;
import com.amazon.ask.model.ui.SsmlOutputSpeech;
import com.amazon.ask.response.ResponseBuilder;
import com.google.common.collect.ImmutableList;
import com.sbiscigl.lambda.exception.TrainTimeFetchException;
import com.sbiscigl.lambda.mta.StationTimes;
import com.sbiscigl.lambda.mta.StationTimesFetcher;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetTrainsHandlerTest extends HandlerTestBase {

  @Mock
  private StationTimesFetcher stationTimesFetcher;

  private GetTrainsHandler getTrainsHandler;

  @Before
  public void before() {
    stationTimesFetcher = mock(StationTimesFetcher.class);
    getTrainsHandler = new GetTrainsHandler(stationTimesFetcher);
  }

  @Test
  public void shouldHandleCorrectly() {
    HandlerInput nextTrainInput = createHandlerInputWithIntent("NextTrain");
    HandlerInput badInput = createHandlerInputWithIntent(RandomStringUtils.randomAlphanumeric(8));
    HandlerInput fallbackInput = createHandlerInputWithIntent("AMAZON.FallbackIntent");
    HandlerInput launchInput = createHandlerInputWithLaunchRequest();
    assertTrue(getTrainsHandler.canHandle(nextTrainInput));
    assertTrue(getTrainsHandler.canHandle(fallbackInput));
    assertTrue(getTrainsHandler.canHandle(launchInput));
    assertFalse(getTrainsHandler.canHandle(badInput));
  }

  @Test
  public void shouldHandleNoTrainsCorrectly() throws IOException {
    HandlerInput handlerInput = mock(HandlerInput.class);
    when(handlerInput.getResponseBuilder()).thenReturn(new ResponseBuilder());
    when(stationTimesFetcher.getFeedData())
        .thenReturn(StationTimes.of(ImmutableList.of(), ImmutableList.of()));
    Optional<Response> response = getTrainsHandler.handle(handlerInput);
    assertTrue(response.isPresent());
    Response resp = response.get();
    SimpleCard card = (SimpleCard) resp.getCard();
    SsmlOutputSpeech outputSpeech = (SsmlOutputSpeech) resp.getOutputSpeech();
    assertEquals("Next Train", card.getTitle());
    assertEquals("There is no manhattan bound trains. There is no rockaway bound trains. ",
        card.getContent());
    assertEquals(outputSpeech.getSsml(),
        "<speak>There is no manhattan bound trains. There is no rockaway bound trains.</speak>");
  }

  @Test
  public void shouldHandleOneTrainCorrectly() throws IOException {
    HandlerInput handlerInput = mock(HandlerInput.class);
    when(handlerInput.getResponseBuilder()).thenReturn(new ResponseBuilder());
    when(stationTimesFetcher.getFeedData())
        .thenReturn(StationTimes.of(ImmutableList.of(1L), ImmutableList.of(2L)));
    Optional<Response> response = getTrainsHandler.handle(handlerInput);
    assertTrue(response.isPresent());
    Response resp = response.get();
    SimpleCard card = (SimpleCard) resp.getCard();
    SsmlOutputSpeech outputSpeech = (SsmlOutputSpeech) resp.getOutputSpeech();
    assertEquals("Next Train", card.getTitle());
    assertEquals("The next manhattan bound train is in 1 minutes. " +
            "The next rockaway bound train is in 2 minutes. ",
        card.getContent());
    assertEquals(outputSpeech.getSsml(),
        "<speak>The next manhattan bound train is in 1 minutes. " +
            "The next rockaway bound train is in 2 minutes.</speak>");
  }

  @Test
  public void shouldHandleManyTrainsCorrectly() throws IOException {
    HandlerInput handlerInput = mock(HandlerInput.class);
    when(handlerInput.getResponseBuilder()).thenReturn(new ResponseBuilder());
    when(stationTimesFetcher.getFeedData())
        .thenReturn(StationTimes.of(ImmutableList.of(1L, 2L, 3L), ImmutableList.of(4L, 5L, 6L)));
    Optional<Response> response = getTrainsHandler.handle(handlerInput);
    assertTrue(response.isPresent());
    Response resp = response.get();
    SimpleCard card = (SimpleCard) resp.getCard();
    SsmlOutputSpeech outputSpeech = (SsmlOutputSpeech) resp.getOutputSpeech();
    assertEquals("Next Train", card.getTitle());
    assertEquals(
        "The next manhattan bound train is in 1 minutes. " +
            "Followed by one in 2 minutes. The next rockaway bound train " +
            "is in 4 minutes. Followed by one in 5 minutes. ",
        card.getContent());
    assertEquals("<speak>The next manhattan bound train is in 1 minutes. " +
            "Followed by one in 2 minutes. The next rockaway bound train is in 4 minutes. " +
            "Followed by one in 5 minutes.</speak>",
        outputSpeech.getSsml());
  }

  @Test
  public void shouldHandleManyTrainsCorrectlyWithSomeArriving() throws IOException {
    HandlerInput handlerInput = mock(HandlerInput.class);
    when(handlerInput.getResponseBuilder()).thenReturn(new ResponseBuilder());
    when(stationTimesFetcher.getFeedData())
        .thenReturn(StationTimes.of(ImmutableList.of(0L, 2L, 3L), ImmutableList.of(4L, 5L, 6L)));
    Optional<Response> response = getTrainsHandler.handle(handlerInput);
    assertTrue(response.isPresent());
    Response resp = response.get();
    SimpleCard card = (SimpleCard) resp.getCard();
    SsmlOutputSpeech outputSpeech = (SsmlOutputSpeech) resp.getOutputSpeech();
    assertEquals("Next Train", card.getTitle());
    assertEquals("The next manhattan bound train is arriving now. " +
            "Followed by one in 2 minutes. The next rockaway bound train is in 4 minutes. " +
            "Followed by one in 5 minutes. ",
        card.getContent());
    assertEquals("<speak>The next manhattan bound train is arriving now. " +
            "Followed by one in 2 minutes. The next rockaway bound train is in 4 minutes. " +
            "Followed by one in 5 minutes.</speak>",
        outputSpeech.getSsml());
  }

  @Test(expected = TrainTimeFetchException.class)
  public void shouldThrowTrainTimeExceptionWhenFetcherFails() throws IOException {
    HandlerInput handlerInput = mock(HandlerInput.class);
    when(handlerInput.getResponseBuilder()).thenReturn(new ResponseBuilder());
    when(stationTimesFetcher.getFeedData()).thenThrow(new IOException());
    getTrainsHandler.handle(handlerInput);
  }
}