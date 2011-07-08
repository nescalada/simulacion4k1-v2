/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;

/**
 *
 * @author Admin
 */
public class RungeKutta {

    private double h;
    private double t;
    private Funcion simulador;
    private double x1,  x2,  x3,  x4; //Valores iniciales
    private double[] KX1;
    private double[] KX2;
    private double[] KX3;
    private double[] KX4;
    private ArrayList<double[]> lista;
    private double[] array;
    private double tmax;

    public RungeKutta(double h, double x1, double x2, double x3, double x4, double max) {
        this.h = h;
        t = 0;
        tmax = max;
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;
        this.x4 = x4;
        array = new double[21];
        KX1 = new double[4];
        KX2 = new double[4];
        KX3 = new double[4];
        KX4 = new double[4];
        simulador = new Funcion();
        lista = new ArrayList<double[]>();
        cargarEstadoInicial();


    }
     public void RungeKuttaDid(double h, double max) {
        this.h = h;
        t = 0;
        tmax = max;
        array = new double[21];
        simulador = new Funcion();
        lista = new ArrayList<double[]>();
        cargarEstadoInicial();


    }

    private void calcularSiguiente() {
        array[0] = t = t + h;
        for (int i = 1; i < 5; i++) {
            calcularK(i); //Calculamos en orden todos los Ki de todas las variables
        }
        array[1] = x1 = x1 + (h / 6) * (KX1[0] + 2 * KX1[1] + 2 * KX1[2] + KX1[3]);
        array[6] = x2 = x2 + (h / 6) * (KX2[0] + 2 * KX2[1] + 2 * KX2[2] + KX2[3]);
        array[11] = x3 = x3 + (h / 6) * (KX3[0] + 2 * KX3[1] + 2 * KX3[2] + KX3[3]);
        array[16] = x4 = x4 + (h / 6) * (KX4[0] + 2 * KX4[1] + 2 * KX4[2] + KX4[3]);

        
                
        lista.add(array.clone());
    }

    private void calcularK(int i) {
        switch (i) {
//            case 1: {
//                array[2] = KX1[0] = h * x3;
//                array[7] = KX2[0] = h * x4;
//                array[12] = KX3[0] = h * simulador.obtenerValorFuncionX3Prima(x1, x2);
//                array[17] = KX4[0] = h * simulador.obtenerValorFuncionX4Prima(x1, x2);
//                break;
//            }
//            case 2: {
//                array[3] = KX1[1] = h * x3 + KX1[0] / 2;
//                array[8] = KX2[1] = h * x4 + KX2[0] / 2;
//                array[13] = KX3[1] = h * simulador.obtenerValorFuncionX3Prima(x1 + KX3[0] / 2, x2 + KX3[0] / 2);
//                array[18] = KX4[1] = h * simulador.obtenerValorFuncionX4Prima(x1 + KX4[0] / 2, x2 + KX4[0] / 2);
//                break;
//
//            }
//            case 3: {
//                array[4] = KX1[2] = h * x3 + KX1[1] / 2;
//                array[9] = KX2[2] = h * x4 + KX2[1] / 2;
//                array[14] = KX3[2] = h * simulador.obtenerValorFuncionX3Prima(x1 + KX3[1] / 2, x2 + KX3[1] / 2);
//                array[19] = KX4[2] = h * simulador.obtenerValorFuncionX4Prima(x1 + KX4[1] / 2, x2 + KX4[1] / 2);
//                break;
//            }
//            case 4: {
//                array[5] = KX1[3] = h * x3 + KX1[2];
//                array[10] = KX2[3] = h * x4 + KX2[2];
//                array[15] = KX3[3] = h * simulador.obtenerValorFuncionX3Prima(x1 + KX3[2], x2 + KX3[2]);
//                array[20] = KX4[3] = h * simulador.obtenerValorFuncionX4Prima(x1 + KX4[2], x2 + KX4[2]);
//                break;
//            }
            case 1: {
                array[2] = KX1[0] = h * x3;
                array[7] = KX2[0] = h * x4;
                array[12] = KX3[0] = h * simulador.obtenerValorFuncionX3Prima(x1, x2);
                array[17] = KX4[0] = h * simulador.obtenerValorFuncionX4Prima(x1, x2);
                break;
            }
            case 2: {
                array[3] = KX1[1] = h * x3 + KX3[0] / 2;
                array[8] = KX2[1] = h * x4 + KX4[0] / 2;
                array[13] = KX3[1] = h * simulador.obtenerValorFuncionX3Prima(x1 + KX1[0] / 2, x2 + KX2[0] / 2);
                array[18] = KX4[1] = h * simulador.obtenerValorFuncionX4Prima(x1 + KX1[0] / 2, x2 + KX2[0] / 2);
                break;

            }
            case 3: {
                array[4] = KX1[2] = h * x3 + KX3[1] / 2;
                array[9] = KX2[2] = h * x4 + KX4[1] / 2;
                array[14] = KX3[2] = h * simulador.obtenerValorFuncionX3Prima(x1 + KX1[1] / 2, x2 + KX2[1] / 2);
                array[19] = KX4[2] = h * simulador.obtenerValorFuncionX4Prima(x1 + KX1[1] / 2, x2 + KX2[1] / 2);
                break;
            }
            case 4: {
                array[5] = KX1[3] = h * x3 + KX3[2];
                array[10] = KX2[3] = h * x4 + KX4[2];
                array[15] = KX3[3] = h * simulador.obtenerValorFuncionX3Prima(x1 + KX1[2], x2 + KX2[2]);
                array[20] = KX4[3] = h * simulador.obtenerValorFuncionX4Prima(x1 + KX1[2], x2 + KX2[2]);
                break;
            }
            
        }
    }

    private void cargarEstadoInicial() {
        array[0] = t;
        array[1] = x1;
        array[6] = x2;
        array[11] = x3;
        array[16] = x4;
        lista.add(array.clone());
    }

    public double[][] calcular() throws OutOfMemoryError {

        while (t < tmax) {
            calcularSiguiente();
        }
        return armarMatriz();
    }

    private double[][] armarMatriz() {
        double[][] data = new double[lista.size()][20];
        for (int i = 0; i < data.length; i++) {
            data[i] = lista.get(i);
        }

        return data;
    }
}
