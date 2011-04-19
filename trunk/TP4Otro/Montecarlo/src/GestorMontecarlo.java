
import javax.swing.JTable;
import javax.swing.table.TableModel;

public class GestorMontecarlo {

    private final int KM = 3;
    private final int KS = 4;
    private final int KOPolticaA = 280;//El Ko en la politica A es siempre 280.
    private int demanda, //la demanda del día.
            demora, //La demora de un pedido.
            balance, //El stock.
            total[]; //Un array con los totales para sacar la suma.
    private boolean pedido;/* true: Hay un pedido hecho que todavia no ha llegado.
     * false: No hay ningun pedido en camino.*/

    private Object[] nombreColumnas = {"Día", "RND-Demanda", "RND-Demora", "Demanda",
        "Pedido", "Arribo", "Balance", "KM", "KO", "KS",
        "TOTAL"};/*Columnas de la tabla*/


    /**
     * Le mandas el random y te da la demanda.
     * @param random
     * @return la demora en Decenas
     */
    private int getDemanda(double rnd) {

        if (rnd < 0.05) {
            return 0;
        }
        if (rnd < 0.17) {
            return 10;
        }
        if (rnd < 0.35) {
            return 20;
        }
        if (rnd < 0.60) {
            return 30;
        }
        if (rnd < 0.82) {
            return 40;
        }
        return 50;
    }

    private int getDemora(double rnd) {
        if (rnd < 0.15) {
            return 1;
        }
        if (rnd < 0.35) {
            return 2;
        }
        if (rnd < 0.75) {
            return 2;
        }
        return 4;
    }

    private int getKo(int cantidadDecenasPedidas) {
        if (cantidadDecenasPedidas < 101) {
            return 200;
        }
        if (cantidadDecenasPedidas < 201) {
            return 280;
        }
        return 300;
    }

    /**
     * Calcula y genera la tabla de la politica A.
     * @return
     */
    public TableModel simularPoliticaA() {


        int cantidadDeDias = 30;
        Object[][] tabla = new Object[cantidadDeDias + 1][11];//Object[filas][columnas].

        int variableKs = 0, variableKo = 0, variableKm = 0;
        balance = 20;
        demanda = 0;
        pedido = false;
        demora = 0;
        total = new int[cantidadDeDias + 1];
        total[0] = balance * 10 * KS;
        tabla[0][0] = "0";//DIA
        tabla[0][6] = balance;//Balance dia 0
        tabla[0][7] = tabla[0][10] = balance * 10 * KM;//KS y Total del dia 0.

        //i es el numero de dia
        for (int i = 1; i <= cantidadDeDias; i++) {

            tabla[i][0] = i;
            demora--;
            variableKo = 0;
            variableKs = 0;

            if ((i - 1) % 7 == 0) {
                pedido = true;
                tabla[i][4] = 180;

                double rnd = Generador.getNumAleatorio();
                rnd *= 1000;
                rnd = Math.round(rnd);
                rnd /= 1000;

                tabla[i][2] = rnd;
                demora = getDemora(rnd);
                variableKo = KOPolticaA;
            }

            if (pedido && demora == 0) {
                tabla[i][5] = 180;
                balance += 180;
            }

            //Todo esto es para que me quede con tres decimales
            double rnd = Generador.getNumAleatorio();
            rnd *= 1000;
            rnd = Math.round(rnd);
            rnd /= 1000;

            tabla[i][1] = rnd;
            tabla[i][3] = demanda = getDemanda(rnd);
            balance -= demanda;

            if (balance < 0) {
                variableKs = balance * (-1) * 10 * KS;
                balance = 0;
            }
            variableKm = balance * 10 * KM;

            tabla[i][6] = balance;
            tabla[i][7] = variableKm;
            tabla[i][8] = variableKo;
            tabla[i][9] = variableKs;
            tabla[i][10] = total[i] = variableKm + variableKo + variableKs;

        }
        return new JTable(tabla, nombreColumnas).getModel();
    }

    public TableModel simularPoliticaB() {


        int cantidadDeDias = 30;
        Object[][] tabla = new Object[cantidadDeDias + 1][11];//Object[filas][columnas].

        int variableKs = 0, variableKo = 0, variableKm = 0;
        int sumatoriaDemanda = 180;//Sumamos las demandas para el pedido
        int cantidadPedido = 0;//Cantidad que hemos pedido
        balance = 20;
        demanda = 0;
        pedido = false;
        demora = 0;
        total = new int[cantidadDeDias + 1];
        total[0] = balance * 10 * KS;
        tabla[0][0] = "0";//DIA
        tabla[0][6] = balance;//Balance dia 0
        tabla[0][7] = tabla[0][10] = balance * 10 * KM;//KS y Total del dia 0.

        //i es el numero de dia
        for (int i = 1; i <= cantidadDeDias; i++) {

            tabla[i][0] = i;
            demora--;
            variableKo = 0;
            variableKs = 0;


            if (pedido && demora == 0) {//Si llego un pedido
                tabla[i][5] = cantidadPedido;
                balance += cantidadPedido;
                cantidadPedido = 0;
                pedido = false;
                /**False por que ya no tengo pedido nada,
                 * puedo no escribirlo ya de demora sigue decreciendo y es !=0*/
            }

            //Todo esto es para que me quede con tres decimales
            //Genero RND Demanda
            double rnd = Generador.getNumAleatorio();
            rnd *= 1000;
            rnd = Math.round(rnd);
            rnd /= 1000;

            tabla[i][1] = rnd;
            tabla[i][3] = demanda = getDemanda(rnd);
            sumatoriaDemanda += getDemanda(rnd);
            balance -= demanda;

            if (balance < 0) {
                variableKs = balance * (-1) * 10 * KS;
                balance = 0;
            }

            if ((i - 1) % 10 == 0) {//Si es dia de hacer un pedido
                pedido = true;
                tabla[i][4] = sumatoriaDemanda;
                cantidadPedido = sumatoriaDemanda;
                variableKo = getKo(sumatoriaDemanda);
                sumatoriaDemanda = 0;

                //Todo esto es para que me quede con tres decimales
                //Genero RND demora
                double rnd2 = Generador.getNumAleatorio();
                rnd2 *= 1000;
                rnd2 = Math.round(rnd2);
                rnd2 /= 1000;

                tabla[i][2] = rnd2;
                demora = getDemora(rnd2);
            }

            variableKm = balance * 10 * KM;

            tabla[i][6] = balance;
            tabla[i][7] = variableKm;
            tabla[i][8] = variableKo;
            tabla[i][9] = variableKs;
            tabla[i][10] = total[i] = variableKm + variableKo + variableKs;
        }
        return new JTable(tabla, nombreColumnas).getModel();
    }
}
