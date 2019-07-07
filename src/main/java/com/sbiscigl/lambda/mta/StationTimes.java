package com.sbiscigl.lambda.mta;

import java.util.List;

public class StationTimes {
  private List<Long> manhattanBoundTrains;
  private List<Long> rockawayBoundTrains;

  private StationTimes(
      final List<Long> manhattanBoundTrains, final List<Long> rockawayBoundTrains) {
    this.manhattanBoundTrains = manhattanBoundTrains;
    this.rockawayBoundTrains = rockawayBoundTrains;
  }

  public static StationTimes of(
      final List<Long> manhattanBoundTrains, final List<Long> rockawayBoundTrains) {
    return new StationTimes(manhattanBoundTrains, rockawayBoundTrains);
  }

  public List<Long> getManhattanBoundTrains() {
    return manhattanBoundTrains;
  }

  public List<Long> getRockawayBoundTrains() {
    return rockawayBoundTrains;
  }
}
