import java.awt.geom.AffineTransform;

public class Camera {
    final static private double defaultZoomFactor = 3;

    private double zoomFactor;
    private double cameraOffsetX;
    private double cameraOffsetY;


    public Camera() {
        zoomFactor = defaultZoomFactor;
        cameraOffsetX = 0;
        cameraOffsetY = 0;
    }

    public double getZoomFactor() {
        return zoomFactor;
    }

    public void setZoomFactor(double zoomFactor) {
        this.zoomFactor = zoomFactor;
    }

    public void zoomIn(double zoom) {
        zoomFactor *= zoom;
    }

    public void zoomOut(double zoom) {
        zoomFactor /= zoom;
    }

    public void setDefaultZoomFactor() {
        zoomFactor = defaultZoomFactor;
    }

    public double getCameraOffsetX() {
        return cameraOffsetX;
    }

    public void setCameraOffsetX(double cameraOffsetX) {
        this.cameraOffsetX = cameraOffsetX;
    }

    public double getCameraOffsetY() {
        return cameraOffsetY;
    }

    public void setCameraOffsetY(double cameraOffsetY) {
        this.cameraOffsetY = cameraOffsetY;
    }


    public void move(double offsetX, double offsetY) {
        cameraOffsetX += offsetX / zoomFactor;
        cameraOffsetY += offsetY / zoomFactor;
    }
}
