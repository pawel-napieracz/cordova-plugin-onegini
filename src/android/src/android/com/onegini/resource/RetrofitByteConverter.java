package com.onegini.resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ClassCastException;
import java.lang.IllegalArgumentException;
import java.lang.RuntimeException;
import java.lang.reflect.Type;

import org.apache.commons.io.IOUtils;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

public class RetrofitByteConverter implements Converter {

  @Override
  public Object fromBody(final TypedInput body, final Type type) throws ConversionException {
    return fromTypedInput(body);
  }

  @Override
  public TypedOutput toBody(final Object object) {
    try {
      final byte[] bytes = ((String) object).getBytes("UTF-8");
      return new ByteTypedOutput(bytes);
    } catch (final ClassCastException e) {
      throw new IllegalArgumentException("Request body should be a String", e);
    } catch (final UnsupportedEncodingException e) {
      throw new RuntimeException("Unable to convert String body using UTF-8", e);
    }
  }

  public static byte[] fromTypedInput(final TypedInput input) {
    try {
      return IOUtils.toByteArray(input.in());
    } catch (IOException e) {
      return new byte[0];
    }
  }

  private static class ByteTypedOutput implements TypedOutput {

    private final byte[] bytes;

    public ByteTypedOutput(final byte[] bytes) {
      this.bytes = bytes;
    }

    @Override
    public String fileName() {
      return null;
    }

    @Override
    public String mimeType() {
      return "";
    }

    @Override
    public long length() {
      return bytes.length;
    }

    @Override
    public void writeTo(final OutputStream out) throws IOException {
      out.write(bytes);
    }
  }

}
