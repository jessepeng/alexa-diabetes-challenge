package de.startupbootcamp.alexachallenge.servlet;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.speechlet.dialog.directives.DelegateDirective;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jan-Christopher on 10.06.2017.
 */
public class CalculateDosageSpeechlet implements Speechlet {

    private static final Logger log = LoggerFactory.getLogger(CalculateDosageSpeechlet.class);

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
//            if (request.getDialogState() != null && request.getDialogState().equals(IntentRequest.DialogState.COMPLETED)) {
                // Calculate the dose
                return getHelloResponse();
//            } else {
//                // Delegate Dialog completion
//                SpeechletResponse response = new SpeechletResponse();
//                List<Directive> directives = new LinkedList<>();
//                directives.add(new DelegateDirective());
//                response.setDirectives(directives);
//                return response;
//            }
        } else if ("AMAZON.HelpIntent".equals(intentName)) {
            return getHelpResponse();
        } else {
            throw new SpeechletException("Invalid Intent");
        }
    }

    private SpeechletResponse getHelloResponse() {
        String speechText = "Welcome to bolus count. This app will help you determine how much you have to bolus. Say ask bolus count to calculate my bolus dose to continue.";

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech);
    }

    private SpeechletResponse getHelpResponse() {
        String speechText = "This app will help you determine how much you have to bolus.";

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech);
    }

}
