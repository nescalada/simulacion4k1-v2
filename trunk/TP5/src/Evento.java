
public class Evento implements Comparable<Evento> {

    private String descripcion;
    private double horaEvento;
    private int tipoEvento;
    private Estacionamiento estacionamiento;
    private Auto auto;

    public Evento(int tipoDeEvento, double hora, Estacionamiento estacionamiento, Auto auto) {
        this.tipoEvento = tipoDeEvento;
        this.horaEvento = hora;
        switch (tipoDeEvento) {
            case 1:
                descripcion = "Llegada de ";
                this.estacionamiento = null;
                this.auto = null;
                break;
            case 2:
                descripcion = "Fin de Estacionamiento de " + estacionamiento.getAuto().getNombre();
                this.estacionamiento = estacionamiento;
                this.auto = null;
                break;
            case 3:
                descripcion = "Fin de Cobro de " + auto.getNombre();
                this.estacionamiento = null;
                this.auto = auto;
                break;
            default:
                descripcion = "ERROR de asignacion";
        }
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getHoraEvento() {
        return horaEvento;
    }

    public int getIntTipoEvento() {
        return tipoEvento;
    }

    public int compareTo(Evento o) {
        if (horaEvento < o.getHoraEvento()) {
            return -1;
        }
        if (horaEvento == o.getHoraEvento()) {
            return 0;
        }
        return 1;
    }
}
