package fr.lacombe;

import java.time.LocalDateTime;

public class MovementDate {


    private LocalDateTime dateTime;

    public MovementDate(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
