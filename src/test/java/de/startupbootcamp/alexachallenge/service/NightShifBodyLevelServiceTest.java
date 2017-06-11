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

}
