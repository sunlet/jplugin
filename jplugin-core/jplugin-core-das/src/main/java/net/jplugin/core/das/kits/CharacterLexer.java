package net.jplugin.core.das.kits;

public class CharacterLexer{
	String words;
	int position;
	char word;
	
	CharacterLexer(String w){
		words = w;
		reset();
	}
	public void reset(){
		position = -1;
		word = 0;
	}
	
	public boolean next(){
		if (position>=words.length()-1) return false;
		word = words.charAt(++position);
		return true;
	}
	
	public boolean nextUntil(char s){
		while(next()){
			if (s == word) return true;
		}
		return false;
	}

}
