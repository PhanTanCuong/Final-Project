package com.example.cinema.event;

public class ResultQrCodeEvent {

    // A private field to store the result of the QR code scan
    private String result;

    // Constructor to initialize the ResultQrCodeEvent with a result
    public ResultQrCodeEvent(String result) {
        this.result = result;
    }

    // Getter method to retrieve the result
    public String getResult() {
        return result;
    }

    // Setter method to update the result
    public void setResult(String result) {
        this.result = result;
    }
}
