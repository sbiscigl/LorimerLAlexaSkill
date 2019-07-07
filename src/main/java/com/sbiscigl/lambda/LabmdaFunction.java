package com.sbiscigl.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sbiscigl.lambda.command.TrainTimes;
import com.sbiscigl.lambda.inject.TrainTimeModule;
import com.sbiscigl.lambda.model.Request;
import com.sbiscigl.lambda.model.Response;

public class LabmdaFunction implements RequestHandler<Request, Response> {

  private final Injector injector = Guice.createInjector(new TrainTimeModule());
  private final TrainTimes trainTimes = injector.getInstance(TrainTimes.class);

  @Override
  public Response handleRequest(final Request request, final Context context) {
    return trainTimes.getResponse();
  }
}
