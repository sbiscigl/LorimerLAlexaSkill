package com.sbiscigl.lambda.model;

import com.sbiscigl.lambda.mta.StationTimes;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class ResponseTest {

  @Test
  public void shouldConstruct() {
    StationTimes stationTimes = mock(StationTimes.class);
    String error = RandomStringUtils.randomAlphanumeric(8);
    Response response = new Response(stationTimes, error);
    assertEquals(stationTimes, response.getStationTimes());
    assertEquals(error, response.getError());
  }

  @Test
  public void shouldConstructEmpty() {
    Response response = new Response();
    assertNull(response.getStationTimes());
    assertNull(response.getError());
  }
}
