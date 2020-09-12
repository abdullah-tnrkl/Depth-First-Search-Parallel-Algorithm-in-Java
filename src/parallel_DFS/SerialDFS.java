package parallel_DFS;

import java.util.Stack;

public class SerialDFS implements Runnable{
	private boolean[] visited;
	private Graph graph;
	private Stack<Integer> stack;
	private int baslangicnode;
	public SerialDFS(int size,boolean[] visited,int beginPoint){
		graph = new Graph(size,visited,1);
		this.visited = visited;
		stack = new Stack<>();
		baslangicnode = beginPoint;
	}

    SerialDFS() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
	@Override
	public void run() {//DepthFirstSearch run sınıfı
		for(int i = 0;i<graph.getSize();i++){
			visited[i] = false;
		}
		stack.push(baslangicnode);
		while(!stack.isEmpty()){
			int node = stack.pop();
			if(visited[node]==false){
				visited[node] = true;
				for(int i = 0; i<graph.getSize(); i++){
					if(node==i)continue;
					if(graph.isNeighbour(node, i) && visited[i]==false){
						stack.push(i);
					}
				}
			}
		} 
	}
}
