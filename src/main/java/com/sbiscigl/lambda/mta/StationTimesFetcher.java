package com.sbiscigl.lambda.mta;

import com.google.inject.Inject;

import java.io.IOException;
import java.time.Clock;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.google.transit.realtime.GtfsRealtime.*;
import static com.google.transit.realtime.GtfsRealtime.TripUpdate.StopTimeEvent;
import static com.google.transit.realtime.GtfsRealtime.TripUpdate.StopTimeUpdate;

public class StationTimesFetcher {

  private static final String LORIMER_SOUTH = "L10S";
  private static final String LORIMER_NORTH = "L10N";

  private MTAUrlProvider mtaUrlProvider;
  private Clock clock;

  @Inject
  public StationTimesFetcher(final MTAUrlProvider mtaUrlProvider, final Clock clock) {
    this.mtaUrlProvider = mtaUrlProvider;
    this.clock = clock;
  }

  public StationTimes getFeedData() throws IOException {
    final FeedMessage feed =
        FeedMessage.parseFrom(mtaUrlProvider.getLTrainStreamURL().openStream());
    final List<Long> manhattanBoundTrains = getTrainTimesForStation(feed, LORIMER_SOUTH);
    final List<Long> rockawayBoundTrains = getTrainTimesForStation(feed, LORIMER_NORTH);
    return StationTimes.of(manhattanBoundTrains, rockawayBoundTrains);
  }

  private List<Long> getTrainTimesForStation(
      final FeedMessage feedMessage, final String stationID) {
    return feedMessage.getEntityList().stream()
        .map(FeedEntity::getTripUpdate)
        .map(TripUpdate::getStopTimeUpdateList)
        .flatMap(List::stream)
        .filter(stopTimeUpdate -> stopTimeUpdate.getStopId().equals(stationID))
        .filter(StopTimeUpdate::hasArrival)
        .map(StopTimeUpdate::getArrival)
        .map(StopTimeEvent::getTime)
        .map(this::transformToMinutesUntil)
        .sorted()
        .collect(Collectors.toList());
  }

  private Long transformToMinutesUntil(final Long arrivalTime) {
    long currentTimeSeconds = TimeUnit.MILLISECONDS.toSeconds(clock.millis());
    if (arrivalTime - currentTimeSeconds < 0) {
      return 0L;
    }
    return TimeUnit.SECONDS.toMinutes(arrivalTime - currentTimeSeconds);
  }
}
