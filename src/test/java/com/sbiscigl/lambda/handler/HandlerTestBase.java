package com.sbiscigl.lambda.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.*;

abstract class HandlerTestBase {
  protected HandlerInput createHandlerInputWithSessionEndedRequest() {
    return HandlerInput.builder()
        .withRequestEnvelope(
            RequestEnvelope.builder().withRequest(SessionEndedRequest.builder().build()).build())
        .build();
  }

  protected HandlerInput createHandlerInputWithLaunchRequest() {
    return HandlerInput.builder()
        .withRequestEnvelope(
            RequestEnvelope.builder().withRequest(LaunchRequest.builder().build()).build())
        .build();
  }

  protected HandlerInput createHandlerInputWithIntent(final String intentName) {
    return HandlerInput.builder()
        .withRequestEnvelope(
            RequestEnvelope.builder()
                .withRequest(
                    IntentRequest.builder()
                        .withIntent(Intent.builder().withName(intentName).build())
                        .build())
                .build())
        .build();
  }
}
