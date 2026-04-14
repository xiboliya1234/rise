package com.rise.util;

public class math {

    private double intercept;
    private double slope;
    public math() {
        this.intercept = 0;
        this.slope = 0;
    }

    public void fit(double[] x, double[] y) {
        if (x.length != y.length) {
            throw new IllegalArgumentException("The length of x and y must be the same.");
        }

        int n = x.length;
        double xSum = 0, ySum = 0, xySum = 0, xSquareSum = 0;

        for (int i = 0; i < n; i++) {
            xSum += x[i];
            ySum += y[i];
            xySum += x[i] * y[i];
            xSquareSum += x[i] * x[i];
        }

        // Calculate slope and intercept
        this.slope = (n * xySum - xSum * ySum) / (n * xSquareSum - xSum * xSum);
        this.intercept = (ySum - this.slope * xSum) / n;
    }

    public double predict(double x) {
        return this.intercept + this.slope * x;
    }

    public static void main(String[] args) {
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {2, 4, 5, 4, 5};

        math lr = new math();
        lr.fit(x, y);

        System.out.println("Intercept: " + lr.intercept);
        System.out.println("Slope: " + lr.slope);
        System.out.println("Prediction for x=6: " + lr.predict(6));
    }

}
