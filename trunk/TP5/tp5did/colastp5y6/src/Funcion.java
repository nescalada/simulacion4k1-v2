/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Admin
 */
public class Funcion {

    private static final double g = 6.672 * Math.pow(10, -11);
    private static final double m = 5.97 * Math.pow(10, 24);
    
    

    public double obtenerValorFuncionX3Prima(double x1, double x2) {
        double numerador = -g * m * x1;

        return calcularValor(x1, x2, numerador);

    }

    public double obtenerValorFuncionX4Prima(double x1, double x2) {
        double numerador = -g * m * x2;

        return calcularValor(x1, x2, numerador);
    }

    private double calcularValor(double x1, double x2, double numerador) {
        double suma = Math.pow(x1, 2) + Math.pow(x2, 2);
        double cuerpoRaiz = Math.pow(suma, 3);
        double denominador = Math.sqrt(cuerpoRaiz);
        double funcion = numerador / denominador;
        return funcion;

    }
    
    
}
