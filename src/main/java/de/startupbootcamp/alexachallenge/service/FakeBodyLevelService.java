package de.startupbootcamp.alexachallenge.service;

/**
 * Created by Jan-Christopher on 11.06.2017.
 */
public class FakeBodyLevelService implements BodyLevelService {

    private FakeBodyLevelService() {}

    private static class ServiceHolder {
        private static final FakeBodyLevelService instance = new FakeBodyLevelService();
    }

    public static FakeBodyLevelService getService() {
        return ServiceHolder.instance;
    }

    @Override
    public double getGlucseLevel() {
        return 12.6;
    }

    @Override
    public double getActiveInsuline() {
        return 1.2;
    }
}
