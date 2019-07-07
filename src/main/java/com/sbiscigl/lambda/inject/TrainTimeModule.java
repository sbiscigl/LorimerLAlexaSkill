package com.sbiscigl.lambda.inject;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.sbiscigl.lambda.handler.GetTrainsHandler;
import com.sbiscigl.lambda.handler.NoContentHandler;
import com.sbiscigl.lambda.mta.MTAUrlProvider;
import com.sbiscigl.lambda.mta.StationTimesFetcher;

import java.time.Clock;

public class TrainTimeModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(TrainTimeModule.class);
    bind(MTAUrlProvider.class);
    bind(StationTimesFetcher.class);
    bind(Clock.class).toInstance(Clock.systemDefaultZone());
    bind(String.class)
        .annotatedWith(Names.named("accessKey"))
        .toInstance(System.getenv("ACCESS_KEY"));
    bind(GetTrainsHandler.class);
    bind(NoContentHandler.class);
  }
}
