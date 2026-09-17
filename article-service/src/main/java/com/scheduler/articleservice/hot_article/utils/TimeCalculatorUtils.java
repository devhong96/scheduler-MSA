package com.scheduler.articleservice.hot_article.utils;

import java.time.Duration;
import java.time.LocalDateTime;

import static java.time.LocalTime.MIDNIGHT;

public class TimeCalculatorUtils {
    public static Duration calculateDurationToMidnight() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.plusDays(1).with(MIDNIGHT);
        return Duration.between(now, midnight);
    }
}

