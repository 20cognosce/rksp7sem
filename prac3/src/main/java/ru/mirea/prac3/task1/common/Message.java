package ru.mirea.prac3.task1.common;

import java.time.Instant;

public record Message(Kind kind, Integer value, Instant timestamp) {

}
