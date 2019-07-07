package com.sbiscigl.lambda;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sbiscigl.lambda.handler.GetTrainsHandler;
import com.sbiscigl.lambda.handler.NoContentHandler;
import com.sbiscigl.lambda.inject.TrainTimeModule;

public class AlexaEndpoint extends SkillStreamHandler {

  private static final Injector injector = Guice.createInjector(new TrainTimeModule());

  public AlexaEndpoint() {
    super(getSkill());
  }

  private static Skill getSkill() {
    return Skills.standard()
        .addRequestHandlers(
            injector.getInstance(GetTrainsHandler.class),
            injector.getInstance(NoContentHandler.class))
        .withSkillId(System.getenv("SKILL_ID"))
        .build();
  }
}
