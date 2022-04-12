package net.jplugin.common.kits.queue;

import java.util.function.Function;

public class NodeList{
	Node head = new Node(){
		public boolean headNode() {
			return true;
		};
	}; 
	
	NodeList(){
		head.next = head;
		head.prev = head;
	}
	
	public void addFirst(Node n){
		head.addAfter(n);
	}

	public void addTail(Node n){
		head.addBefore(n);
	}
	
	public int size(){
		int cnt=0;
		Node temp = head;
		while(temp.next!=head){
			cnt++;
			temp = temp.next;
		}
		return cnt;
	}
	
	public void iteration(INodeOperation op){
		Node temp = head;
		while(temp.next!=head){
			temp = temp.next;
			op.call(temp);
		}
	}
	
	public void iterationFromTail(INodeOperation op){
		Node temp = head;
		while(temp.prev!=head){
			temp = temp.prev;
			op.call(temp);
		}
	}
	
	public String getString(){
		StringBuffer sb = new StringBuffer();
		boolean first = true;
		Node temp = head;
		while(temp.next!=head){
			if (!first){
				sb.append(" | ");
			}else{
				first = false;
			}
			temp = temp.next;
			sb.append(temp.toString());
			
		}
		return sb.toString();
	}
}
