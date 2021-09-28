 import java.util.concurrent.Semaphore; 
 import java.util.Random; 
 public class PBarberoDormilon { 
	 static int SILLAS=5; 
	 public static Semaphore clientes=new Semaphore(0); 
	 public static Semaphore barberos=new Semaphore(0); 
	 public static Semaphore mutex =new Semaphore(1); 
	 public static int clientesesperando=0; 
	 public static Cliente c[]=new Cliente[SILLAS]; 
	 public static Barbero b=new Barbero(); 
	 private static Random r=new Random(); 
	 public static class Barbero extends Thread { 
		 @Override 
		 public void run(){ 
			 while (true) { 
				 try { 
					 clientes.acquire(); 
					 mutex.acquire(); 
					 clientesesperando--; 
					 barberos.release(); 
					 mutex.release(); 
					 int aux=r.nextInt(1000); 
					 Thread.sleep(aux); 
					 System.out.println("Barbero cortando el pelo."); 
					 } 
				 catch (InterruptedException ex){ ex.printStackTrace(); 
				 }
				 }
			 }
		 }
	 public static class Cliente extends Thread {
		 private int id; 
		 public Cliente(int id){ 
			 this.id=id; 
		 } 
		 @Override 
		 public void run(){ 
			 try {
				 mutex.acquire(); 
				 if(clientesesperando<SILLAS) {
					 clientesesperando++;
					 clientes.release();
					 mutex.release();
					 barberos.acquire();
					 System.out.println("Cliente " + id + " siendo pelado. Clientes esperando:"+clientesesperando);
					 } 
				 else {
					 mutex.release();
					 }
				 } 
			 catch (InterruptedException ex){ ex.printStackTrace();
			 }
			 }
		 } 
	 public static void main(String[] args) throws InterruptedException {
		 for(int i=0;i<SILLAS;i++){
			 c[i]=new Cliente(i);
			 c[i].start();
			 }
		 b.start();
		 for(int i=0;i<SILLAS;i++){
			 try {
				 c[i].join();
				 }
			 catch (InterruptedException ex) {
				 ex.printStackTrace(); }
			 }
		 b.join();
		 }
	 }