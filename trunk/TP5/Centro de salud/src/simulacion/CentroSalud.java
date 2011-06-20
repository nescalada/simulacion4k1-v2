/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulacion;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeSet;

/**
 *
 * @author Admin
 */
public class CentroSalud {

    //Variables actualizables en cada itereación:
    private String nombreEvento = "Inicio";
    private Paciente pacienteEnAtencionMesaTurno;
    private Paciente pacienteEnAtencionCooperadora;
    //La llamada en atención no se guarda porque no se necesita calcular ningún dato a partir de ella
    //Listas
    private ArrayList vectorActual; //El estado del vector en la iteración actual
    private ArrayList<ArrayList> vectorEstado; //La "tabla" de todos los vectores 
    private ArrayList<String> pacientesActuales; //El estado de los clientes en la iteración actual
    private ArrayList<ArrayList> vectorPacientes; //La "tabla" de todos los clientes
    private TreeSet<Evento> eventos;
    //Relojes_
    private int nroEvento = 0;
    private double horaActual = 0;
    private double proximaLlamadaTelefono = 0;
    private double proximaLlegadaPaciente = 0;
    private double proximoFinAtencionMesaTurno = 0;
    private double proximoFinAtencionCooperadora = 0;
    private double proximoFinLlamada = 0;
    //Colas:
    private LinkedList<Paciente> colaMesaTurno;
    private LinkedList<Paciente> colaCooperadora;
    //Empleados que atienden:
    private boolean empleadoMesaTurnoLibre = true;
    private boolean empleadoCooperadoraLibre = true;
    private boolean lineaTelefonicaLibre = true;
    //Contadores y acumuladores:
    private double sumatoriaTiempoEnCola;
    private int cantidadPacientesIngresados = 0;
    private int estadoTelefono; // 0 libre, 1 sonando, -1 atendido
    private int cantidadLlamadasPerdidas = 0;
    //Parámetros configurables?
    private double cantidadMinutosSimulacion;
    private double intInferiorMesaTurno;
    private double intSuperiorMesaTurno;
    private double intInferiorCooperadora;
    private double intSuperiorCooperadora;
    private double intInferiorDuracionLlamada;
    private double intSupereriorDuracionLlamada;
    private double tiempoEntreLlamadas;
    private double mediaArriboPacientes;
    private double tiempoAtencionParaPacientesSinObraSocial;
    //Auxiliares
    private Object[][] matrizEstado; //La tabla completa de solución
    private Object[][] matrizPacientes; //La tabla copleta de los clientes

    public CentroSalud(double cantidad) {
        cantidadMinutosSimulacion = cantidad;
        intInferiorDuracionLlamada = 0.5;
        intSupereriorDuracionLlamada = 1.5;
        intInferiorMesaTurno = 1;
        intSuperiorMesaTurno = 3;
        intInferiorCooperadora = 0.8;
        intSuperiorCooperadora = 2.4;
        tiempoEntreLlamadas = 3;
        tiempoAtencionParaPacientesSinObraSocial = (10d / 60d); //son 10 segundos, y todo se mide en minutos
        vectorEstado = new ArrayList<ArrayList>();
        pacientesActuales = new ArrayList<String>();
        vectorPacientes = new ArrayList<ArrayList>();
        colaMesaTurno = new LinkedList<Paciente>();
        colaCooperadora = new LinkedList<Paciente>();
        eventos = new TreeSet<Evento>();
        mediaArriboPacientes = 3;

        Paciente.setFirstId(0); // Seteamos los identificadores de las personas a 0.
        cargarEventoInicial();
    }

    public void simular() {
        while (horaActual < cantidadMinutosSimulacion) {
            if (eventos.isEmpty()) {
                break;
            } else {
                Evento eventoActual = eventos.first();
                eventos.remove(eventoActual);
                cargarNuevoEvento(eventoActual);
            }
        }
        armarMatrizEstado();
    }

    /**
     * Describe el comportamiento del Empleado de la Mesa de Turno con respecto a la cola de pacientes
     */
    private void comportamientoMesaTurnoConColaPacientes() {
        if (colaMesaTurno.isEmpty()) {
            empleadoMesaTurnoLibre = true;
            pacienteEnAtencionMesaTurno = null;
        } else {
            pacienteEnAtencionMesaTurno = colaMesaTurno.getFirst();
            colaMesaTurno.remove(pacienteEnAtencionMesaTurno);
            atenderPacienteMesaTurno();
            actualizarDatosPaciente(pacienteEnAtencionMesaTurno);
        }
    }

    /**
     * Guarda la fila actual del vector, para poder verla luego como matriz.
     */
    private void guardarVector() {
        vectorActual = new ArrayList();
        vectorActual.add(nroEvento);
        vectorActual.add(horaActual);
        vectorActual.add(nombreEvento);
        vectorActual.add(proximaLlegadaPaciente);
        vectorActual.add(proximaLlamadaTelefono);
        vectorActual.add(proximoFinAtencionMesaTurno);
        vectorActual.add(proximoFinAtencionCooperadora);
        vectorActual.add(proximoFinLlamada);
        vectorActual.add(isEmpleadoLibre(empleadoMesaTurnoLibre));
        vectorActual.add(colaMesaTurno.size());
        vectorActual.add(isEmpleadoLibre(empleadoCooperadoraLibre));
        vectorActual.add(colaCooperadora.size());
        vectorActual.add(sumatoriaTiempoEnCola);
        vectorActual.add(cantidadPacientesIngresados);
        vectorActual.add(isEmpleadoLibre(lineaTelefonicaLibre));
        vectorActual.add(estadoTelefono(estadoTelefono));
        vectorActual.add(cantidadLlamadasPerdidas);
        vectorEstado.add(vectorActual);
        vectorPacientes.add(new ArrayList(pacientesActuales));
    }

    /**
     * Traduce a String el estado de algun empleado
     * @param empleado El empleado del que se quiere averiguar el estado
     * @return una String con el estado
     */
    private String isEmpleadoLibre(boolean empleado) {
        if (empleado) {
            return "Libre";
        } else {
            return "Ocupado";
        }

    }

    /**
     * Crea un nuevo evento de llegada de paciente, de acuerdo a los parámetros dados por el enunciado.
     */
    private void crearNuevoEventoLlegadaPaciente() {
        proximaLlegadaPaciente = horaActual + generarValorDistribucionExponencial(1 / mediaArriboPacientes);
        eventos.add(new Evento(Evento.LLEGADA_PACIENTE, proximaLlegadaPaciente));
    }

    private void crearNuevoEventoNuevaLlamada() {
        proximaLlamadaTelefono = horaActual + tiempoEntreLlamadas;
        eventos.add(new Evento(Evento.LLAMADA_ENTRANTE, proximaLlamadaTelefono));
    }

    private void crearNuevoEventoFinLlamada() {
        proximoFinLlamada = horaActual + (intInferiorDuracionLlamada + (Math.random() * (intSupereriorDuracionLlamada - intInferiorDuracionLlamada)));
        eventos.add(new Evento(Evento.FIN_LLAMADA, proximoFinLlamada));
    }

    private void crearNuevoEventoFinAtencionMesaTurnos() {
        proximoFinAtencionMesaTurno = horaActual + (intInferiorMesaTurno + (Math.random() * (intSuperiorMesaTurno - intInferiorMesaTurno)));
        eventos.add(new Evento(Evento.FIN_ATENCION_MESA_TURNOS, proximoFinAtencionMesaTurno));
    }

    private void crearNuevoEventoFinAtencionCooperadora() {
        proximoFinAtencionCooperadora = horaActual + (intInferiorCooperadora + (Math.random() * (intSuperiorCooperadora - intInferiorCooperadora)));
        eventos.add(new Evento(Evento.FIN_ATENCION_COOPERADORA, proximoFinAtencionCooperadora));
    }

    /**
     * Carga el evento inicial de acuerdo al enunciado;
     */
    private void cargarEventoInicial() {
        crearNuevoEventoLlegadaPaciente();
        pacientesActuales.add(0, String.valueOf(nroEvento));
        pacientesActuales.add(1, String.valueOf(horaActual));
        pacientesActuales.add(2, nombreEvento);

        // 4 pacientes en cola de la mesa de turno       
        Paciente p = new Paciente(horaActual, determinarTipoPaciente());
        colaMesaTurno.add(p);
        p.setEstado(Paciente.EN_COLA_MESA_TURNOS);
        p.setHoraInicioEstado(horaActual);
        agregarDatosPaciente(p);

        Paciente p2 = new Paciente(horaActual, determinarTipoPaciente());
        colaMesaTurno.add(p2);
        agregarDatosPaciente(p2);
        p2.setEstado(Paciente.EN_COLA_MESA_TURNOS);
        p2.setHoraInicioEstado(horaActual);

        Paciente p3 = new Paciente(horaActual, determinarTipoPaciente());
        colaMesaTurno.add(p3);
        agregarDatosPaciente(p3);
        p3.setEstado(Paciente.EN_COLA_MESA_TURNOS);
        p3.setHoraInicioEstado(horaActual);

        Paciente p4 = new Paciente(horaActual, determinarTipoPaciente());
        colaMesaTurno.add(p4);
        agregarDatosPaciente(p4);
        p4.setEstado(Paciente.EN_COLA_MESA_TURNOS);
        p4.setHoraInicioEstado(horaActual);

        // 2 pacientes en cola de la cooperadora
        Paciente p5 = new Paciente(horaActual, Paciente.SIN_OBRA_SOCIAL);
        colaCooperadora.add(p5);
        p5.setEstado(Paciente.EN_COLA_COOPERADORA);
        p5.setHoraInicioEstado(horaActual);
        agregarDatosPaciente(p5);
        Paciente p6 = new Paciente(horaActual, Paciente.SIN_OBRA_SOCIAL);
        colaCooperadora.add(p6);
        p6.setEstado(Paciente.EN_COLA_COOPERADORA);
        p6.setHoraInicioEstado(horaActual);
        agregarDatosPaciente(p6);

        // Faltan dos minutos para la siguiente llamada        
        proximaLlamadaTelefono = horaActual + 2;
        eventos.add(new Evento(Evento.LLAMADA_ENTRANTE, proximaLlamadaTelefono));

        pacienteEnAtencionMesaTurno = colaMesaTurno.getFirst();
        colaMesaTurno.remove(pacienteEnAtencionMesaTurno);
        atenderPacienteMesaTurno();
        actualizarDatosPaciente(pacienteEnAtencionMesaTurno);
        pacienteEnAtencionCooperadora = colaCooperadora.getFirst();
        colaCooperadora.remove(pacienteEnAtencionCooperadora);
        atenderPacienteCooperadora();
        actualizarDatosPaciente(pacienteEnAtencionCooperadora);

        guardarVector();


    }

    /**
     * Determina qué tipo de paciente es: Sin obra social o Con obra social
     * @return el tipo de paciente.
     */
    private int determinarTipoPaciente() {
        double random = Math.random() * 100;
        if (random <= 44) {
            return Paciente.SIN_OBRA_SOCIAL;
        } else {
            return Paciente.CON_OBRA_SOCIAL;
        }
    }

    /**
     * Genera un valor aleatorio con distribución exponencial negativa
     * @param lambda El lambda de la exponencial
     * @return un número aleatorio
     */
    public double generarValorDistribucionExponencial(double lambda) {
        double random = Math.random();
        double numero = -1 * Math.log(random) / lambda;
        return numero;
    }

    /**
     * El evento actual
     * @param evento 
     */
    private void cargarNuevoEvento(Evento evento) {
        nroEvento++;
        horaActual = evento.getHoraEvento();
        nombreEvento = evento.getNombre();
        pacientesActuales.set(0, String.valueOf(nroEvento));
        pacientesActuales.set(1, String.valueOf(horaActual));
        pacientesActuales.set(2, nombreEvento);

        switch (evento.getTipoEvento()) {
            case Evento.LLAMADA_ENTRANTE: {
                proximaLlamadaTelefono = 0;
                llamadaEntrante();
                break;
            }
            case Evento.LLEGADA_PACIENTE: {
                proximaLlegadaPaciente = 0;
                llegadaPaciente();
                break;
            }
            case Evento.FIN_ATENCION_MESA_TURNOS: {
                proximoFinAtencionMesaTurno = 0;
                finAtencionMesaTurno();
                break;
            }
            case Evento.FIN_ATENCION_COOPERADORA: {
                proximoFinAtencionCooperadora = 0;
                finAtencionCooperadora();
                break;
            }
            case Evento.FIN_LLAMADA: {
                proximoFinLlamada = 0;
                finLlamada();
                break;
            }
        }
        guardarVector();
    }

    private void llegadaPaciente() {
        Paciente paciente = new Paciente(horaActual, determinarTipoPaciente()); //Creamos la persona que llega     
        cantidadPacientesIngresados++;
        crearNuevoEventoLlegadaPaciente(); // Como llegó alguién, va a haber un evento de proxima llegada
        if (empleadoMesaTurnoLibre) {
            pacienteEnAtencionMesaTurno = paciente;
            atenderPacienteMesaTurno();
        } else {
            ponerEnColaMesaTurno(paciente);
        }
        agregarDatosPaciente(paciente);
    }

    private void llamadaEntrante() {
        crearNuevoEventoNuevaLlamada(); // Como entra una llamada, se crea la próxima llamada
        if (lineaTelefonicaLibre) {
            lineaTelefonicaLibre = false; //La línea dará ocupado
            if (empleadoMesaTurnoLibre) { //El teléfono lo atiende el de la mesa de turnos
                atenderLlamada();
            } else {
                ponerLlamadaEnEspera();
            }
        } else {
            cantidadLlamadasPerdidas++;
        }
    }

    private void finAtencionMesaTurno() {
        //Qué tipo de cliente es?       
        if (pacienteEnAtencionMesaTurno.getTipo() == Paciente.SIN_OBRA_SOCIAL && !pacienteEnAtencionMesaTurno.isPagoCooperadora()) {
            //Acá llega ese que estuvo esperando 10 minutos a que le digan que tiene que ir a pagar la cooperadora
            if (empleadoCooperadoraLibre) { 
                pacienteEnAtencionCooperadora = pacienteEnAtencionMesaTurno;
                atenderPacienteCooperadora();
            } else {
                ponerEnColaCooperadora(pacienteEnAtencionMesaTurno);
            }
            actualizarDatosPaciente(pacienteEnAtencionMesaTurno);
        } else {
            //Ya le dimos el turno al paciente, que se vaya ahora y no moleste más.
            pacienteEnAtencionMesaTurno.setHoraSalida(horaActual);
            pacienteEnAtencionMesaTurno.setEstado(Paciente.RETIRADO);
            actualizarDatosPaciente(pacienteEnAtencionMesaTurno);
        }

        // Las llamadas tienen prioridad
        switch (estadoTelefono) {
            case 0: {
                // 0 significa: No está sonando el phono, el empleado pasa a atender a la cola de pacientes
                comportamientoMesaTurnoConColaPacientes();
                break;
            }
            case 1: {
                //1 significa que el teléfono está sonando, debe atenderse
                atenderLlamada();
                break;
            }
            case -1: {
                //-1 no deberia dar nunca, puesto que significa que actualmente se está atendiendo una llamada
                break;
            }
        }
    }

    private void finAtencionCooperadora() {
        pacienteEnAtencionCooperadora.setPagoCooperadora(true); //Ya se pagó la cooperadora
        if (empleadoMesaTurnoLibre) { //Lo mandamos de vuelta a la mesa de turno
            pacienteEnAtencionMesaTurno = pacienteEnAtencionCooperadora;
            atenderPacienteMesaTurno();
        } else {
            pacienteEnAtencionCooperadora.setEstado(Paciente.EN_COLA_MESA_TURNOS);
            colaMesaTurno.addFirst(pacienteEnAtencionCooperadora); //Recordemos que tiene priodidad en la fila
        }
        if (colaCooperadora.isEmpty()) {
            empleadoCooperadoraLibre = true;
            pacienteEnAtencionCooperadora = null;
        } else {
            pacienteEnAtencionCooperadora = colaCooperadora.getFirst();
            colaCooperadora.remove(pacienteEnAtencionCooperadora);
            atenderPacienteCooperadora();
            actualizarDatosPaciente(pacienteEnAtencionCooperadora);
        }
    }

    private void finLlamada() {
        lineaTelefonicaLibre = true; //Evidentemente la línea ya no está ocupada
        estadoTelefono = 0; //El telefono no suena
        comportamientoMesaTurnoConColaPacientes(); //Asumimos que no va a empezar a sonar en el exacto mismo momento, y no hay colas de llamadas, así que vamos a la cola de pacientes
    }

    private void atenderPacienteCooperadora() {
        empleadoCooperadoraLibre = false;
        sumatoriaTiempoEnCola += horaActual - pacienteEnAtencionCooperadora.getHoraInicioEstadoNumero();
        pacienteEnAtencionCooperadora.setEstado(Paciente.EN_ATENCION_COOPERADORA);
        pacienteEnAtencionCooperadora.setHoraInicioEstado(horaActual);
        crearNuevoEventoFinAtencionCooperadora();

    }
    /**
     * Convierte a String el estado del teléfono
     * @param estado El estado del phono
     * @return Una string
     */
    private String estadoTelefono(int estado) {
        switch (estado) {
            case 1:
                return "Sonando";
            case -1:
                return "En atención";
            default:
                return "Normal";
        }
    }

    private void atenderPacienteMesaTurno() {
        empleadoMesaTurnoLibre = false;
        sumatoriaTiempoEnCola += horaActual - pacienteEnAtencionMesaTurno.getHoraInicioEstadoNumero();
        pacienteEnAtencionMesaTurno.setEstado(Paciente.EN_ATENCION_MESA_TURNOS);
        pacienteEnAtencionMesaTurno.setHoraInicioEstado(horaActual);
        if (pacienteEnAtencionMesaTurno.getTipo() == Paciente.SIN_OBRA_SOCIAL && !pacienteEnAtencionMesaTurno.isPagoCooperadora()) {
            proximoFinAtencionMesaTurno = horaActual + tiempoAtencionParaPacientesSinObraSocial;
            eventos.add(new Evento(Evento.FIN_ATENCION_MESA_TURNOS, proximoFinAtencionMesaTurno));
        } else {
            crearNuevoEventoFinAtencionMesaTurnos();
        }
    }

    private void atenderLlamada() {
        lineaTelefonicaLibre = false;
        empleadoMesaTurnoLibre = false;
        estadoTelefono = -1;
        crearNuevoEventoFinLlamada();
    }

    private void ponerEnColaMesaTurno(Paciente persona) {
        persona.setEstado(Paciente.EN_COLA_MESA_TURNOS);
        persona.setHoraInicioEstado(horaActual);
        colaMesaTurno.add(persona);

    }

    private void ponerEnColaCooperadora(Paciente persona) {
        persona.setEstado(Paciente.EN_COLA_COOPERADORA);
        persona.setHoraInicioEstado(horaActual);
        colaCooperadora.add(persona);
    }

    private void ponerLlamadaEnEspera() {
        estadoTelefono = 1;
    }

    private void agregarDatosPaciente(Paciente persona) {
        pacientesActuales.add(3 + persona.getNro(), persona.getTipoCliente());
        pacientesActuales.add(3 + persona.getNro() + 1, persona.getEstadoString());
        pacientesActuales.add(3 + persona.getNro() + 2, persona.getHoraInicioEstado());

    }

    private void actualizarDatosPaciente(Paciente persona) {
        pacientesActuales.set(3 + persona.getNro() + 1, persona.getEstadoString());
        pacientesActuales.set(3 + persona.getNro() + 2, persona.getHoraInicioEstado());
    }

    public Object[][] getMatrizPacientes() {
        armarMatrizPacientes();
        return matrizPacientes;
    }

    public Object[][] getMatrizEstado() {
        return matrizEstado;
    }

    private void armarMatrizEstado() {
        matrizEstado = new Object[vectorEstado.size()][17];
        for (int i = 0; i < matrizEstado.length; i++) {
            matrizEstado[i] = vectorEstado.get(i).toArray();
        }
    }

    private void armarMatrizPacientes() {
        matrizPacientes = new Object[vectorPacientes.size()][vectorPacientes.get(vectorPacientes.size() - 1).size()];
        for (int i = 0; i < matrizPacientes.length; i++) {
            matrizPacientes[i] = vectorPacientes.get(i).toArray();
        }

    }

    public String[] getNombresColumnas() {
        String[] nombres = {"Evento #", "Hora", "Tipo de Evento", "Próxima Llegada Paciente",
            "Próximo Llamada de Teléfono", "Próximo Fin Atencion Mesa Turno", "Próximo Fin Atención Cooperadora", "Próximo Fin Llamada",
            "Empleado Mesa Turno", "Cola Mesa Turno", "Empleado Cooperadora", "Cola Cooperadora",
            "Σ Tiempo en cola", "Σ Pacientes ingresados", "Linea Telefónica", "Estado de teléfono", "Σ Llamadas Perdidas"};
        return nombres;
    }

    public String[] getNombresColumnasClientes() {

        String[] nombres = new String[matrizPacientes[matrizPacientes.length - 1].length];
        nombres[0] = "Evento #";
        nombres[1] = "Hora";
        nombres[2] = "Tipo de Evento";

        int i = 3;
        int c = 1;
        while (i < nombres.length) {

            nombres[i] = "Paciente # " + c + " - Tipo";
            nombres[i + 1] = "Estado";
            nombres[i + 2] = "Hora de Inicio Estado";
            c++;
            i = i + 3;
        }
        return nombres;
    }

    public Object[][] obtenerNuevoEvento() {
        if (!eventos.isEmpty()) {
            Evento eventoActual = eventos.first();
            eventos.remove(eventoActual);
            cargarNuevoEvento(eventoActual);
            armarMatrizEstado();
        }
        return getMatrizEstado();
    }

    public double getCantidadLlamadasPerdidas() {
        return cantidadLlamadasPerdidas;
    }

    public double getTiempoPromedioEspera() {
        return sumatoriaTiempoEnCola / (double) cantidadPacientesIngresados;
    }
}
