
public class GestorNumeroAleatorio {

    public GestorNumeroAleatorio() {
    }

    public double[] poisson(int cantValores, double media) {

        double[] result = new double[cantValores];
        //System.out.println("result = "+result);
        for (int i = 0; i < cantValores; i++) {
            result[i] = Generador.poisson(media);
          //  System.out.println("result "+i+" = "+result[i]);
        }
        //System.out.println("result fin= "+result);
        return result;
    }

    public double[] exponencial(int cantValores, double media) {

        double[] result = new double[cantValores];
        for (int i = 0; i < cantValores; i++) {
            result[i] = Generador.exponencialNegativa(media);
        }
        return result;
    }

    public double[] normalSenoCoseno(int cantValores) {

        double[] result = new double[cantValores];
        for (int i = 0; i < cantValores; i++) {
            result[i] = Generador.normal();
        }
        return result;
    }

    public double[] normalSumatoria(int cantValores, int cantValoresSumatoria) {
        double[] result = new double[cantValores];
        for (int i = 0; i < cantValores; i++) {
            result[i] = Generador.normalSumatoria(cantValoresSumatoria);
        }
        return result;
    }

    public double[] CongruenciaAditiva(int cantValores, double b, double m, double semilla) {
        double[] result = new double[cantValores];
        for (int i = 0; i < cantValores; i++) {
            result[i] = Generador.congruenciaAditiva(b, m, semilla);
            semilla = result[i];
        }
        return result;
    }

    public double[] CongruenciaMultiplicativa(int cantValores, double a, double m, double semilla) {
        double[] result = new double[cantValores];
        for (int i = 0; i < cantValores; i++) {
            result[i] = Generador.congruenciaMultiplicativa(a, m, semilla);
            semilla = result[i];
        }
        return result;
    }

    public double[] CongruenciaMixta(int cantValores, double a, double b, double m, double semilla) {
        double[] result = new double[cantValores];
        for (int i = 0; i < cantValores; i++) {
            result[i] = Generador.congruenciaMixta(a, b, m, semilla);
            semilla = result[i];
        }
        return result;
    }

    public double[] uniforme(int cantValores, double a, double b) {

        double[] result = new double[cantValores];
        for (int i = 0; i < cantValores; i++) {
            result[i] = Generador.uniforme(a, b);
        }
        return result;
    }

    public double[] calcularValoresDeFrecuencia(double[] numeros, int intervalos, boolean esContinua) {
        double recorrido = calcularRecorrido(numeros);
        int recorridoAmpliado = (int) recorrido;

        double[] valoresDeFrecuencia;

        if (esContinua) {

            valoresDeFrecuencia = new double[recorridoAmpliado];

            valoresDeFrecuencia[0] = (int) calcularMenor(numeros);
            for (int i = 1; i < valoresDeFrecuencia.length; i++) {
                valoresDeFrecuencia[i] = valoresDeFrecuencia[i - 1]+1;
            }
        } else {

            recorridoAmpliado++;
            double amplitud = (double) recorridoAmpliado / (double) intervalos;

            valoresDeFrecuencia = new double[intervalos + 1];
            valoresDeFrecuencia[0] = (int) calcularMenor(numeros);

            for (int i = 1; i <= intervalos; i++) {
                valoresDeFrecuencia[i] = valoresDeFrecuencia[i - 1] + amplitud;
                valoresDeFrecuencia[i] *= 10;
                valoresDeFrecuencia[i] = Math.round(valoresDeFrecuencia[i]);
                valoresDeFrecuencia[i] /= 10;
            }
        }
        return valoresDeFrecuencia;
    }

    private double calcularRecorrido(double[] numeros) {
        double menor = numeros[0];
        double mayor = numeros[0];
        for (int i = 0; i < numeros.length; i++) {
            double d = numeros[i];
            if (d < menor) {
                menor = d;
            }
            if (d > mayor) {
                mayor = d;
            }

        }
        return (mayor - menor);
    }

    private double calcularMayor(double[] numeros) {
        double mayor = numeros[0];
        for (int i = 0; i < numeros.length; i++) {
            double d = numeros[i];
            if (d > mayor) {
                mayor = d;
            }

        }
        return mayor;
    }

    private double calcularMenor(double[] numeros) {
        double menor = numeros[0];
        for (int i = 0; i < numeros.length; i++) {
            double d = numeros[i];
            if (d < menor) {
                menor = d;
            }
        }
        return menor;
    }
}
