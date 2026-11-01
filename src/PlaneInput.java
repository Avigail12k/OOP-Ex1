
public class PlaneInput {

    private final Point3D[] points;
    private final String error;

    public PlaneInput(Point3D[] points, String error) {
        this.points = points == null ? null : points.clone();
        this.error = error;
    }

    public boolean isValid() {
        return error == null;
    }

    public Point3D[] getPoints() {
        return points == null ? null : points.clone();
    }

    public String getError() {
        return error;
    }
}
