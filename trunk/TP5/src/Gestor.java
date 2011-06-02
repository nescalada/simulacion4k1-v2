
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class Gestor {

    private Playa playa;
    private LinkedList<Auto> colaParaCobrar;
    private LinkedList<Evento> eventos;
    private double contadorCobro, indice, contadorNombreAuto = 1, reloj = 0;
    private int numeroEvento = 0;
    private String[][] datosParaTabla;
    private String[] columnas = {"Nº", "Evento", "Reloj", "RND Tiempo entre Llegada",
        "Tiempo entre Llegada", "Hora Proxima Llegada", "RND Tipo de Auto", "Tipo de Auto",
        "RND Tiempo de Estacionamiento", "Tiempo de Estacionamiento",
        "Hora de salida"};

    /** Indices de las comlumnas de la tabla
     *
     * 0 - Numero de Evento
     * 1 - Evento
     * 2 - Reloj
     * 3 - RND Tiempo entre Llegada
     * 4 - Tiempo entre Llegada
     * 5 - Hora Proxima Llegada
     * 6 - RND Tipo de Auto
     * 7 - Tipo de Auto
     * 8 - RND Tiempo de Estacionamiento
     * 9 - Tiempo de estacionamiento
     * 10 - Hora de salida
     *
     *
     *
     *
     *
     *
     */
    public Gestor() {
        //limpiar();
    }

    public TableModel getProximasMilHoras(double indice, int horasDeSimulacion) {
        this.indice = indice;

        if (numeroEvento == 0) {
            limpiar();
        }

        for (int i = 0; i < horasDeSimulacion; i++) {

            Collections.sort(eventos);

            Evento e = eventos.removeFirst();

            numeroEvento++;
            datosParaTabla[numeroEvento][0] = String.valueOf(numeroEvento);


            e.getDescripcion();
            reloj = e.getHoraEvento();

            switch (e.getIntTipoEvento()) {


                case 1:
                    gestionarLlegadaProximoAuto(e);
                    break;
                case 2:
                    gestionarFinDeEstacionamiento();
                    break;
                case 3:
                    gestionarFinDeCobro();
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Error al obtener evento en el switch");
            }
        }
        return new DefaultTableModel(datosParaTabla, columnas);
    }

    /**
     * Devuelve TRUE si la playa tiene lugar para un auto mas, es decir, que pudo ingresar correctamente,
     * si da FALSE, es porque no hay mas lugar en la playa y el auto no pudo estacionar, SE FUE!!!
     * @param auto
     * @return BOOLEAN
     */
    private boolean agregarEstacionamiento(Estacionamiento estacionamiento) {
        if (playa.estaLibre()) {
            playa.agregarEstacionamiento(estacionamiento);
            return true;
        }
        return false;
    }

    private double getTiempoEntreLlegada(double indice, double rndTiempoEntreLlegada) {
        return ((double) Math.round((-indice * Math.log(1 - rndTiempoEntreLlegada)) * 100)) / 100;
    }

    private void limpiar() {
        numeroEvento = 0;

        reloj = 0;
        double rndProximaLlegada = getNumeroAleatorio();
        double horaproximaLlegada = getTiempoEntreLlegada(indice, rndProximaLlegada);

        playa = new Playa();
        eventos = new LinkedList<Evento>();
        crearEventoProximaLlegada(horaproximaLlegada);

        datosParaTabla = new String[1000][20];

        datosParaTabla[0][0] = "0";
        datosParaTabla[0][1] = "Inicio";
        datosParaTabla[0][2] = "0";
        datosParaTabla[0][3] = String.valueOf(rndProximaLlegada);
        datosParaTabla[0][4] = String.valueOf(rndProximaLlegada);
        datosParaTabla[0][5] = String.valueOf(horaproximaLlegada);


    }

    private boolean crearEventoProximaLlegada(double horaProximaLlegada) {
        return eventos.add(new Evento(1, horaProximaLlegada, null, null));
    }

    private boolean crearEventoFinEstacionamiento(double horaFinEstacionamiento, Estacionamiento e) {
        return eventos.add(new Evento(2, horaFinEstacionamiento, e, null));
    }

    private boolean crearEventoFinCobro(double horaFinCobro, Auto a) {
        return eventos.add(new Evento(3, horaFinCobro, null, a));
    }

    private int getProximoEvento() {
        Collections.sort((List) eventos);

        return eventos.getFirst().getIntTipoEvento();
    }

    private void gestionarLlegadaProximoAuto(Evento e) {

        double rndTipoDeAuto = getNumeroAleatorio();
        double rndTiempoDeEstacionamiento = getNumeroAleatorio();

        Auto auto = new Auto("Auto" + (int) contadorNombreAuto, rndTipoDeAuto, reloj, rndTiempoDeEstacionamiento);
        contadorNombreAuto++;
        Estacionamiento esta = new Estacionamiento();
        esta.agregarAuto(auto);

        double rndTiempoEntreLlegada = getNumeroAleatorio();
        double tiempoEntreLlegada = getTiempoEntreLlegada(indice, rndTiempoEntreLlegada);


        datosParaTabla[numeroEvento][1] = e.getDescripcion() + auto.getNombre();
        datosParaTabla[numeroEvento][2] = String.valueOf(reloj);
        datosParaTabla[numeroEvento][3] = String.valueOf(rndTiempoEntreLlegada);
        datosParaTabla[numeroEvento][4] = String.valueOf(tiempoEntreLlegada);
        datosParaTabla[numeroEvento][5] = String.valueOf(arreglarNumero(tiempoEntreLlegada + reloj));
        datosParaTabla[numeroEvento][6] = String.valueOf(rndTipoDeAuto);
        datosParaTabla[numeroEvento][7] = String.valueOf(auto.getTipoDeAuto());

        //Si hay lugar en la playa
        if (agregarEstacionamiento(esta)) {
            crearEventoFinEstacionamiento(auto.getHoraSalida(), esta);

            datosParaTabla[numeroEvento][8] = String.valueOf(rndTiempoDeEstacionamiento);
            datosParaTabla[numeroEvento][9] = String.valueOf(auto.getTiempoDeEstacionamiento());
            datosParaTabla[numeroEvento][10] = String.valueOf(auto.getHoraSalida());




        } //Si no hay lugar en la playa
        else {
            esta.getAuto().getNombre();
            esta.getAuto().getHoraEntrada();
        }



        crearEventoProximaLlegada(arreglarNumero(reloj + tiempoEntreLlegada));
    }

    private void gestionarFinDeEstacionamiento() {
    }

    private void gestionarFinDeCobro() {
    }

    private double getNumeroAleatorio() {
        double r = (double) Math.round(Math.random() * 100) / 100;
        if (r == 1) {
            return 0.99;
        } else {
            return r;
        }
    }

    public double arreglarNumero(double numero) {
        numero *= 100;
        numero = Math.round(numero);
        numero /= 100;
        return numero;
    }
}
