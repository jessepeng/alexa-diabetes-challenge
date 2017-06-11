package de.startupbootcamp.alexachallenge.service;

import de.startupbootcamp.alexachallenge.data.User;

import java.util.*;

/**
 * Stub for looking up the user
 *
 * Created by Jan-Christopher on 10.06.2017.
 */
public class UserService {

    private static final User EXAMPLE_USER;

    private UserService() {}

    private static class ServiceHolder {
        private static final UserService instance = new UserService();
    }

    public static UserService getService() {
        return ServiceHolder.instance;
    }

    static {
        List<User.ExchangeFactor> exchangeFactors = new LinkedList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.YEAR, 1900);
        calendar.set(Calendar.HOUR, 5);
        // 0:00 - 5:00
        exchangeFactors.add(new User.ExchangeFactor(calendar.getTime(), 0.5));
        // 5:00 - 12:00
        calendar.add(Calendar.HOUR, 7);
        exchangeFactors.add(new User.ExchangeFactor(calendar.getTime(), 1.5));
        // 12:00 - 18:00
        calendar.add(Calendar.HOUR, 6);
        exchangeFactors.add(new User.ExchangeFactor(calendar.getTime(), 1.0));
        // 18:00 - 24:00
        calendar.add(Calendar.HOUR, 6);
        exchangeFactors.add(new User.ExchangeFactor(calendar.getTime(), 1.25));
        EXAMPLE_USER = new User(
                60.0,
                167,
                28,
                User.Gender.FEMALE,
                User.DiabetesType.TYPE_1,
                User.TherapyType.CSII,
                3,
                new User.Range(4, 7),
                exchangeFactors);
    }

    public User getUser() {
        return EXAMPLE_USER;
    }

    public double calculateFoodBolusDose(User user, double carbsInFood) {
        double be = carbsInFood / 12.0;
        double exchangeFactor = user.getExchangeFactor(new Date());
        return exchangeFactor * be;
    }

    public double calculateCorrectionBolusDose(User user, double actualBloodGlucose) {
        User.Range targetRange = user.getTargetRange();
        if (actualBloodGlucose > targetRange.getLower()) {
            double difference = actualBloodGlucose - targetRange.getLower();
            return difference / user.getCorrectionFactor();
        } else {
            return 0;
        }
    }

}
