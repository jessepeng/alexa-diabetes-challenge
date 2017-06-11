package de.startupbootcamp.alexachallenge.service;

import org.junit.Test;

/**
 * Created by Jan-Christopher on 11.06.2017.
 */
public class NightShifBodyLevelServiceTest {

    private NightshiftBodyLevelService service = new NightshiftBodyLevelService();

    @Test
    public void testBodyGlucose() {
        double glucoseLevel = service.getGlucoseLevel();
    }

    @Test
    public void testLastInsuline() {
        double elapsedSeconds = 60 * 60 * 3;
        double insuline = 4.5;
        double resultInsuline = (1.0 / (Math.sqrt(2 * Math.PI))) + Math.exp(-(1.0 / 2.0) * Math.pow(elapsedSeconds + 3600, 2.0)) * insuline;
    }

}
