package com.onegini.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import android.support.annotation.NonNull;
import retrofit.client.Header;
import retrofit.client.Response;

public class RetrofitResponseUtil {

  public static JSONObject getJsonHeadersFromRetrofitResponse(Response response) throws JSONException {
    final JSONObject result = new JSONObject();

    for (Header header : response.getHeaders()) {
      result.put(header.getName(), header.getValue());
    }

    return result;
  }

  @NonNull
  public static String getBodyStringFromRetrofitResponse(Response response) {
    final BufferedReader reader;
    final StringBuilder stringBuilder = new StringBuilder();

    if (response.getBody() == null) {
      return "";
    }

    try {
      reader = new BufferedReader(new InputStreamReader(response.getBody().in()));
      String line;

      try {
        while ((line = reader.readLine()) != null) {
          stringBuilder.append(line);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }


    return stringBuilder.toString();
  }
}
