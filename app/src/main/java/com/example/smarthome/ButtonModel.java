package com.example.smarthome;

public class ButtonModel {
    String buttonName;
    String buttonPinNumber;
    String buttonState;


    public ButtonModel() {
    }

    public ButtonModel(String buttonName) {
        this.buttonName = buttonName;
    }

    public ButtonModel(String buttonName, String buttonPinNumber, String buttonState) {
        this.buttonName = buttonName;
        this.buttonPinNumber = buttonPinNumber;
        this.buttonState = buttonState;
    }

    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonPinNumber() {
        return buttonPinNumber;
    }

    public void setButtonPinNumber(String buttonPinNumber) {
        this.buttonPinNumber = buttonPinNumber;
    }

    public String getButtonState() {
        return buttonState;
    }

    public void setButtonState(String buttonState) {
        this.buttonState = buttonState;
    }
}
