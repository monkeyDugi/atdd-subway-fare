package nextstep.subway.domain;

public class Fare {
    private final int distance;
    private final int additionFare;

    private Fare(int distance, int additionFare) {
        this.distance = distance;
        this.additionFare = additionFare;
    }
    public static Fare createNonAdditionFareFare(int distance) {
        return new Fare(distance, 0);
    }

    public static Fare createFare(int distance, int additionFare) {
        return new Fare(distance, additionFare);
    }

    public int calculateOverFare() {
        return FareStandard.calculateOverFare(distance, additionFare);
    }
}