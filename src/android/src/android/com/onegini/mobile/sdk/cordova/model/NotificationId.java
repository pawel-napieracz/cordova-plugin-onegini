package com.onegini.mobile.sdk.cordova.model;

import java.util.concurrent.atomic.AtomicInteger;

public class NotificationId {

  private static final AtomicInteger integer = new AtomicInteger((int) System.currentTimeMillis());

  public static int getId() {
    return integer.getAndIncrement();
  }
}
