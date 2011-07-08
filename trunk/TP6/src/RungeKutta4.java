public class RungeKutta4 {
    public double resolver(double y, double h) {
        double k1, k2, k3, k4;

        k1 = h * f(y);
        k2 = h * f(y + (h * k1 / 2));
        k3 = h * f(y + (h * k2 / 2));
        k4 = h * f(y + (h * k3));
        double y2 = y + (k1 + 2 * k2 + 2 * k3 + k4) * h / 6;
        return y2;
    }
    public double f(double y){
        double dT=0.01068*y;
        return dT;
    }
}