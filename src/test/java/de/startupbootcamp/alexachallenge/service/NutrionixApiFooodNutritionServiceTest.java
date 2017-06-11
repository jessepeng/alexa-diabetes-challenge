package de.startupbootcamp.alexachallenge.service;

import org.junit.Test;

public class NutrionixApiFooodNutritionServiceTest {

	@Test
	public void test() {
		NutrionixApiFooodNutritionService result = new NutrionixApiFooodNutritionService();
		
		double carbs = result.getCarbsInFood("one apple, two orange");
		double bloodLevel = result.getBloodLevel("Andreas");
		
	}

}
