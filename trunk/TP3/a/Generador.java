import java.util.Random;

/**
 * La clase Generador contiene métodos para generar numeros aleatorios de distintas distribuciones.
 * @author Coppa David, Fernandez Ezequiel Enrique, Gutierrez Victor y Muñoz Manuel
 */
public final class Generador {

    /**
     *La clase Generador contiene un constructor vacio para generar una instancia,
     * y asi llamar a sus métodos.
     */
    public Generador() {
    }

    /**
     * Genera un numero aleatorio de distribucion normal con la formula de la sumatoria.
     * <p>La formula es: z = &#8721;x-(m/2)) / &#8730;(m/12).
     * Donde m es la cantidad de numeros de distribucion uniforme generados.
     *
     * @param   m   La cantidad de numeros de distribucion uniforme a generar
     * para calcular un solo numero aleatorio de distribucion Normal.
     * @return  Un solo numero aleatorio de distribucion Normal.
     */
    public static double normalSumatoria(int cantidadValores, double media, double desviacion) {
        double sumatoria = 0, num = 0;
        for (int i = 0; i < cantidadValores; i++) {
            sumatoria += Math.random();
        }
        double z = sumatoria - cantidadValores / 2;
        z = z / Math.sqrt(cantidadValores / 12);

        double x = media + desviacion * z;


        return x;
//        return ((sumatoria / (Math.sqrt(cantidadValores / 12))) - media) / desviacion;
    }

    /**
     * Genera un numero aleatorio de distribucion normal con la formula de seno y coseno.
     * <p>La formula de seno utilizada es: z = &#8730;((-2)*Ln(R1)) * seno(2<i>pi</i>*R2)
     * <p>La formula de coseno utilizada es: z = &#8730;((-2)*Ln(R1)) * coseno(2<i>pi</i>*R2)
     * <p>La decision de que formula usar, la resuelve el método de forma aleatoria.
     * <p>R1 y R2 son generados aleatoriamente, NO se pasan por parametro.
     *
     * @return  Un numero aleatorio de distribucion Normal.
     */
    public static double normal(double media, double desviacion) {
        double r1 = 0, r2 = 0, z = 0;
        r1 = Math.random();
        r2 = Math.random();
        if (Math.random() < (1 / 2)) {
            z = (Math.sqrt((-2) * Math.log(r1)) * Math.sin(2 * Math.PI * r2));
            return media + desviacion * z;
            //return ((Math.sqrt((-2) * Math.log(r1)) * Math.sin(2 * Math.PI * r2)) - media) / desviacion;
        } else {
            return media + desviacion * z;
            //return ((Math.sqrt((-2) * Math.log(r1)) * Math.cos(2 * Math.PI * r2)) - media) / desviacion;
        }
    }

    /**
     * Genera un numero aleatorio de distribucion Poisson.
     *
     * @param media  La media M(x) que se desea de la distribucion.
     * @return  Un numero aleatorio de distribucion de Poisson.
     */
    public static double poisson(double media) {
        double a = Math.pow(Math.E, -media);
        double b = 1;
        int i = -1;
        do {
            i++;
            b = b * Math.random();
        } while (b > a);
        return i;
    }

    /**
     * Genera un numero aleatorio de distribucion exponencial negativa.
     * <p>En este método se usa la transformada inversa.
     *
     * @param media  La media M(x) que se desea de la distribucion.
     * @return Un solo numero aleatorio de distribucion exponencial negativa
     */
    public static double exponencialNegativa(double media) {
        return Math.log(1 - Math.random()) / (-(1 / media));
    }

    /**
     * Genera un numero aleatorio de distribucion uniforme.
     * <p>Este método sirve para llamar a Math.random().
     * Genera numeros de distribucion uniforme comprendidos entre 0 y 1.
     * Es decir mayor o igual que 0 y menor que 1.
     * 
     * @return Un solo numero aleatorio de distribucion uniforme, comprendido entre 0 y 1.
     */
    public static double getNumAleatorio() {
        return Math.random();
    }

    /**
     * Genera un numero aleatorio de distribucion uniforme entre dos numeros [a;b).
     * <p>Este metodo usa la transformada inversa para generar valores entre un valor "a"
     * y un valor "b" de distribucion uniforme, donde a < b.
     * <p>Cabe destacar que es posible que se genere "a", pero nunca se va a generar "b".
     * Es decir se generará un numero aleatorio de distribucion uniforme mayor o igual que "a" y menor que "b".
     *
     * @param a   Valor minimo para generar un numero aleatorio.
     * @param b   Valor Maximo para generar un numero aleatorio.
     * @return   Un numero aleatorio de distribucion uniforme comprendido entre a y b. Intervalo: [a;b)
     */
    public static double uniforme(double a, double b) {
        return (Math.random() * (b - a)) + a;
    }

    /**
     * Devuelve un <i>Random</i> con una semilla determinada.
     * <p>Esto sirve para generar numeros aleatorios con una semilla determinada.
     * Se utilizaría en el caso de que uno quiera repetir la misma secuencia de numeros aleatorios.
     * <i>Random</i> genera numeros de distribucion uniforme comprendidos entre 0 y 1.
     * Es decir mayor o igual que 0 y menor que 1.
     *
     * @param semilla   Semilla con la cual se quiere construir el <i>Random</i>.
     * @return un <i>Random</i> con una semilla determinada.
     */
    public static Random getRandomConSemilla(long semilla) {
        return new Random(semilla);
    }

    /**
     * Genera un numero aleatorio con un generador de congruencia Mixta.
     * <p>Se genera un numero a partir de la siguiente formula: (a * <i>semilla</i> + b) modulo m.
     * Donde a, b y m deben ser numeros enteros
     *
     * @param a   Numero al que se multiplica la semilla para luego sumarle "b" y calcularle el modulo.
     * @param b   Numero al que se le suma (a * <i>semilla</i>) para calcuarle el modulo.
     * @param m   Numero por el cual se va a dividir para calcular el modulo.
     * @param semilla   Semilla para la generacion de un numero aleatorio.
     * @return   Un numero aleatorio de un generador de congruencia mixta.
     */
    public static double congruenciaMixta(double a, double b, double m, double semilla) {
        return (a * semilla + b) % m;
    }

    /**
     * Genera un numero aleatorio con un generador de congruencia Aditiva.
     * <p>Se genera un numero a partir de la siguiente formula: (<i>semilla</i> + b) modulo m.
     * Donde b y m deben ser numeros enteros
     * <p>Es como el generador de congruencia mixta cuando "a" es igual a 1(uno): (1 * <i>semilla</i> + b) modulo m.
     *
     * @param b   Numero al que se le suma la semilla para calcuarle el modulo
     * @param m   Numero por el cual se va a dividir para calcular el modulo.
     * @param semilla   Semilla para la generacion de un numero aleatorio.
     * @return   Un numero aleatorio de un generador de congruencia aditiva.
     */
    public static double congruenciaAditiva(double b, double m, double semilla) {
        return (semilla + b) % m;
    }

    /**
     * Genera un numero aleatorio con un generador de congrencia multiplicativa.
     * <p>Se genera un numero a partir de la siguiente formula: (a * <i>semilla</i>) modulo m.
     * Donde a y m deben ser numeros enteros
     * <p>Es como el generador de congruencia mixta cuando "b" es igual a 0(cero): (a * semilla + 0) mod m.
     *
     * @param a   Numero al que se multiplica la semilla para calcularle el modulo.
     * @param m   Numero por el cual se va a dividir para calcular el modulo.
     * @param semilla   Semilla para la generacion de un numero aleatorio.
     * @return   Un numero aleatorio de un generador de congruencia multiplicativa.
     */
    public static double congruenciaMultiplicativa(double a, double m, double semilla) {
        return (a * semilla) % m;
    }
}
