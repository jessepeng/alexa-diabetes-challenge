package de.startupbootcamp.alexachallenge.servlet;

import com.amazon.speech.speechlet.servlet.SpeechletServlet;

/**
 * Created by Jan-Christopher on 10.06.2017.
 */
public class CalculateDosageSpeechletServlet extends SpeechletServlet {

    public CalculateDosageSpeechletServlet() {
        this.setSpeechlet(new CalculateDosageSpeechlet());
    }
}
