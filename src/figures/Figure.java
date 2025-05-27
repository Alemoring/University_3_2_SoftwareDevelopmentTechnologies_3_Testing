package figures;

public class Figure {
    private int radius = 0;
    private int smRadius = 0;
    private int widthScaleFactor;
    private final int scaleFactor;
    private boolean isBuilt = false;
    private final int windowWidth;
    private final double arg = Math.sqrt(25 + 10 * Math.sqrt(5));
    private final double sin = Math.sin(0.628319);

    public Figure(int scaleFactor, int windowWidth) {
        this.scaleFactor = scaleFactor;
        this.widthScaleFactor = scaleFactor;

        this.windowWidth = windowWidth;
    }

    public double calculateShadedArea() throws Exception {
        if (this.radius == 0)
            throw new Exception();
        double x = 2 * this.smRadius * this.sin;
        return (Math.PI * this.smRadius * this.smRadius) - (((x * x)/ 4) * this.arg);
    }

    public void setNewRates(String radius) throws NumberFormatException, NullPointerException {
        if (radius.isEmpty())
            throw new NullPointerException();

        if (Integer.parseInt(radius) <= 0)
            throw new NullPointerException();

        if (Integer.parseInt(radius) * this.widthScaleFactor * 2.5 > windowWidth)
            throw new NumberFormatException();

        this.radius = Integer.parseInt(radius) * this.widthScaleFactor;

        this.smRadius = Integer.parseInt(radius);

        isBuilt = true;
    }

    public int getRadius() {
        return this.radius;
    }

    public int getSmRadius() {
        return this.smRadius;
    }

    public void changeScale(double widthScale) {
        if (this.isBuilt) {
            this.radius = this.radius / this.widthScaleFactor;

            this.widthScaleFactor = (int) (this.scaleFactor * widthScale);

            this.radius = this.radius * this.widthScaleFactor;
        } else {
            this.widthScaleFactor = (int) (this.scaleFactor * widthScale);
        }
    }

    public void changeSize(double scale) throws NullPointerException, NumberFormatException {
        if (!isBuilt)
            throw new NullPointerException();

        if ((int) (this.radius * scale) < 10)
            throw new NumberFormatException();

        if ((int) (this.radius * scale * 2.5) > this.windowWidth)
            throw new NumberFormatException();

        if ((int) ((this.radius * scale) / this.widthScaleFactor)  <= 0)
            throw new NumberFormatException();

        this.radius = (int) (this.radius * scale);

        this.smRadius = (this.radius / this.widthScaleFactor);
    }
}
