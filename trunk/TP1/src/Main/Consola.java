package Main;

 

/**
 * Clase para facilitar operaciones de carga por teclado en consola estándar
 * @author Instructores de CISCO System para su curso de Fundamentos de Java 1.1 - Modificado por Valerio Frittelli
 * @version Mayo de 2004
*/
 
public class Consola
{
    /*
     * 1) Todos los métodos de esta clase son estáticos, y por lo tanto pueden ser invocados sin tener que crear objetos de la clase. Es 
     * suficiente con nombrar la clase al invocar el método:   int x = Consola.readInt();
     */

    /**
     * Lee un string desde teclado. El string termina con un salto de linea
     * @return el string leido (sin el salto de linea)
    */
    public static String readLine()
    { 
       int ch;
       String r = "";
       boolean done = false;
       while (!done)
       {
	    try
	    {
    		ch = System.in.read();
    		if (ch < 0 || (char)ch == '\n') { done = true; }
    		else 
    		{
    		     if ((char)ch != '\r') { r = r + (char) ch; }
    		}
	    }
	    catch(java.io.IOException e)
	    {
    		done = true;
	    }
       }
       return r;
    }
  
    /**
     * Lee un integer desde teclado. La entrada termina con un salto de linea
     * @return el valor cargado, como un int
     */
    public static int readInt()    
    {
       while(true)
       { 
    	  try
    	  {
    	      return Integer.parseInt(readLine().trim());
    	  }
    	  catch(NumberFormatException e)
    	  {
    	      System.out.println("No es un integer. Por favor, pruebe otra vez!");
    	  }
       }
    }
  
    /**
     * Lee un double desde teclado. La entrada termina con un salto de linea
     * @return el valor cargado, como un double
     */
    public static double readDouble()
    {
       while(true)
       { 
    	   try
    	   {
    	      return Double.parseDouble(readLine().trim());
    	   }
    	   catch(NumberFormatException e)
    	   {
    	      System.out.println("No es un flotante. " + "Por favor, pruebe otra vez!");
    	   }
       }
    }
}
