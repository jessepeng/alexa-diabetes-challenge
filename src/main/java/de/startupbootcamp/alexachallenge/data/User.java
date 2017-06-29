package de.startupbootcamp.alexachallenge.data;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Jan-Christopher on 10.06.2017.
 */
public class User {

    private double weight;

    private double height;

    private int age;

    private Gender gender;

    private DiabetesType diabetesType;

    private TherapyType therapyType;

    private double correctionFactor;

    private Range targetRange;

    private List<ExchangeFactor> exchangeFactors;

    public User(double weight, double height, int age, Gender gender, DiabetesType diabetesType, TherapyType therapyType, double correctionFactor, Range targetRange, List<ExchangeFactor> exchangeFactors) {
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.gender = gender;
        this.diabetesType = diabetesType;
        this.therapyType = therapyType;
        this.correctionFactor = correctionFactor;
        this.targetRange = targetRange;
        this.exchangeFactors = exchangeFactors;
    }

    public double getCorrectionFactor() {
        return correctionFactor;
    }

    public void setCorrectionFactor(double correctionFactor) {
        this.correctionFactor = correctionFactor;
    }

    public Range getTargetRange() {
        return targetRange;
    }

    public void setTargetRange(Range targetRange) {
        this.targetRange = targetRange;
    }

    public DiabetesType getDiabetesType() {
        return diabetesType;
    }

    public void setDiabetesType(DiabetesType diabetesType) {
        this.diabetesType = diabetesType;
    }

    public TherapyType getTherapyType() {
        return therapyType;
    }

    public void setTherapyType(TherapyType therapyType) {
        this.therapyType = therapyType;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public double getExchangeFactor(Date timeOfDay) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(timeOfDay);
        calendar.set(Calendar.YEAR, 1900);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        final Date currentDate = calendar.getTime();
        for (ExchangeFactor factor : exchangeFactors) {
            if (factor.getEndTime().after(currentDate)) {
                return factor.getFactor();
            }
        }
        return exchangeFactors.get(0).getFactor();
        //throw new IllegalStateException("The exchange factors are not set correctly.");
    }

    public double getExchangeFactor() {
        return getExchangeFactor(new Date());
    }

    public enum Gender {
        MALE, FEMALE;
    }

    public enum DiabetesType {
        TYPE_1, TYPE_2;
    }

    public enum TherapyType {
        CSII, ICT, CT;
    }

    public static class ExchangeFactor {

        private Date endTime;

        private double factor;

        public ExchangeFactor(Date endTime, double factor) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(endTime);
            // use 1900 as fixed year to make
            calendar.set(Calendar.YEAR, 1900);
            calendar.set(Calendar.DAY_OF_YEAR, 1);
            this.endTime = calendar.getTime();
            this.factor = factor;
        }

        public Date getEndTime() {
            return endTime;
        }

        public void setEndTime(Date endTime) {
            this.endTime = endTime;
        }

        public double getFactor() {
            return factor;
        }

        public void setFactor(double factor) {
            this.factor = factor;
        }
    }

    public static class Range {

        private double lower;

        private double higher;

        public double getLower() {
            return lower;
        }

        public void setLower(double lower) {
            this.lower = lower;
        }

        public double getHigher() {
            return higher;
        }

        public void setHigher(double higher) {
            this.higher = higher;
        }

        public Range(double lower, double higher) {

            this.lower = lower;
            this.higher = higher;
        }
    }

}
