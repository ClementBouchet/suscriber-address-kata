package fr.lacombe;

import org.springframework.stereotype.Service;

@Service
public class TimeProvider implements TimeProviderInterface{
    @Override
    public MovementDate now() {
        return null;
    }
}
