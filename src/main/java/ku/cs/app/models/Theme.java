package ku.cs.app.models;

public class Theme {
    private boolean lightMode  = true;

    public boolean isLightMode() {
        return lightMode;
    }

    public void setLightMode(boolean lightMode) {
        this.lightMode = lightMode;
    }

    @Override
    public String toString() {
        return "Theme{" +
                "lightMode=" + lightMode +
                '}';
    }
}
