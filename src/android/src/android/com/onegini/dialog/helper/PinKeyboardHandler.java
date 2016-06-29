package com.onegini.dialog.helper;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class PinKeyboardHandler {

  private final ImageView[] pinInputs;
  private final int pinLength;
  private final PinProvidedListener pinProvidedHandler;
  private int cursorIndex;
  private char[] pin;

  private int inputFocusedBackgroundResourceId;
  private int inputEmptyBackgroundResourceId;
  private int inputFilledBackgroundResourceId;

  public interface PinProvidedListener {
    void onPinProvided(char[] pin);
  }

  public PinKeyboardHandler(final PinProvidedListener handler, final ImageView[] pinInputs) {
    pinProvidedHandler = handler;
    this.pinInputs = pinInputs;
    this.pinLength = pinInputs.length;

    pin = new char[pinLength];
  }

  public void setInputFocusedBackgroundResourceId(final int inputFocusedBackgroundResourceId) {
    this.inputFocusedBackgroundResourceId = inputFocusedBackgroundResourceId;
  }

  public void setInputEmptyBackgroundResourceId(final int inputEmptyBackgroundResourceId) {
    this.inputEmptyBackgroundResourceId = inputEmptyBackgroundResourceId;
  }

  public void setInputFilledBackgroundResourceId(final int inputFilledBackgroundResourceId) {
    this.inputFilledBackgroundResourceId = inputFilledBackgroundResourceId;
  }


  public void onPinDigitRemoved(final ImageButton deleteButton) {
    if (cursorIndex > 0) {
      pinInputs[cursorIndex].setBackgroundResource(inputEmptyBackgroundResourceId);
      pin[--cursorIndex] = '\0';

      if (cursorIndex == 0) {
        deleteButton.setVisibility(View.INVISIBLE);
      }
    }

    pinInputs[cursorIndex].setBackgroundResource(inputFocusedBackgroundResourceId);
  }

  public void onPinDigitPressed(final int digit) {
    if (cursorIndex < pinLength) {
      pin[cursorIndex] = Character.forDigit(digit, 10);
      pinInputs[cursorIndex].setBackgroundResource(inputFilledBackgroundResourceId);
    }

    cursorIndex++;
    final boolean isPinProvided = cursorIndex >= pinLength;
    if (isPinProvided) {
      pinProvidedHandler.onPinProvided(pin.clone());
      clearPin();
    } else {
      pinInputs[cursorIndex].setBackgroundResource(inputFocusedBackgroundResourceId);
    }
  }

  public void reset() {
    for (int i = 0; i < pinLength; i++) {
      pinInputs[i].setBackgroundResource(inputEmptyBackgroundResourceId);
    }
    pinInputs[cursorIndex].setBackgroundResource(inputFocusedBackgroundResourceId);

    clearPin();
    cursorIndex = 0;
  }

  private void clearPin() {
    for (int index = 0; index < pinLength; index++) {
      pin[index] = '\0';
    }
    pin = new char[pinLength];
  }
}
