package net.jplugin.core.das.route.impl.parser;

import java.util.ArrayList;
import java.util.List;

public class SqlWordsWalker{
	String[] words;
	public int position;
	public String word;
	public String sql;
	
	public static SqlWordsWalker createFromSql(String sql){
		List<String> words = new SqlStrLexerToolNew(sql).parse();
		String[] arr = new String[words.size()];
		SqlWordsWalker sw = new SqlWordsWalker((String[]) words.toArray(arr));
		sw.sql = sql;
		return sw;
	}
	
	SqlWordsWalker(String[] w){
		words = w;
		reset();
	}
	
	public String[] getArray(){
		return this.words;
	}
	
	/**
	 * 这两个直接查询数组
	 * @param start
	 * @param s
	 * @return
	 */
	public int positionFrom(int start,String s){
		for (int i=start;i<words.length;i++){
			if (s.equals(words[i])) return i;
		}
		return -1;
	}
	public String wordAt(int i){
		return words[i];
	}
	public int size(){
		return words.length;
	}
	
	public void reset(){
		position = -1;
		word = null;
	}
	
	public boolean next(){
		if (position>=words.length-1) return false;
		word = words[++position];
		return true;
	}
	public boolean next(int n){
		if (position>=words.length-n) return false;
		word = words[position+=n];
		return true;
	}
	

	public void setPosition(int pos){
		if (pos>=-1 && pos<=words.length-1){
			this.position = pos;

			//注意，position可以是-1,此时初始化为null
			if (pos!=-1) {
				word = words[pos];
			}else 
				word = null;
		}
	}
	
	public int getPosition(){
		return this.position;
	}
	
	public boolean nextUntilIgnoreCase(String s){
		while(next()){
			if (s.equalsIgnoreCase(word)) return true;
		}
		return false;
	}
	public boolean nextUntil(String s){
		while(next()){
			if (s.equals(word)) return true;
		}
		return false;
	}

	public String getNextWord(int n){
		if (position>=words.length - n){
			return null;
		}else 
			return words[position+n];
	}

	public String getNextWord(){
		if (position>=words.length-1){
			return null;
		}else 
			return words[position+1];
	}

	/**
	 * 开始的先决条件是已经当前是左括号了。
	 * 要找它匹配的右括号
	 */
	public void nextUntilMatchingBracket() {
		int leftBracketNum=1;
		
		while(this.next()){
			if ("(".equals(word))
				leftBracketNum++;
			if (")".equals(word)){
				leftBracketNum--;
				if (leftBracketNum==0)
					break;
			}
		}
	}
	
	
	public  String toSql(){
		return appendToBuffer(new StringBuffer(),words);
	}

	public static String appendToBuffer(StringBuffer sb,String[] list) {
		boolean first = true;
		for (String s : list) {
			if (first) {
				sb.append(s);
				first = false;
			} else if (s.equals("(")){
				sb.append(s);
			} else
				sb.append(" ").append(s);
		}
		return sb.toString();
	}
}
