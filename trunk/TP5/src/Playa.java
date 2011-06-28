
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class Playa {

    private Estacionamiento[] playa;
    private boolean estado = false; //False = Ocupado - true = Libre;

    public Playa() {
        playa = new Estacionamiento[20];
        for (int i = 0; i < 20; i++) {
            playa[i] = new Estacionamiento();
        }

        estado = true;
    }

    private void actualizarEstado() {
        for (int i = 0; i < playa.length; i++) {
            if (playa[i].estaLibre()) {
                estado = true;
                return;
            }
        }
        estado = false;
    }

    public boolean estaLibre() {
        actualizarEstado();
        return estado;
    }

    public int tamañoPlaya() {
        return playa.length;
    }

    public void agregarAuto(Auto auto) {
        for (int i = 0; i < playa.length; i++) {
            if (playa[i].estaLibre()) {
                playa[i].agregarAuto(auto);
                return;
            }
        }
    }

    public void agregarEstacionamiento(Estacionamiento esta) {
        for (int i = 0; i < playa.length; i++) {
            if (playa[i].estaLibre()) {
                playa[i] = esta;
                return;
            }
        }
    }

    public void eliminarEstacionamiento(Estacionamiento esta) {
        for (int i = 0; i < playa.length; i++) {
            if (playa[i].equals(esta)) {
                playa[i] = new Estacionamiento();
            }
        }
    }

    public TableModel getTableModel() {
        String[] col = {"Estacionamiento", "Auto", "Tipo De Auto",
            "Tiempo de Estacioamiento", "Hora De Salida"};
        String[][] dat = new String[20][5];

        for (int i = 0; i < playa.length; i++) {
            Estacionamiento esta = playa[i];
            dat[i][0] = String.valueOf(i + 1);
            if (esta.getAuto() != null) {
                dat[i][1] = esta.getAuto().getNombre();
                dat[i][2] = String.valueOf(esta.getAuto().getTipoDeAuto());
                dat[i][3] = String.valueOf(esta.getAuto().getTiempoDeEstacionamiento());
                dat[i][4] = String.valueOf(esta.getAuto().getHoraSalida());
            } else {
                dat[i][1] = "";
                dat[i][2] = "";
                dat[i][3] = "";
                dat[i][4] = "";
            }
        }
        return new DefaultTableModel(dat, col);
    }
}
