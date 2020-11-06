import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class StartGame {
	private final JFrame frame;
	public static void main(String[] args) {
		StartGame window = new StartGame();
		window.frame.setVisible(true);
	}
	public StartGame() {
		frame = new JFrame();
		frame.setTitle("SnakeAI");
		frame.setResizable(false);
		SnakePanel panel = new SnakePanel();
		new Thread(panel).start();
		panel.setBackground(Color.WHITE);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		frame.setBounds(100, 100, 200, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

public class SnakePanel extends JPanel implements Runnable{
	Snake  snake;
	public SnakePanel(){
		snake = new Snake();
		Node n = new Node(10,10);
		snake.getS().add(n);
		snake.setFirst(n);
		snake.setLast(n);
		snake.setTail(new Node(0,10)); 
		snake.setFood(new Node(80,80)); 
		
	}
	public void paint(Graphics g){
		super.paint(g);
		g.setColor(Color.RED);
		g.drawRect(10, 10, snake.map_size, snake.map_size);
		g.setColor(Color.WHITE);
		paintSnake(g, snake);
		g.setColor(Color.WHITE);
		paintFood(g, snake.getFood());
		SnakeAI ai = new SnakeAI();
		char dir=ai.play2(snake,snake.getFood());
		if(dir=='x'){
			System.out.println("Final Snake Length:" + snake.getLen());
			System.exit(0);
		}
		else{
			snake.move(dir);
		}
	}
	public void paintSnake(Graphics g , Snake snake){
		for(Node n: snake.getS()){
			if(n.toString().equals(snake.getFirst().toString())){
				g.setColor(Color.GREEN);
			}
			if(n.toString().equals(snake.getLast().toString()) && !snake.getFirst().toString().equals(snake.getLast().toString())){
				g.setColor(Color.BLUE); 
			}
			g.fillRect(n.getX(), n.getY(), snake.size, snake.size);
			g.setColor(Color.BLACK); 
		}
	}
	public void paintFood(Graphics g, Node food){
		g.setColor(Color.RED);
		g.fillRect(food.getX(), food.getY(), snake.size, snake.size);
	}
	public void run(){
		while (true) {
			try {
				Thread.sleep(50);
				this.repaint();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

public class SnakeAI {
	public char play1(Snake s,Node f) {
		Queue<Node> q = new LinkedList<Node>();
		Set<String> visited = new HashSet<String>();
		Map<String, String> path = new HashMap<String, String>();
		Stack<String> stack = new Stack<String>();
		q.add(s.getFirst());
		while (!q.isEmpty()) {
			Node n = q.remove();
			if (n.getX() == f.getX() && n.getY() == f.getY()) {
				String state = f.toString();
				while (state != null &&!state.equals(s.getFirst().toString())) {
					stack.push(state);
					state = path.get(state);
				}
				String []str;
				str = stack.peek().split("-");
				int x = Integer.parseInt(str[0]);
				int y = Integer.parseInt(str[1]);
				if (x > s.getFirst().getX() && y == s.getFirst().getY()) {
					return 'r';
				}
				if (x < s.getFirst().getX() && y == s.getFirst().getY()) {
					return 'l';
				}
				if (x == s.getFirst().getX() && y > s.getFirst().getY()) {
					return 'u';
				}
				if (x == s.getFirst().getX() && y < s.getFirst().getY()) {
					return 'd';
				}
			}
			Node up = new Node(n.getX(), n.getY() - Snake.size);
			Node right = new Node(n.getX() + Snake.size, n.getY());
			Node down = new Node(n.getX(), n.getY() + Snake.size);
			Node left = new Node(n.getX() - Snake.size, n.getY());
			if (!s.getMap().contains(up.toString()) && !visited.contains(up.toString()) && up.getX() <= Snake.map_size&& up.getX() >= 10 && up.getY() <= Snake.map_size && up.getY() >= 10) {
				q.add(up);
				visited.add(up.toString());
				path.put(up.toString(),n.toString());
			}
			if (!s.getMap().contains(right.toString()) && !visited.contains(right.toString()) && right.getX() <= Snake.map_size&& right.getX() >= 10 && right.getY() <= Snake.map_size && right.getY() >= 10) {
				q.add(right);
				visited.add(right.toString());
				path.put(right.toString(),n.toString());
			}
			if (!s.getMap().contains(down.toString()) && !visited.contains(down.toString()) && down.getX() <= Snake.map_size&& down.getX() >= 10 && down.getY() <= Snake.map_size && down.getY() >= 10) {
				q.add(down);
				visited.add(down.toString());
				path.put(down.toString(),n.toString());
			}
			if (!s.getMap().contains(left.toString()) && !visited.contains(left.toString()) && left.getX() <= Snake.map_size&& left.getX() >= 10 && left.getY() <= Snake.map_size && left.getY() >= 10) {
				q.add(left);
				visited.add(left.toString());
				path.put(left.toString(),n.toString());
			}
		}
		return 'x';
	}
	public char play2(Snake snake,Node f){
		Snake virSnake =new Snake(snake.getFirst(),snake.getLast(),snake.getFood(),snake.getTail());
		virSnake.setS((ArrayList<Node>) snake.getS().clone());
		virSnake.setMap((HashSet<String>) snake.getMap().clone());
		char realGoTofoodDir=play1(snake,f);
		if(realGoTofoodDir!='x'){
			while(!virSnake.getFirst().toString().equals(f.toString())){
				virSnake.move(play1(virSnake, f));
			}
				int goToDailDir=Asearch(virSnake,virSnake.getTail());
				if(goToDailDir!='x')return realGoTofoodDir;
				else {
					snake.c++;
					if(snake.c<100)return Asearch(snake,snake.getTail());
					else {
						return realGoTofoodDir;
					}
				}
		}else{
			char realGoToDailDir=Asearch(snake,snake.getTail());
			if(realGoToDailDir=='x'){
				realGoToDailDir=randomDir();
				int i=0;
				while(!snake.canMove(realGoToDailDir)){
					realGoToDailDir=randomDir();
					i++;
					if(i>300)return 'x';
				}
				return realGoToDailDir;
			}
			return realGoToDailDir;
		}
	}
	public char Asearch(Snake s,Node f){
		ArrayList<Node> openList = new ArrayList<Node>();
		ArrayList<Node> closeList = new ArrayList<Node>();
		Stack<Node> stack = new Stack<Node>();
		openList.add(s.getFirst());
		s.getFirst().setH(dis(s.getFirst(),f));
		while(!openList.isEmpty()){
			Node now=null;
			int min=Integer.MAX_VALUE;
			for(Node n:openList){
				if(n.getF()<=min){
					min=n.getF();
					now=n;
				}
			}
			openList.remove(now);
			closeList.add(now);
			Node up = new Node(now.getX(), now.getY() - Snake.size);
			Node right = new Node(now.getX() + Snake.size, now.getY());
			Node down = new Node(now.getX(), now.getY() + Snake.size);
			Node left = new Node(now.getX() - Snake.size, now.getY());
			ArrayList<Node> temp = new ArrayList<Node>(4);
			temp.add(up);
			temp.add(right);
			temp.add(down);
			temp.add(left);
			for (Node n : temp){
				if (s.getMap().contains(n.toString()) || closeList.contains(n) || n.getX() > Snake.map_size|| n.getX() < 10 || n.getY() > Snake.map_size || n.getY() < 10)
					continue;
				if(!openList.contains(n)){
					n.setFather(now);
					n.setG(now.getG()+10);
					n.setH(dis(n,f));
					openList.add(n);
					if (n.equals(f)) {
						Node node = n;
						while(node!=null&&!node.equals(s.getFirst())){
							stack.push(node);
							node=node.getFather();
						}
						int x = stack.peek().getX();
						int y = stack.peek().getY();
						if (x > s.getFirst().getX() && y == s.getFirst().getY()) {
							return 'r';
						}
						if (x < s.getFirst().getX() && y == s.getFirst().getY()) {
							return 'l';
						}
						if (x == s.getFirst().getX() && y > s.getFirst().getY()) {
							return 'u';
						}
						if (x == s.getFirst().getX() && y < s.getFirst().getY()) {
							return 'd';
						}
					}
				}
				else{
					if (n.getG() > (now.getG() + 10)) {
						n.setFather(now);
						n.setG(now.getG() + 10);
					}
				}
			}
		}
		return 'x';
	}
	public int dis(Node src,Node des){
		return Math.abs(src.getX()-des.getX())+Math.abs(src.getY()-des.getY());
	}
	public char randomDir(){
		int dir=(int)(Math.random()*4);
		if(dir==0)return 'd';
		else if(dir==1)return 'r';
		else if(dir==2)return 'u';
		else return 'l';
	}
}

public class Node {
	private int G =0;
	private int H =0;
	private Node father;
	private int x, y;
	
	public Node(int x, int y){
		this.x = x;
		this.y = y;
	}
	public int getF(){
		return G+H;
	}
	public int getG(){
		return G;
	}
	public void setG(int g){
		G = g;
	}
	public int getH(){
		return H;
	}
	public void setH(int h){
		H = h;
	}
	public Node getFather(){
		return father;
	}
	public void setFather(Node father){
		this.father = father;
	}
	public int getX(){
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public String toString(){
		return x+"-"+y;
	}
	
	public boolean equals(Object obj){
		if(this == obj)
			return true;
		if(obj instanceof Node){
			Node antherNode = (Node) obj;
			return this.x == antherNode.x && this.y == antherNode.y;
		}
		return false;
	}
}

public class Snake {
	public int c=0;    
	public final static int size =10;  
	public final static int map_size=150; 
	private Node first;   
	private Node tail;  
	private Node last;  
	private ArrayList<Node> s=new ArrayList<Node>();  
	private HashSet<String> map=new HashSet<String>(); 
	private char dir;
	private Node food;  

	public Snake(){

	}
	public Snake(Node first,Node last,Node food,Node tail){
		this.first=first;
		this.last=last;
		this.food=food;
		this.tail=tail;
	}
	private void add_Node(Node n){
		s.add(0, n);
		first=s.get(0);
		if(!n.toString().equals(food.toString())){
			tail=last;
			s.remove(last);
			last=s.get(s.size()-1);
		}else{
			food=RandomFood();
		}
	}
	public void move() {
		if(dir=='d'){
			Node n=new Node(first.getX(),first.getY()-10);
			add_Node(n);
		}
		if(dir=='r'){
			Node n=new Node(first.getX()+10,first.getY());
			add_Node(n);
		}
		if(dir=='l'){
			Node n=new Node(first.getX()-10,first.getY());
			add_Node(n);
		}
		if(dir=='u') {
			Node n = new Node(first.getX(), first.getY() + 10);
			add_Node(n);
		}
		updaterMap(s);
	}
	public void move(char dir){
		this.dir=dir;
		move();
	}
	public boolean canMove(char dir){
		if(dir=='d'){
			int X=first.getX();
			int Y=first.getY()-10;
			return Y >= 10 && !map.contains(X + "-" + Y);
		}
		if(dir=='r'){
			int X=first.getX()+10;
			int Y=first.getY();
			return X <= Snake.map_size && !map.contains(X + "-" + Y);
		}
		if(dir=='l'){
			int X=first.getX()-10;
			int Y=first.getY();
			return X >= 10 && !map.contains(X + "-" + Y);
		}
		if(dir=='u'){
			int X=first.getX();
			int Y=first.getY()+10;
			return Y <= Snake.map_size && !map.contains(X + "-" + Y);
		}
		return false;
	}
	public void updaterMap(ArrayList<Node> s){
		map.clear();
		for(Node n:s){
			map.add(n.toString());
		}
	}
	public Node RandomFood() {
		c=0;
		while(true){
			int x,y;
			x = Snake.size*(int) (Math.random() * Snake.map_size/Snake.size)+10;
			y = Snake.size*(int) (Math.random() * Snake.map_size/Snake.size)+10;
			Node n=new Node(x,y);
			if(!s.contains(n)){
				return n;
			}
		}
	}
	public int getLen() {
		return s.size();
	}
	public Node getTail() {
		return tail;
	}
	public void setTail(Node tail) {
		this.tail = tail;
	}
	public HashSet<String> getMap() {
		return map;
	}
	public Node getFirst() {
		return first;
	}
	public Node getLast() {
		return last;
	}
	public ArrayList<Node> getS() {
		return s;
	}
	public void setFirst(Node first) {
		this.first = first;
	}
	public void setLast(Node last) {
		this.last = last;
	}
	public void setS(ArrayList<Node> s) {
		this.s = s;
	}
	public void setMap(HashSet<String> map) {
		this.map = map;
	}
	public void setFood(Node food) {
		this.food = food;
	}
	public Node getFood() {
		return food;
	}
}

