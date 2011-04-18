/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Montecarlo.java
 *
 * Created on 14/04/2011, 20:50:21
 */

package visual;

/**
 *
 * @author matlock
 */
public class Montecarlo extends javax.swing.JFrame
{

    /** Creates new form Montecarlo */
    public Montecarlo()
    {
        initComponents();
    }

    public int stockInicial=20; // es con lo que empieza (decenas), sea el caso que sea (A o B)
    public int numeroDeDias; //es la cantidad de dias que se va a realizar la prueba
    public int demandaAPedir; //es la cantidad que se va a pedir en cada pedido
    public int demandaXDia; // es la cantidad demandada por dia, segun la probabilidad que de
    public int demora; // es la demora del pedido, se relaciona con la probabilidad de la demora
    public int costoDecenas; // es el costo que tiene, por pedir cierta cantidad de decenas, es "fijo"

    public int km;
    public int ks;
    public int ko;

    public float  laProbabilidadDemandaXDIa[]; // es la probabilidad que me va a dar la cantidad de demanda por dia
    public float  probabilidadDemandaXDIa; //es mi probabilidad de demanda actual
    public float  laProbabilidadDemora[]; // es la probabilidad que me dice cuanto va a tardar la entrega de la demanda pedida
    public float  probabilidadDemora; // es mi probabilidad dee demora actual

    public int total; //es el costo total que voy a tener
    public int totalPorDia[];

    public int valor; // es el tamaño que va a tener mis vectores
    public void inicio() //caso A
    {
        numeroDeDias=25; //cantidad de dias que dice el ejercicio
        demandaAPedir=180; // es fija, siempre pide eso, cada 7 dias

        valor=100;
        laProbabilidadDemandaXDIa= new float [valor]; //hago un vector de 100 valores
        laProbabilidadDemora=new float [valor];

        calcularRNDDemanda(); //me calcula el random de la demanda en un dia , es un vector
        calcularRNDDemora(); //me calcula el random de la demora del pedido , es un vector

        totalPorDia=new int[numeroDeDias];
        //     verCostoDecenasPedidas(); // ve cuanto me cuestan las decenas que pido

        km=(3*10);//cuesta $3 por dia y unidad, lo paso a decenas
        ks=(4*10); //en el caso que no tenga, es lo que pierdo, esta puesto en decenas
       // ko= verCostoDecenasPedidas();//es el costo de tenerlo en stock

        empiezaMontecarlo(); //es el metodo que hace todo el calculo

       // verDemora();  // ve el tiempo que va a tardar el pedido, recibe un float (que viene del valor rnd)
   
       // verDemandaXDia(); // ve la cantidad que me van a demandar por dia, recibe un float (que viene del valor rnd)
    }

    public void empiezaMontecarlo()
    {
        int tmpTotal=0; //es el total temporal, asi lo muestro
        int miPedido=0; // es el stock que me queda, al finalizar el dia
        int miKm=0; // es el costo de lo que me sobro * el costo de tener en stock
        int miKs=0;
        int demPorDia=0; //es la cantidad que me consumen en el dia
        int demandaNueva=0; // es lo que me va a quedar de demanda
        int demoraNueva=0; //es mi dimora actual en dias
        int perdidaActual=0; // es la cantidad negativa que me perdi de vender, pero la hago positiva
        boolean calculoKsOKm=false;
        boolean calculo2=false;
        int contadorDeDias=0;

        for (int i= 0; i < numeroDeDias; i++)
        {
            System.out.println("");
            System.out.println("contador dias i= "+i);
            

            if (i!=0)
            {
                System.out.println("RND demanda = "+laProbabilidadDemandaXDIa[i-1]); //le resto uno porq en el dia cero no lo uso...
                System.out.println("RND demora= "+laProbabilidadDemora[i-1]);
                

                //bloque que me incerta el pedido, que viene en unos dias
                contadorDeDias=+1;
                System.out.println("contador dias para reposicion= "+contadorDeDias);
                if (demoraNueva==contadorDeDias)
                {
                    stockInicial=demandaAPedir;
                    System.out.println("stock con pedido= "+stockInicial);
                }

                //bloque que calcula la demanda
                demPorDia=verDemandaXDia(laProbabilidadDemandaXDIa[i-1]);
                System.out.println("demanda por dia= "+demPorDia);
                demandaNueva= stockInicial - demPorDia;
                System.out.println("demanda Nueva= "+demandaNueva);
                if (demandaNueva<0)
                {
                    perdidaActual=Math.abs (demandaNueva);
                    demandaNueva=0;
                    miKs=ks*perdidaActual;
                    calculoKsOKm=false;
                    System.out.println("perdidaActual= "+perdidaActual+" -mi ks= "+miKs);
                 //   tmpTotal=calcularTotalSoloks(miKs,i);
                }else
                {
                    miKm=km*demandaNueva;
                    System.out.println("mikm= "+miKm);
                    calculoKsOKm=true;
                }
                
                stockInicial=demandaNueva;
                System.out.println("stockInicial - nuevo = "+stockInicial);


                //bloque que calcula el pedido con su demora
                if (i==1||i==7*i) //si es multiplo de 7 hago el pedido
                {
                    System.out.println("hace pedido");
                    demoraNueva=verDemora(laProbabilidadDemora[i-1]); //dias que va a tardar...
                    System.out.println("dias que va a tardar... = "+demoraNueva);
                    ko=verCostoDecenasPedidas(demandaAPedir);
                    System.out.println("valor ko= "+ko);
                    calculo2=true;
                    contadorDeDias=0;

                }else
                {
                    calculo2=false;
                }

                if (calculoKsOKm) //sirve para ver que calculo y como...
                {
                    tmpTotal=calcularTotalConkmYko(miKm,ko,i);
                    System.out.println("calcularTotalConkmYko (1)= "+tmpTotal);
                    calculoKsOKm=false;
                }else
                {
                    if (calculo2)
                    {
                        calcularTotalConksYko(miKs,ko,i);
                        System.out.println("calcularTotalConksYko (2)= "+tmpTotal);
                    } else
                    {
                        tmpTotal=calcularTotalSoloks(miKs,i);
                        System.out.println("calcularTotalSoloks (3)= "+tmpTotal);
                    }
                    
                }

                
            }else
            {
                miPedido=stockInicial;
                miKm=km*miPedido;
                tmpTotal=calcularTotalSoloKm(miKm,i);
                System.out.println("calcularTotalSoloKm (4)= "+tmpTotal);
            }
            System.out.println("total en ese dia = "+totalPorDia[i]);


        }
        System.out.println("el total de los totales = "+tmpTotal);
    }

    public int calcularTotalSoloKm(int kM, int i)
    {    
        totalPorDia[i]=kM;
        total=total + totalPorDia[i];
        return total;
       
    }
    public int calcularTotalSoloks(int kS,int i)
    {
        totalPorDia[i]=kS;
        total=total + totalPorDia[i];
        return total;
    }

    public int calcularTotalConkmYko(int kM,int kO, int i)
    {
        totalPorDia[i]=kM + kO;
        total=total + totalPorDia[i];
        return total;
    }
    public int calcularTotalConksYko(int kS,int kO, int i)
    {
        totalPorDia[i]=kS + kO;
        total=total + totalPorDia[i];
        return total;
    }

    public void calcularRNDDemora()
    {
        for (int i = 0; i < valor; i++) // podria seer un numero menor, como numeroDeDias/7
        {
            laProbabilidadDemora[i]=(long)(Math.random()*100);
        }
    }

    public void calcularRNDDemanda()
    {
        for (int i= 0; i < valor; i++)
        {
            laProbabilidadDemandaXDIa[i] = (long) (Math.random()*100);
        }

    }

    public int verDemandaXDia(float  i)
    {
        probabilidadDemandaXDIa=i;
        if (probabilidadDemandaXDIa<=4)//0.04
        {
            demandaXDia=0;
        }else
        {
            if (probabilidadDemandaXDIa>=5 && probabilidadDemandaXDIa<=16)
            {
                demandaXDia=10;
            }else
            {
                if (probabilidadDemandaXDIa>=17 && probabilidadDemandaXDIa<=34)
                {
                    demandaXDia=20;
                }else
                {
                    if (probabilidadDemandaXDIa>=35 && probabilidadDemandaXDIa<=59)
                    {
                        demandaXDia=30;
                    }else
                    {
                        if (probabilidadDemandaXDIa>=6 && probabilidadDemandaXDIa<=81)
                        {
                            demandaXDia=40;
                        }else
                        {
                            if (probabilidadDemandaXDIa>=82 && probabilidadDemandaXDIa<=99)
                            {
                                demandaXDia=50;
                            }else
                            {
                                System.out.println("error de probabilidad demanda por dia...");
                            }
                        }
                    }
                }
            }
        }
        return demandaXDia;

    }

   public int verDemora(float  i)
   {
       probabilidadDemora=i;
       if (probabilidadDemora<=14 )//0.14
       {
           demora=1;
       }else
       {
           if (probabilidadDemora>=15 && probabilidadDemora<=34 )
           {
               demora=2;
           }else
           {
               if (probabilidadDemora>=35 && probabilidadDemora<=74 )
               {
                   demora=3;
               }else
               {
                   if (probabilidadDemora>=75 && probabilidadDemora<=99 )
                   {
                       demora=4;
                   }else
                   {
                       System.out.println("error de probabilidad demora...");
                   }

               }

           }
       }
       return demora;
   }

    public int verCostoDecenasPedidas(int cantidad)
    {
        demandaAPedir=cantidad;
        if (demandaAPedir<=100)
        {
            costoDecenas=200;
        }else
        {
            if (demandaAPedir>=101 && demandaAPedir<=200)
            {
                costoDecenas=280;
            }else
            {
                if (demandaAPedir>=201)
                    {
                        costoDecenas=300;
                    }else
                    {
                        System.out.println("error de calculo decenas...");
                    }
            }
        }
        return costoDecenas;
    }



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 663, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 388, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Descripción", jPanel2);

        jButton1.setText("empezar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addComponent(jButton1)
                .addContainerGap(505, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(jButton1)
                .addContainerGap(310, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Politica \"A\"", jPanel3);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 663, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 388, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Politica \"B\"", jPanel4);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 668, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        inicio();
    }//GEN-LAST:event_jButton1ActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Montecarlo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables

}
