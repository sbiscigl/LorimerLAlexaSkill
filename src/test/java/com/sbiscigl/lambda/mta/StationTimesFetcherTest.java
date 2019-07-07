package com.sbiscigl.lambda.mta;

import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.File;
import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StationTimesFetcherTest {

  private static final String REQUEST_TEST_TIME = "2019-07-04T20:38:00Z";
  private static final String SRC_TEST_RESOURCES_GTFS = "src/test/resources/gtfs";
  private ImmutableList<Long> expectedManhattan =
      ImmutableList.of(2L, 6L, 10L, 14L, 18L, 22L, 26L, 30L, 34L, 38L);
  private ImmutableList<Long> expectedRockaway =
      ImmutableList.of(0L, 5L, 8L, 11L, 17L, 24L, 27L, 31L, 35L, 39L, 43L, 47L, 51L, 55L);

  @Mock private MTAUrlProvider mtaUrlProvider;

  private StationTimesFetcher stationTimesFetcher;

  @Before
  public void before() {
    mtaUrlProvider = mock(MTAUrlProvider.class);
    Clock clock = Clock.fixed(Instant.parse(REQUEST_TEST_TIME), ZoneOffset.UTC);
    stationTimesFetcher = new StationTimesFetcher(mtaUrlProvider, clock);
  }

  @Test
  public void shouldReturnResponse() throws IOException {
    when(mtaUrlProvider.getLTrainStreamURL())
        .thenReturn(new File(SRC_TEST_RESOURCES_GTFS).toURI().toURL());
    StationTimes feedData = stationTimesFetcher.getFeedData();
    Assert.assertThat(expectedManhattan, is(feedData.getManhattanBoundTrains()));
    Assert.assertThat(expectedRockaway, is(feedData.getRockawayBoundTrains()));
  }
}
