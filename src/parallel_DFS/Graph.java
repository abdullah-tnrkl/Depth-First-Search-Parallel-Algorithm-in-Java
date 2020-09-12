package parallel_DFS;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class Graph {
	private int size;//Düğüm Sayısı
	private int[][] kose;//Matris oluşturmak için 
	private Stack<Integer> genelyigin;
	private List<Stack<Integer>> localStacks;
	public List<Stack<Integer>> getLocalStacks() {
		return localStacks;
	}
	public void setLocalStacks(List<Stack<Integer>> localStacks) {
		this.localStacks = localStacks;
	}

	private boolean[] visited;
	private boolean isDone;
	private int counter;
	public Graph(int size,boolean[] visited,int numberOfProcessors){
		this.size = size;
		localStacks = new ArrayList<Stack<Integer>>(numberOfProcessors);
		for(int i=0;i<numberOfProcessors;i++){
			localStacks.add(new Stack<Integer>());
		}
		kose = new int[size][size];
		this.visited = visited;
		isDone = false;
		genelyigin = new Stack<Integer>();
		genelyigin.push(size-1);
		counter = 0;
		for(int i = 0; i<this.size; i++)
			for(int j = 0; j<this.size; j++){
				Random boolNumber = new Random();
                boolean edge = boolNumber.nextBoolean();
                if(i==j)
                	kose[i][j]=1;
                else	
                	kose[i][j]=edge ? 1 : 0;
			}
	}
	public int getSize(){
		return size;
	}
	
	public synchronized boolean getVisited(int index){
		return visited[index];
	}
	
	public synchronized void setVisited(int index, boolean value){
		visited[index] = value;
	}
	
	public synchronized void pushStack(Stack<Integer> tmp){
		while(!tmp.isEmpty()){
			genelyigin.push(tmp.pop());
		}
	}
	
	public boolean isNeighbour(int node, int neighbour){
		return kose[node][neighbour]==1 ? true : false;
	}
	
	public synchronized void incrementCounter(){
		counter++;
	}
	
	public boolean isDone() {
		return isDone;
	}
	
	public synchronized void dfs(){
		while(!isDone && genelyigin.empty()){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		int index = (int)(Thread.currentThread().getId());
		if(!genelyigin.isEmpty()){
			boolean popped = false;
			int node = genelyigin.pop();
			popped = true;
			while(visited[node]){
				if(genelyigin.empty()){
					isDone = true;
					popped = false;
					break;
				}else{
					node = genelyigin.pop();
					popped = true;
				}
			}
			if(popped){
				visited[node] = true;
				counter++;
				boolean flag = false;
				for(int i = 0;i<size;i++){
					if(node==i)continue;
					if(isNeighbour(node, i) && !visited[i] && !flag){
						localStacks.get(index).push(i);
						flag = true;
					}
					if(isNeighbour(node, i) && !visited[i] && flag){
						genelyigin.push(i);
					}
				}
			}
		}
		if(genelyigin.empty())
			isDone = true;
		if(isDone && counter<size){
			isDone = false;
			for(int i=0;i<size;i++){
				if(!visited[i])
					genelyigin.push(i);
			}
		}
		notifyAll();
	}
	
}
