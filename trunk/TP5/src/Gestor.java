
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;

public class Gestor {

    private Playa playa;
    private LinkedList<Auto> colaParaCobrar;
    private LinkedList<Evento> eventos;
    private double contadorCobro, indice, contadorNombreAuto = 0, reloj = 0;
    private int numeroEvento = 0;

    public Gestor() {
        //limpiar();
    }

    void getProximasMilHoras(double indice, int horasDeSimulacion) {
        this.indice = indice;

        if (numeroEvento == 0) {
            limpiar();
        }

        for (int i = 0; i < horasDeSimulacion; i++) {

            Collections.sort(eventos);
            Evento e = eventos.removeFirst();

            numeroEvento++;


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
        return (-indice * Math.log(1 - rndTiempoEntreLlegada));
    }

    private void limpiar() {
        numeroEvento = 0;

        reloj = 0;
        double rndProximaLlegada = Math.random();
        double horaproximaLlegada = getTiempoEntreLlegada(indice, rndProximaLlegada);

        playa = new Playa();
        eventos = new LinkedList<Evento>();
        crearEventoProximaLlegada(horaproximaLlegada);
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

        double rndTipoDeAuto = Math.random();
        double rndTiempoDeEstacionamiento = Math.random();

        Auto auto = new Auto("Auto" + contadorNombreAuto, rndTipoDeAuto, reloj, rndTiempoDeEstacionamiento);
        Estacionamiento esta = new Estacionamiento();
        esta.agregarAuto(auto);

        //Si hay lugar en la playa
        if (agregarEstacionamiento(esta)) {
            crearEventoFinEstacionamiento(auto.getHoraSalida(), esta);
        } //Si no hay lugar en la playa
        else {
            esta.getAuto().getNombre();
            esta.getAuto().getHoraEntrada();
        }


        double rndTiempoEntreLlegada = Math.random();
        double tiempoEntreLlegada = getTiempoEntreLlegada(indice, rndTiempoEntreLlegada);

        crearEventoProximaLlegada(reloj + tiempoEntreLlegada);
    }

    private void gestionarFinDeEstacionamiento() {

    }

    private void gestionarFinDeCobro() {
        
    }
}
