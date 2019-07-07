package com.sbiscigl.lambda.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import com.google.inject.Inject;
import com.sbiscigl.lambda.exception.TrainTimeFetchException;
import com.sbiscigl.lambda.mta.StationTimes;
import com.sbiscigl.lambda.mta.StationTimesFetcher;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.requestType;

public class GetTrainsHandler implements RequestHandler {

  private final StationTimesFetcher stationTimesFetcher;

  @Inject
  public GetTrainsHandler(final StationTimesFetcher stationTimesFetcher) {
    this.stationTimesFetcher = stationTimesFetcher;
  }

  @Override
  public boolean canHandle(HandlerInput handlerInput) {
    return handlerInput.matches(intentName("NextTrain")
        .or(intentName("AMAZON.FallbackIntent"))
        .or(requestType(LaunchRequest.class)));
  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput) {
    try {
      final String speechText = getSpeectTextFromTrainTimes(stationTimesFetcher.getFeedData());
      return handlerInput
          .getResponseBuilder()
          .withSpeech(speechText)
          .withSimpleCard("Next Train", speechText)
          .build();
    } catch (IOException ioe) {
      throw new TrainTimeFetchException("Could Not retrieve train times");
    }
  }

  private String getSpeectTextFromTrainTimes(final StationTimes stationTimes) {
    final String manhattanStatement =
        directionStatement(stationTimes.getManhattanBoundTrains(), "manhattan");
    final String rockawayStatement =
        directionStatement(stationTimes.getRockawayBoundTrains(), "rockaway");
    return manhattanStatement + rockawayStatement;
  }

  private String directionStatement(final List<Long> trainTimes, final String direction) {
    final StringBuilder statement = new StringBuilder();
    if (trainTimes.isEmpty()) {
      statement.append(String.format("There is no %s bound trains. ", direction));
    } else if (trainTimes.size() == 1) {
      statement
          .append(String.format("The next %s bound train is in ", direction))
          .append(String.format("%d minutes. ", trainTimes.get(0)));
    } else {
      statement
          .append(String.format("The next %s bound train is in ", direction))
          .append(String.format("%d minutes. ", trainTimes.get(0)))
          .append(String.format("Followed by one in %d minutes. ", trainTimes.get(1)));
    }
    return statement.toString().replace("in 0 minutes.", "arriving now.");
  }
}
