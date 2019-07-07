package com.sbiscigl.lambda.mta;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.net.MalformedURLException;
import java.net.URL;

public class MTAUrlProvider {
  private static final String L_TRAIN_STREAM_URL_FORMAT =
      "http://datamine.mta.info/mta_esi.php?key=%s&feed_id=2";
  private final String accessKey;

  @Inject
  public MTAUrlProvider(@Named("accessKey") final String accessKey) {
    this.accessKey = accessKey;
  }

  public URL getLTrainStreamURL() throws MalformedURLException {
    return new URL(String.format(L_TRAIN_STREAM_URL_FORMAT, this.accessKey));
  }
}
