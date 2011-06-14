package tp5colas;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class Gestor {

    private double precioAutoPequeno, precioAutoGrande, precioUtilitario;
    private Playa playa;
    private LinkedList<Evento> colaParaCobrar;
    private LinkedList<Evento> eventos;
    private double contadorCobro, indiceDistribucion, contadorNombreAuto = 1, reloj = 0;
    private int numeroEvento = 0, indiceParaTabla = 0;
    private String[][] datosParaTabla;
    private String[] columnas = {"Nº", "Evento", "Reloj", "RND Tiempo entre Llegada",
        "Tiempo entre Llegada", "Hora Proxima Llegada", "RND Tipo de Auto", "Tipo de Auto",
        "Estado de Playa", "RND Tiempo de Estacionamiento", "Tiempo de Estacionamiento",
        "Hora de salida", "Estado Zona de Cobro", "Cola de Cobro",
        "Cobro", "Contador de Cobros"};

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
     * 8 - Estado de Playa
     * 9 - RND Tiempo de Estacionamiento
     * 10 - Tiempo de estacionamiento
     * 11 - Hora de salida
     *
     * 12 - Estado Zona de Cobro
     * 13 - Cola de Cobro
     * 14 - Cobro
     * 15 - Contador de Cobros
     *
     *
     *
     *
     *
     */
    public Gestor() {
        //limpiar();
    }

    public TableModel getProximasMilHoras(double indice, int horasDeSimulacion,
            double precioAutoPequeno, double precioAutoGrande,
            double precioUtilitario,
            JLabel jlHoraProximaLlegada,
            JTable jTablePlaya,
            JTable jTableZonaDeCobro) 
    {

        this.precioAutoPequeno = precioAutoPequeno;
        this.precioAutoGrande = precioAutoGrande;
        this.precioUtilitario = precioUtilitario;
        this.indiceDistribucion = indice;
        if (numeroEvento == 0) {
            limpiar();
        }

        horasDeSimulacion += reloj;
        horasDeSimulacion = Math.round(horasDeSimulacion);

        while (reloj < horasDeSimulacion) {

            Collections.sort(eventos);

            Evento e = eventos.removeFirst();

            numeroEvento++;
            indiceParaTabla++;

            //Si aca lanza una excepcion es porque se acabaron las filas de la tabla.
            try {
                datosParaTabla[indiceParaTabla][0] = String.valueOf(numeroEvento);
            } catch (ArrayIndexOutOfBoundsException aIOOBE) {
                /*
                 * La solucion es volver a generar la tabla para limpiarla!!!
                 */
                datosParaTabla = new String[1000][16];
                indiceParaTabla = 0;
                datosParaTabla[indiceParaTabla][0] = String.valueOf(numeroEvento);
            }

            datosParaTabla[indiceParaTabla][1] = e.getDescripcion();
            reloj = arreglarNumero(e.getHoraEvento());
            datosParaTabla[indiceParaTabla][2] = String.valueOf(reloj);

            switch (e.getIntTipoEvento()) {


                case 1:
                    gestionarLlegadaProximoAuto(e);
                    break;
                case 2:
                    gestionarFinDeEstacionamiento(e);
                    break;
                case 3:
                    gestionarFinDeCobro(e);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Error al obtener evento en el switch");
            }
        }
        llenarTablas(jlHoraProximaLlegada,jTablePlaya,jTableZonaDeCobro);

//         jlHoraProximaLlegada.setText(String.valueOf(g.getProximaLlegada()));
//        jTablePlaya.setModel(g.getTablaDePlaya());
//
//        jTableZonaDeCobro.setModel(g.getTablaZonaDeCobro());
        return new DefaultTableModel(datosParaTabla, columnas);
    }
    public void llenarTablas(JLabel jlHoraProximaLlegada, JTable jTablePlaya, JTable jTableZonaDeCobro)
    {
        jlHoraProximaLlegada.setText(String.valueOf(getProximaLlegada()));
        jTablePlaya.setModel(getTablaDePlaya());
        jTableZonaDeCobro.setModel(getTablaZonaDeCobro());
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
        indiceParaTabla = 0;
        reloj = 0;
        contadorCobro = 0;
        contadorNombreAuto = 0;
        double rndProximaLlegada = getNumeroAleatorio();
        double horaproximaLlegada = getTiempoEntreLlegada(indiceDistribucion, rndProximaLlegada);

        playa = new Playa();
        eventos = new LinkedList<Evento>();
        colaParaCobrar = new LinkedList<Evento>();
        crearEventoProximaLlegada(horaproximaLlegada);

        datosParaTabla = new String[1000][16];

        datosParaTabla[0][0] = "0";
        datosParaTabla[0][1] = "Inicio";
        datosParaTabla[0][2] = "0";
        datosParaTabla[0][3] = String.valueOf(rndProximaLlegada);
        datosParaTabla[0][4] = String.valueOf(horaproximaLlegada);
        datosParaTabla[0][5] = String.valueOf(horaproximaLlegada);


    }

    private boolean crearEventoProximaLlegada(double horaProximaLlegada) {
        return eventos.add(new Evento(1, horaProximaLlegada, null, null));
    }

    private boolean crearEventoFinEstacionamiento(double horaFinEstacionamiento, Estacionamiento e) {
        return eventos.add(new Evento(2, horaFinEstacionamiento, e, null));
    }

    private boolean crearEventoFinCobro(double horaDeCobro, Auto a) {
        Evento ev = new Evento(3, horaDeCobro, null, a);
        colaParaCobrar.addLast(ev);
        return eventos.add(ev);
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
        double tiempoEntreLlegada = getTiempoEntreLlegada(indiceDistribucion, rndTiempoEntreLlegada);


        datosParaTabla[indiceParaTabla][1] = e.getDescripcion() + auto.getNombre();
//        datosParaTabla[indiceParaTabla][2] = String.valueOf(reloj);
        datosParaTabla[indiceParaTabla][3] = String.valueOf(rndTiempoEntreLlegada);
        datosParaTabla[indiceParaTabla][4] = String.valueOf(tiempoEntreLlegada);
        datosParaTabla[indiceParaTabla][5] = String.valueOf(arreglarNumero(tiempoEntreLlegada + reloj));
        datosParaTabla[indiceParaTabla][6] = String.valueOf(rndTipoDeAuto);
        datosParaTabla[indiceParaTabla][7] = String.valueOf(auto.getTipoDeAuto());

        //Si hay lugar en la playa
        if (agregarEstacionamiento(esta)) {
            crearEventoFinEstacionamiento(auto.getHoraSalida(), esta);
            datosParaTabla[indiceParaTabla][8] = "Libre";
            datosParaTabla[indiceParaTabla][9] = String.valueOf(rndTiempoDeEstacionamiento);
            datosParaTabla[indiceParaTabla][10] = String.valueOf(auto.getTiempoDeEstacionamiento());
            datosParaTabla[indiceParaTabla][11] = String.valueOf(auto.getHoraSalida());
        } //Si no hay lugar en la playa
        else {
            datosParaTabla[indiceParaTabla][8] = "NO HAY LUGAR";
        }

        crearEventoProximaLlegada(arreglarNumero(reloj + tiempoEntreLlegada));

        actualizarTabla();
    }

    private void gestionarFinDeEstacionamiento(Evento e) {

        //Si no hay cola para pagar!!!Si esta libre la zona de cobro
        if (colaParaCobrar.isEmpty()) {

            datosParaTabla[indiceParaTabla][12] = "Libre";
            crearEventoFinCobro(reloj + 2, e.getEstacionamiento().getAuto());
            //colaParaCobrar.addLast(e);
        } //Si la zona de cobro esta ocupada, hay que hacer cola....
        else {
            datosParaTabla[indiceParaTabla][12] = "OCUPADA";
            crearEventoFinCobro(colaParaCobrar.getLast().getHoraEvento() + 2, e.getEstacionamiento().getAuto());
            //colaParaCobrar.addLast(e);
        }

        playa.eliminarEstacionamiento(e.getEstacionamiento());
        actualizarTabla();
//     * 12 - Estado Zona de Cobro
//     * 13 - Cola de Cobro
//     * 14 - Cobro
//     * 15 - Contador de Cobros
    }

    private void gestionarFinDeCobro(Evento e) {

        Evento t = colaParaCobrar.removeFirst();
        datosParaTabla[indiceParaTabla][7] = e.getAuto().getTipoDeAuto();
        /* 1 - Auto Pequeño
         * 2 - Auto Grande
         * 3 - Utilitario
         */
        switch (e.getAuto().getIntTipoDeAuto()) {
            case 1:
                datosParaTabla[indiceParaTabla][14] =
                        String.valueOf(arreglarNumero(e.getAuto().getTiempoDeEstacionamiento() / 60 * precioAutoPequeno));
                contadorCobro +=
                        arreglarNumero(e.getAuto().getTiempoDeEstacionamiento() / 60 * precioAutoPequeno);
                break;
            case 2:
                datosParaTabla[indiceParaTabla][14] =
                        String.valueOf(arreglarNumero(e.getAuto().getTiempoDeEstacionamiento() / 60 * precioAutoGrande));
                contadorCobro +=
                        arreglarNumero(e.getAuto().getTiempoDeEstacionamiento() / 60 * precioAutoGrande);
                break;
            case 3:
                datosParaTabla[indiceParaTabla][14] =
                        String.valueOf(arreglarNumero(e.getAuto().getTiempoDeEstacionamiento() / 60 * precioUtilitario));
                contadorCobro +=
                        arreglarNumero(e.getAuto().getTiempoDeEstacionamiento() / 60 * precioUtilitario);
                break;
            default:
                datosParaTabla[indiceParaTabla][14] = "Error en el switch de gestion fin de cobro";
        }
        datosParaTabla[indiceParaTabla][15] = String.valueOf(contadorCobro);
        datosParaTabla[indiceParaTabla][10] = String.valueOf(e.getAuto().getTiempoDeEstacionamiento());
        actualizarTabla();
    }

    private void actualizarTabla() {

        if (playa.estaLibre()) {
            datosParaTabla[indiceParaTabla][8] = "Libre";
        } //Si no hay lugar en la playa
        else {
            datosParaTabla[indiceParaTabla][8] = "NO HAY LUGAR";
        }

        //Si no hay cola para pagar!!!Si esta libre la zona de cobro
        if (colaParaCobrar.isEmpty()) {
            datosParaTabla[indiceParaTabla][12] = "Libre";
        } //Si la zona de cobro esta ocupada, hay que hacer cola....
        else {
            datosParaTabla[indiceParaTabla][12] = "OCUPADA";
        }

        if (colaParaCobrar.isEmpty()) {
            datosParaTabla[indiceParaTabla][13] = "0";
        } else {
            datosParaTabla[indiceParaTabla][13] = String.valueOf(colaParaCobrar.size() - 1);
        }


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

    public TableModel reiniciar() {
        numeroEvento = 0;
        return new DefaultTableModel(new Object[1][16], columnas);
    }

    public TableModel getTablaDePlaya() {
        return playa.getTableModel();
    }

    public TableModel getTablaZonaDeCobro() {
        String[] col = {"Auto", "Tipo de Auto", "Tiempo de Estacionamiento",
            "Hora Fin De Cobro"};
        String[][] dat = new String[20][4];
        int i = -1;

        for (Evento evento : colaParaCobrar) {
            i++;
            dat[i][0] = evento.getAuto().getNombre();
            dat[i][1] = String.valueOf(evento.getAuto().getIntTipoDeAuto());
            dat[i][2] = String.valueOf(evento.getAuto().getTiempoDeEstacionamiento());
            dat[i][3] = String.valueOf(evento.getHoraEvento());
        }
        return new DefaultTableModel(dat, col);
    }

    public double getProximaLlegada() {
        for (Evento eve : eventos) {
            if (eve.getIntTipoEvento() == 1) {
                return eve.getHoraEvento();
            }
        }
        return 0;
    }


    /*
     * este metodo lo que hace es ir listando por evento y llena las tablas, lo que hay que hacerle es q en cada paso
     * guarde los registros en un archivo y me quede con los datos del ultimo evento q paso,
     * asi podemos ir viendo en cada paso lo q va pasando y q viene en el proximo,
     * para mostrar todo eso lo q hay q hacer es una consulta ala bd y listo (la bd o archivo)
     * -----------------------------------------------------------------------------------------------
     * cuando este la bd, no hay que hacer q pase evento a evento, si no como estaba antes (junto con el metodo llenar tablas)
     * entonces: en cada paso se guarda los eventos en la bd y podemos tener q paso en cada momento...
     * 
     */
    TableModel deAEventos(double indice, int horasDeSimulacion,
            double precioAutoPequeno, double precioAutoGrande,
            double precioUtilitario,
            JLabel jlHoraProximaLlegada,
            JTable jTablePlaya,
            JTable jTableZonaDeCobro)
    {

        if (numeroEvento == 0) {
            limpiar();
            this.precioAutoPequeno = precioAutoPequeno;
            this.precioAutoGrande = precioAutoGrande;
            this.precioUtilitario = precioUtilitario;
            this.indiceDistribucion = indice;
            horasDeSimulacion += reloj;
            horasDeSimulacion = Math.round(horasDeSimulacion);
        }

        

        if (reloj < horasDeSimulacion) {

            Collections.sort(eventos);

            Evento e = eventos.removeFirst();

            numeroEvento++;
            indiceParaTabla++;

            //Si aca lanza una excepcion es porque se acabaron las filas de la tabla.
            try {
                datosParaTabla[indiceParaTabla][0] = String.valueOf(numeroEvento);
            } catch (ArrayIndexOutOfBoundsException aIOOBE) {
                /*
                 * La solucion es volver a generar la tabla para limpiarla!!!
                 */
                datosParaTabla = new String[1000][16];
                indiceParaTabla = 0;
                datosParaTabla[indiceParaTabla][0] = String.valueOf(numeroEvento);
            }

            datosParaTabla[indiceParaTabla][1] = e.getDescripcion();
            reloj = arreglarNumero(e.getHoraEvento());
            datosParaTabla[indiceParaTabla][2] = String.valueOf(reloj);

            switch (e.getIntTipoEvento()) {


                case 1:
                    gestionarLlegadaProximoAuto(e);
                    break;
                case 2:
                    gestionarFinDeEstacionamiento(e);
                    break;
                case 3:
                    gestionarFinDeCobro(e);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Error al obtener evento en el switch");
            }



            llenarTablas(jlHoraProximaLlegada,jTablePlaya,jTableZonaDeCobro);
            return new DefaultTableModel(datosParaTabla, columnas);
        }
         else
        {
            return new DefaultTableModel(datosParaTabla, columnas);
        }
        

    }
}
