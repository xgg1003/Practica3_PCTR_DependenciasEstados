package p03.c01;

import java.util.Enumeration;
import java.util.Hashtable;

public class Parque implements IParque{

	// TODO -----------------
	private int maximo;
	private int contadorPersonasTotales;
	private Hashtable<String, Integer> contadoresPersonasPuerta;
	
	
	public Parque(int MAX_AFORO) {	// TODO---------------
		contadorPersonasTotales = 0;
		contadoresPersonasPuerta = new Hashtable<String, Integer>();
		this.maximo = MAX_AFORO;
	}


	public synchronized void entrarAlParque(String puerta){	// TODO-------------------------
		
		// Si no hay entradas por esa puerta, inicializamos
		if (contadoresPersonasPuerta.get(puerta) == null){
			contadoresPersonasPuerta.put(puerta, 0);
		}
		
		// Comprobamos que el parque no este en su aforo m�ximo
		try {
			comprobarAntesDeEntrar(contadorPersonasTotales);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		
		// Aumentamos el contador total y el individual
		contadorPersonasTotales++;		
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta)+1);
		
		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Entrada");
		
		// 
		checkInvariante();		
		
	}
	
	// Igual al entrar pero salir
	public void salirDelParque(String puerta) {
		
		// Si no hay entradas por esa puerta, inicializamos.
		if (contadoresPersonasPuerta.get(puerta) == null){
			contadoresPersonasPuerta.put(puerta, 0);
		}		
		
		// Aumentamos el contador total y el individual
		contadorPersonasTotales--;		
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta)-1);
		
		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Salida");
		
		// 
		checkInvariante();	
		
		// Comprobamos que el parque no se quede en usuarios negativos
		try {
			comprobarAntesDeSalir(contadorPersonasTotales);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void imprimirInfo (String puerta, String movimiento){
		System.out.println(movimiento + " por puerta " + puerta);
		System.out.println("--> Personas en el parque " + contadorPersonasTotales); //+ " tiempo medio de estancia: "  + tmedio);
		
		// Iteramos por todas las puertas e imprimimos sus entradas
		for(String p: contadoresPersonasPuerta.keySet()){
			System.out.println("----> Por puerta " + p + " " + contadoresPersonasPuerta.get(p));
		}
		System.out.println(" ");
	}
	
	private int sumarContadoresPuerta() {
		int sumaContadoresPuerta = 0;
			Enumeration<Integer> iterPuertas = contadoresPersonasPuerta.elements();
			while (iterPuertas.hasMoreElements()) {
				sumaContadoresPuerta += iterPuertas.nextElement();
			}
		return sumaContadoresPuerta;
	}
	
	protected void checkInvariante() {
		assert sumarContadoresPuerta() == contadorPersonasTotales : "INV: La suma de contadores de las puertas debe ser igual al valor del contador del parte";
		// TODO------------------
		assert maximo >= contadorPersonasTotales : "INV: El parque está en su aforo máximo";
		// TODO----------------
		assert 0 > contadorPersonasTotales : "INV: El parque está vacío";
	}


	protected void comprobarAntesDeEntrar(int contadorPersonasTotales) throws InterruptedException{	// TODO-----------
		while(contadorPersonasTotales > maximo)
			wait();
	}


	protected void comprobarAntesDeSalir(int contadorPersonasTotales)throws InterruptedException{  // TODO---------------
		while(contadorPersonasTotales <= 0)
			wait();
	}


}
