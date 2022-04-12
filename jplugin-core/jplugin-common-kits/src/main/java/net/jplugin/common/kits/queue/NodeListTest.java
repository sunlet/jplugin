package net.jplugin.common.kits.queue;

public class NodeListTest {

	public static void main(String[] args) {
		NodeList nl = new NodeList();
		nl.addFirst(new TheNode("2"));
		nl.addFirst(new TheNode("1"));
		nl.addTail(new TheNode("3"));
		nl.addTail(new TheNode("4"));
		
		System.out.println(nl.getString());
		
		nl.iteration((n)->System.out.println(n));
		nl.iterationFromTail((n)->System.out.println(n));
		
	}
	
	static class TheNode extends Node{
		private String name;

		TheNode(String a){
			this.name = a;
		}
		public String toString(){
			return name;
		}
	}
}
