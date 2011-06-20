/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulacion;

/**
 *
 * @author Admin
 */
public class Evento implements Comparable {

    private int tipoEvento;
    private double horaEvento;
    public static final int LLEGADA_PACIENTE = 0;
    public static final int LLAMADA_ENTRANTE = 1;
    public static final int FIN_ATENCION_MESA_TURNOS = 2;
    public static final int FIN_ATENCION_COOPERADORA = 3;
    public static final int FIN_LLAMADA = 4;

    public Evento(int t, double h) {
        tipoEvento = t;
        horaEvento = h;
    }

    public int compareTo(Object o) {
        Evento e = (Evento) o;
        if (this.horaEvento - e.horaEvento < 0) {
            return -1;
        } else if (this.horaEvento - e.horaEvento == 0) {
            return 0;
        } else {
            return 2;
        }

    }

    public String getNombre() {
        switch (tipoEvento) {
            case LLAMADA_ENTRANTE:
                return "Llamada de teléfono entrante";
            case LLEGADA_PACIENTE:
                return "Llegada de paciente";
            case FIN_ATENCION_MESA_TURNOS:
                return "Fin de atención en la mesa de turno";
            case FIN_ATENCION_COOPERADORA:
                return "Fin de atención en la cooperadora";
            case FIN_LLAMADA:
                return "Fin de la llamada";
        }
        return "-";

    }

    /**
     * @return the tipoEvento
     */
    public int getTipoEvento() {
        return tipoEvento;
    }

    /**
     * @param tipoEvento the tipoEvento to set
     */
    public void setTipoEvento(int tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    /**
     * @return the horaEvento
     */
    public double getHoraEvento() {
        return horaEvento;
    }

    /**
     * @param horaEvento the horaEvento to set
     */
    public void setHoraEvento(double horaEvento) {
        this.horaEvento = horaEvento;
    }
}
