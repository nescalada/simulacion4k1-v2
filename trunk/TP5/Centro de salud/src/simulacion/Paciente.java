/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulacion;

/**
 *
 * @author Admin
 */
public class Paciente {

    public static int nroPersona = 0;
    private int nro;
    private double horaLlegada;
    private double horaInicioEstado;
    private double horaSalida;
    private int tipo;
    public static final int CON_OBRA_SOCIAL = 1;
    public static final int SIN_OBRA_SOCIAL = 2;
    private boolean pagoCooperadora = false;
    private int estado;
    public static final int EN_COLA_MESA_TURNOS = 10;
    public static final int EN_ATENCION_MESA_TURNOS = 15;
    public static final int EN_COLA_COOPERADORA = 20;
    public static final int EN_ATENCION_COOPERADORA = 25;
    public static final int RETIRADO = 30;

    public Paciente(double hora, int t) {
        nro = nroPersona;
        nroPersona = nroPersona + 3;
        horaLlegada = hora;
        tipo = t;
    }

    public double getHoraLlegada() {
        return horaLlegada;
    }

    public void setEstado(int e) {
        estado = e;
    }

    public String getTipoCliente() {
        if (tipo == SIN_OBRA_SOCIAL) {
            return "Sin obra social";
        } else {
            return "Con obra social";
        }

    }

    public int getNro() {
        return nro;
    }

    public String getEstadoString() {
        switch (estado) {
            case EN_COLA_MESA_TURNOS:
                return "En cola de la mesa de turnos";
            case EN_COLA_COOPERADORA:
                return "En cola de la cooperadora";
            case RETIRADO:
                return "Retirado";
            case EN_ATENCION_COOPERADORA:
                return "En atención en cooperadora";
            case EN_ATENCION_MESA_TURNOS:
                return "En atención en la mesa de turnos";
        }
        return "-";

    }

    public int getTipo() {
        return tipo;
    }

    public int getEstado() {
        return estado;
    }

    public static void setFirstId(int i) {
        nroPersona = i;
    }

    /**
     * @return the horaInicioAtencion
     */
    public double getHoraInicioEstadoNumero() {
        return horaInicioEstado;
    }

    /**
     * @return the horaInicioAtencion as a String
     */
    public String getHoraInicioEstado() {
        return String.valueOf(horaInicioEstado);
    }

    /**
     * @param horaInicioAtencion the horaInicioAtencion to set
     */
    public void setHoraInicioEstado(double horaInicioAtencion) {
        this.horaInicioEstado = horaInicioAtencion;
    }

    /**
     * @return the horaFinAtencion
     */
    public double getHoraSalida() {
        return horaSalida;
    }

    /**
     * @param horaFinAtencion the horaFinAtencion to set
     */
    public void setHoraSalida(double horaFinAtencion) {
        this.horaSalida = horaFinAtencion;
    }

    /**
     * @return the pagoCooperadora
     */
    public boolean isPagoCooperadora() {
        return pagoCooperadora;
    }

    /**
     * @param pagoCooperadora the pagoCooperadora to set
     */
    public void setPagoCooperadora(boolean pagoCooperadora) {
        this.pagoCooperadora = pagoCooperadora;
    }
}
