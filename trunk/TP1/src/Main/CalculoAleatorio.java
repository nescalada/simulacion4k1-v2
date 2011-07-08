/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Main;

/**
 *
 * @author matlock
 */
public class CalculoAleatorio
{

    public double seed; //el valor de la semilla
    public double preSeed; // el valor de la semilla anterior, para el método mixto
    public int a; // el valor de a, multiplicador 0<a<m
    public double c; // el valor de c, incremento
    public long m = (long) Math.pow(2d, 48d); // el valor de m (2 a la 48)
    public int eligio;

    public void empezar()
    {
        System.out.println("bienvenido, elija una opcion: \n1-generar 20 numeros aleatorios (mixto o multiplicativo) \n2-prueba de frecuencia (random o mixto),\n9-salir");
        int opcion=Consola.readInt();
        if (opcion==9)
        {
            System.out.println("gracias por usar nuestro generador de numeros aleatorios!");
            Consola.readLine();
            System.exit(0);
        }
        else   
        {
            if (opcion==1)
            {
                puntoa();
            }
            else
            {
                if (opcion==2)
                {
                    puntob();
                }
                else
                {
                    System.out.println("ingrese un numero valido!");
                    empezar();
                }
            }

        }
    }
    public void puntob() //la prueba de frecuencia
    {
       System.out.println("prueba de frecuencia (usando generador de la maquina):\nmetodo random 1,\nmetodo mixto 2,\nmenu anterior 9");
       eligio=Consola.readInt();
       if (eligio==9)
       {
           empezar();
       }
       else
       {
           calcularParaGraficar();
       }
    }

    public void calcularParaGraficar()
    {

           System.out.println("cantidad de numeros");
           int cantidad=Consola.readInt();
           System.out.println("cantidad de intervalos");
           int intervalos=Consola.readInt();


//           int[] arrayCantidad = new int[intervalos];
//           double[] arrayIntervalos = new double[cantidad];
           int[] arrayCantidad = new int[intervalos];
           double[] arrayIntervalos = new double[intervalos];


           //con esto divido los intervalos...
           for (int i = 0; i < arrayIntervalos.length; i++)
           {
               arrayIntervalos[i] = (double) i / intervalos;
           }

           if (eligio==1) //random
           {
               contarCantidades(cantidad,arrayIntervalos,arrayCantidad,true);
               mostrarVentana(arrayIntervalos,arrayCantidad);
           }
           else
           {
               if (eligio==2) //mixto
               {
                   generarNumero();//crea el seed y el preced, para calcular bien... el m se crea al inicio y no lo toco...
                   contarCantidades(cantidad,arrayIntervalos,arrayCantidad,false);
                   mostrarVentana(arrayIntervalos,arrayCantidad);
               }
               else
               {
                   System.out.println("ingrese un numeros validos!"); // si no ingreso ni 1 ni 2... no compruebo los otros valores....
                   puntob();
               }
           }


    }
    public void mostrarVentana(double []arrayIntervalos,int[] arrayCantidad)
    {
        GeneradorGrafico grafico = new GeneradorGrafico(arrayIntervalos, arrayCantidad);
        grafico.setSize(1024, 768); //resolucion pantalla
        grafico.setVisible(true);
        
        empezar();//esto es para cuando se cierra la pantalla...(deberia andar bien...)

    }

    public void contarCantidades(int cantidadNumeros, double[] arrayIntervalos, int[] arrayCantidad, boolean b)
    {
        double numeroAleatorio = 0;
        for (int i = 0; i < cantidadNumeros; i++)
        {
            if (b) //random
            {
                numeroAleatorio = Math.random();
            }
            else // mixto
            {
                numeroAleatorio = generarNAMixto();
            }
            for (int j = 1; j < arrayIntervalos.length; j++)
            {

                if (numeroAleatorio <= arrayIntervalos[j] && numeroAleatorio > arrayIntervalos[j - 1])
                {
                    arrayCantidad[j - 1]++;
                    break;
                }
            }
            if (numeroAleatorio > arrayIntervalos[arrayIntervalos.length - 1])
            {
                arrayCantidad[arrayCantidad.length - 1]++;
            }
        }
    }


     public void generarNumero()
     {
        if (seed == 0)
        {
            seed = (long) (Math.random() * 1000000000);
        }
        if (preSeed == 0)
        {
            preSeed = (long) (Math.random() * 1000000000);
        }

        this.seed = seed;
        this.preSeed = preSeed;

    }


    public void puntoa() //20 numeros aleatorios con opcion de uno mas...
    {
       System.out.println("metodo mixto 1, metodo multiplicativo 2, menu anterior 9");
       eligio=Consola.readInt();
        if (eligio==9)
        {
            empezar();
        }
        else
        {
           System.out.println("ingrese la semilla");
           seed=Consola.readInt();
           System.out.println("ingrese el valor de a");
           a=Consola.readInt();

            if (eligio ==1) //mixto
            {
                int i=0;
                for ( i= 0; i < 20; i++)
                {
                    preSeed=generarNAMixto();
                    System.out.println((i+1) + " numero : "+preSeed);
                }
                System.out.println("uno mas? (1 = si, 9 = no)");
                int mas = Consola.readInt();

                while(mas!=9)
                {
                    preSeed=generarNAMixto();
                    System.out.println((i+1) + " numero : "+preSeed);
                    System.out.println("uno mas? (1 = si, 9 = no)");
                    mas = Consola.readInt();
                }

            }
            else
            {
                if (eligio==2) //multiplicativo
                {
                    System.out.println("ingrese el valor de c");
                    c=Consola.readInt();
                    int i= 0;

                    for (i= 0; i < 20; i++)
                    {
                        seed=generarNAMultiplicativo();
                        System.out.println((i+1) + " numero : "+seed);
                    }
                    System.out.println("uno mas? (1 = si, 9 = no)");
                    int mas = Consola.readInt();

                    while(mas!=9)
                    {
                        seed=generarNAMultiplicativo();
                        System.out.println((i+1) + " numero : "+seed);
                        System.out.println("uno mas? (1 = si, 9 = no)");
                        mas = Consola.readInt();
                    }
                }
                else
                {
                    System.out.println("ingrese un numero valido!");
                    puntoa();
                }
            }
           puntoa();
        }
   

    }
   

    public double generarNAMultiplicativo()
    {

        double x =(a * seed) /( m);
        return corregirLetras(x);
        
    }

    public double generarNAMixto()
    {
        double x = (seed + preSeed) / m;
        return corregirLetras(x);
    }

    // corrigue los valores que produce... tira letras E y cualquier cosa...
    public double corregirLetras(double x)
    {
        String s = String.valueOf(x).replaceAll("E", "");
        s = s.replaceAll("-", "");
        int index = s.indexOf('.');
        String numAl = "0" + s.substring(index);
        String newSeed = s.replaceAll("\\.", "");
        preSeed = seed;
        seed = Long.parseLong(newSeed, 10);
        return Double.parseDouble(numAl);
    }




}
