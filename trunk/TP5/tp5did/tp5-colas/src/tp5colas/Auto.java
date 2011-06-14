package tp5colas;


public class Auto {

    private String nombre, tipoDeAuto;
    private int intTipoDeAuto = 0;
    private double horaEntrada, tiempoDeEstacionamiento, horaSalida;

    public Auto(String nombre, double rndTipoAuto, double horaEntrada, double rndTiempo) {
        this.nombre = nombre;
        this.tipoDeAuto = getTipo(rndTipoAuto);
        this.horaEntrada = horaEntrada;
        this.tiempoDeEstacionamiento = getTiempo(rndTiempo);
        this.horaSalida = this.horaEntrada + this.tiempoDeEstacionamiento;
        this.horaSalida = arreglarNumero(this.horaSalida);
    }

    private String getTipo(double rndTipo) {
        if (rndTipo < 0.4) {
            intTipoDeAuto = 1;
            return "Auto Pequeño";
        }
        if (rndTipo < 0.7) {
            intTipoDeAuto = 2;
            return "Auto Grande";
        }
        intTipoDeAuto = 3;
        return "Utilitario";
    }

    private int getTiempo(double rndTiempo) {
        if (rndTiempo < 0.5) {
            return 60;
        }
        if (rndTiempo < 0.8) {
            return 120;
        }
        if (rndTiempo < 0.95) {
            return 180;
        }
        return 240;
    }

    public double getHoraEntrada() {
        return horaEntrada;
    }

    public double getHoraSalida() {
        return horaSalida;
    }

    public String getNombre() {
        return nombre;
    }

    public double getTiempoDeEstacionamiento() {
        return tiempoDeEstacionamiento;
    }

    public int getIntTipoDeAuto() {
        return intTipoDeAuto;
    }

    public String getTipoDeAuto() {
        return tipoDeAuto;
    }

    private double arreglarNumero(double numero) {
        numero *= 100;
        numero = Math.round(numero);
        numero /= 100;
        return numero;
    }
}
