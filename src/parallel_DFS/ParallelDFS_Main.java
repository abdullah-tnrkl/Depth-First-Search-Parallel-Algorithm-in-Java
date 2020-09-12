package parallel_DFS;

import java.util.Calendar;
import java.util.Scanner;

public class ParallelDFS_Main {

	public static void main(String[] args) {
		long start, finish;
                System.out.println("Lütfen Node Sayisi Giriniz :  ");
                
           
                Scanner girdi = new Scanner(System.in);
                final int  numberOfNodes  =girdi.nextInt(); 
                        
                System.out.println("Lütfen Kullanıcacak Core Sayisi Giriniz :  ");
                Scanner coregirdi = new Scanner(System.in);    
  
               final int numberOfCore =coregirdi.nextInt();
                       
                       
                       
		 
		
		boolean[] visited = new boolean[numberOfNodes];
		for(int i = 0; i<numberOfNodes; i++){
			visited[i] = false;
		}
		Graph graph = new Graph(numberOfNodes,visited,numberOfCore);
		/**
		 * Parallel DFS 
		 * */
		start = Calendar.getInstance().getTimeInMillis();
		Thread[] processors = new islemci[numberOfCore];
		for(int i = 0; i<numberOfCore; i++){
			processors[i] = new islemci(graph,i);
			processors[i].start();
		}
		for(int i = 0; i<numberOfCore; i++){
			try {
				processors[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		finish = Calendar.getInstance().getTimeInMillis();
                long paralelzaman=finish-start;
		System.out.println("Parallel DFS zaman "+(paralelzaman)+" ms"); 
		boolean success = true;
		for(int i = 0;i<numberOfNodes;i++){
			if(!visited[i]){
				success = false;
				System.out.println("Failure");
				break;
			}
		}
		if(success)
			System.out.println("Parallel Depth First Search islemi tamamlandı. ");
                System.out.println("\n"+"****************************************"+"\n");
		
		/**
		 * Seriall DFS 
		 * */
		for(int i = 0;i<numberOfNodes;i++){
			visited[i] = false;
		}
		start = Calendar.getInstance().getTimeInMillis();
		SerialDFS serialDFS = new SerialDFS(numberOfNodes, visited, numberOfNodes-1);
		Thread serial = new Thread(serialDFS);
		serial.start();
		try {
			serial.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		finish = Calendar.getInstance().getTimeInMillis();
                long serizaman=finish-start;
		System.out.println("Seri DFS Geçen Zaman =  "+(serizaman)+" ms"); 
		success = true;
		for(int i = 0;i<numberOfNodes;i++){
			if(!visited[i]){
				success = false;
				System.out.println("Failure");
				break;
			}
		}
		if(success)
			System.out.println("Serial Depth First Search islemi tamamlandı. ");
                
               System.out.println("*************************************** "+"\n");
                
                if((paralelzaman-serizaman)<0){
                    
                    System.out.println("Paralel arama seri aramadan daha hızlıdır fark ise  "+(-1*(paralelzaman-serizaman))+" ms ");
                    
                }
                else{
                    System.out.println("  Seri arama paralelden daha hızlıdır fark ise  "+(paralelzaman-serizaman)+" ms ");
                    
                }
	}
        

}
