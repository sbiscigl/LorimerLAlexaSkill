package com.sbiscigl.lambda.command;

import com.sbiscigl.lambda.model.Response;
import com.sbiscigl.lambda.mta.StationTimes;
import com.sbiscigl.lambda.mta.StationTimesFetcher;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TrainTimesTest {

  @Mock private StationTimesFetcher stationTimesFetcher;

  private TrainTimes trainTimes;

  @Before
  public void before() {
    stationTimesFetcher = mock(StationTimesFetcher.class);
    trainTimes = new TrainTimes(stationTimesFetcher);
  }

  @Test
  public void shouldGetTimes() throws IOException {
    StationTimes stationTimes = mock(StationTimes.class);
    when(stationTimesFetcher.getFeedData()).thenReturn(stationTimes);
    Response response = trainTimes.getResponse();
    assertEquals(stationTimes, response.getStationTimes());
    assertNull(response.getError());
  }

  @Test
  public void shouldReturnErrorWhenExceptionEncounterd() throws IOException {
    String message = RandomStringUtils.randomAlphanumeric(8);
    when(stationTimesFetcher.getFeedData()).thenThrow(new IOException(message));
    Response response = trainTimes.getResponse();
    assertNull(response.getStationTimes());
    assertEquals(message, response.getError());
  }
}
