package com.sbiscigl.lambda.model;

import com.sbiscigl.lambda.mta.StationTimes;

public class Response {
  private StationTimes stationTimes;
  private String error;

  public Response() {}

  public Response(final StationTimes stationTimes, final String error) {
    this.stationTimes = stationTimes;
    this.error = error;
  }

  public StationTimes getStationTimes() {
    return stationTimes;
  }

  public String getError() {
    return error;
  }
}
