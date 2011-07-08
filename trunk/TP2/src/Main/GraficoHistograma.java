package Main;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
/**
 *
 * @author user
 */
public class GraficoHistograma extends JFrame {

    private BufferedImage imagen = null;
    private double[] arrayIntervalos;
    private int[] arrayCantidad;

    public GraficoHistograma(double[] intervalos, int[] cantidad) {
        arrayCantidad = cantidad;
        arrayIntervalos = intervalos;
        this.setTitle("Grafico de Ocurrencias");
    }

    private BufferedImage crearImagen() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 1; i < arrayIntervalos.length; i++) {
            dataset.setValue(arrayCantidad[i - 1], "Ocurrencias", "" + arrayIntervalos[i - 1] + "-" + arrayIntervalos[i]);
			// se cargan los valores, el primer parametros es la cantidad, el segundo es la cantidad de que cosa,
			//y el tercero es lo que sale abajo de la barra
        }

        dataset.setValue(arrayCantidad[arrayCantidad.length - 1], "Ocurrencias", "" + arrayIntervalos[arrayCantidad.length - 1] + "-1.0");

        JFreeChart chart = ChartFactory.createBarChart("Numeros Aleatorios","Intervalos", "Cantidad", dataset, PlotOrientation.VERTICAL, false, true, false);
        BufferedImage grafico = chart.createBufferedImage(1024, 750);
        return grafico;
    }
    public void paint(java.awt.Graphics g) {
        if (imagen == null) {
            imagen = this.crearImagen();
        }


        g.drawImage(imagen, 0, 0, null);
    }
}
