package com.sbiscigl.lambda.mta;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class MTAUrlProviderTest {

  private MTAUrlProvider mtaUrlProvider;

  @Test
  public void shouldGetUrl() throws MalformedURLException {
    String accessKey = RandomStringUtils.randomAlphanumeric(8);
    mtaUrlProvider = new MTAUrlProvider(accessKey);
    URL lTrainStreamURL = mtaUrlProvider.getLTrainStreamURL();
    assertEquals("/mta_esi.php", lTrainStreamURL.getPath());
    assertEquals("datamine.mta.info", lTrainStreamURL.getHost());
    assertEquals(String.format("key=%s&feed_id=2", accessKey), lTrainStreamURL.getQuery());
  }
}
