package fr.lacombe;

import java.io.Serializable;
import java.time.LocalDateTime;

public class EffectiveDate implements Serializable {

    private LocalDateTime localDateTime;

    public EffectiveDate(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
}
