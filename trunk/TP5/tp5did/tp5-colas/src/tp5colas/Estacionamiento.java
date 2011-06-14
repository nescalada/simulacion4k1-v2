package tp5colas;


public class Estacionamiento {

    private boolean estado = false; //False = Ocupado - true = Libre;
    private Auto auto = null;

    public Estacionamiento() {
        estado = true;
    }

    public boolean estaLibre() {
        return estado;
    }

    public Auto getAuto() {
        return auto;
    }

    public void agregarAuto(Auto auto) {
        this.auto = auto;
        cambiarEstado();
    }

    public void sacarAuto() {
        this.auto = null;
        cambiarEstado();
    }

    public void cambiarEstado() {
        if (estado) {
            estado = false;
        } else {
            estado = true;
        }
    }
}
