package de.startupbootcamp.alexachallenge.servlet;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import de.startupbootcamp.alexachallenge.data.*;
import de.startupbootcamp.alexachallenge.data.User;
import de.startupbootcamp.alexachallenge.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Created by Jan-Christopher on 10.06.2017.
 */
public class CalculateDosageSpeechlet implements Speechlet {

    private static final Logger log = LoggerFactory.getLogger(CalculateDosageSpeechlet.class);
    public static final String BLOOD_GLUCOSE = "BLOOD_GLUCOSE";

    private BodyLevelService bodyLevelService;

    private FoodNutritionService foodNutritionService;

    private UserService userService;

    private static final String APP_NAME = "Sugarcane";

    public CalculateDosageSpeechlet() {
        this.foodNutritionService = NutrionixApiFooodNutritionService.getService();
        this.userService = UserService.getService();
        this.bodyLevelService = FakeBodyLevelService.getService();
    }

    @Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any cleanup logic goes here
    }

    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any initialization logic goes here
    }

    @Override
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        return getHelloResponse();
    }

    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        if ("CalculateBolusDose".equals(intentName)) {
            // Check if dialog is complete
            return callBolusIntent(intent, session);
        } else if ("AMAZON.HelpIntent".equals(intentName)) {
            return getHelpResponse();
        } else if ("AMAZON.StopIntent".equals(intentName)){
            return getOKResponse();
        } else {
            throw new SpeechletException("Invalid Intent");
        }
    }

    private SpeechletResponse callBolusIntent(final Intent intent, final Session session) {
        Slot foodSlot = intent.getSlot("food");
        if (foodSlot.getValue() == null && session.isNew()) {
            de.startupbootcamp.alexachallenge.data.User user = userService.getUser();
            double glucoseLevel = bodyLevelService.getGlucoseLevel();
            session.setAttribute(BLOOD_GLUCOSE, glucoseLevel);
            return getInsulineCountAndAskForFoodResponse(glucoseLevel, user.getTargetRange());
        } else if (foodSlot.getValue() != null && !session.isNew()){
            double glucoseLevel = (double)session.getAttribute(BLOOD_GLUCOSE);
            return getBolusCountResponse(foodSlot.getValue(), glucoseLevel);
        } else {
            return getOKResponse();
        }
    }

    private SpeechletResponse getOKResponse() {
        String speechText = "OK.";

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech);
    }

    private SpeechletResponse getBolusCountResponse(String food, double bloodGlucoseLevel) {
        StringBuilder response = new StringBuilder();
        double carbs = foodNutritionService.getCarbsInFood(food);
        BigDecimal rounded = new BigDecimal(carbs).setScale(2, BigDecimal.ROUND_HALF_UP);
        response.append(food);
        response.append(" contains ");
        response.append(rounded.toString());
        response.append(" grams of carbohydrates. You need to bolus ");

        double bolusCount = userService.calculateBolusDose(userService.getUser(), bloodGlucoseLevel, carbs);

        response.append(bolusCount);
        response.append(" units.");
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(response.toString());

        return SpeechletResponse.newTellResponse(speech);
    }

    private SpeechletResponse getInsulineCountAndAskForFoodResponse(double glucoseLevel, User.Range glucoseRange) {
        StringBuilder response = new StringBuilder("I will do that. Your current blood glucose level is ");
        response.append(glucoseLevel);
        response.append(". ");

        if (glucoseLevel < glucoseRange.getLower()) {
            response.append("This is very low! Go eat fast carbs. ");
        } else if (glucoseLevel >= glucoseRange.getHigher()) {
            response.append("This is in your desired range. Good. ");
        } else {
            response.append("This is too high! ");
        }


        response.append("Do you want to eat something?");
        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(response.toString());

        PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
        repromptSpeech.setText("I'm sorry, but I haven't heard back from you. Do you want to eat something?");
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(repromptSpeech);

        return SpeechletResponse.newAskResponse(speech, reprompt);
    }

    private SpeechletResponse getHelloResponse() {
        String speechText = "Welcome to " + APP_NAME + ". This app will help you determine how much you have to bolus. Say ask " + APP_NAME + " to calculate my bolus dose to continue.";

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech);
    }

    private SpeechletResponse getHelpResponse() {
        String speechText = "This app will help you determine how much you have to bolus. Say 'ask " + APP_NAME + " to calculate my bolus dose' to continue.";

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech);
    }

}
