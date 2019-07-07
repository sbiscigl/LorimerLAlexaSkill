package com.sbiscigl.lambda.command;

import com.google.inject.Inject;
import com.sbiscigl.lambda.model.Response;
import com.sbiscigl.lambda.mta.StationTimesFetcher;

import java.io.IOException;

public class TrainTimes {

  private final StationTimesFetcher stationTimesFetcher;

  @Inject
  public TrainTimes(final StationTimesFetcher stationTimesFetcher) {
    this.stationTimesFetcher = stationTimesFetcher;
  }

  public Response getResponse() {
    try {
      return new Response(stationTimesFetcher.getFeedData(), null);
    } catch (IOException e) {
      return new Response(null, e.getMessage());
    }
  }
}
