package net.jplugin.common.kits.queue;

enum NodeType{Simple,Query,Slice}
public class Node {
	Node prev;
	Node next;
	
	/**
	 * 是否头结点
	 * @return
	 */
	public boolean headNode(){
		return false;
	}
	
	public void addAfter(Node n) {
		Node temp = this.next;
		this.next = n;
		n.prev = this;

		n.next = temp;
		temp.prev = n;
	}
	public void addBefore(Node n) {
		Node temp = this.prev;
		this.prev = n;
		n.next = this;
		
		n.prev = temp;
		temp.next= n;
	}
}
