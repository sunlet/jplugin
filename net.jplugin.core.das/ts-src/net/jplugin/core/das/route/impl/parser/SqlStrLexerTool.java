package net.jplugin.core.das.route.impl.parser;

import java.util.ArrayList;

public class SqlStrLexerTool {
	public static final int STATE_STR_CONST = 1;
	public static final int STATE_WORD = 2;
	public static final int STATE_SPACING = 3;
	public static final int STATE_OPERATOR = 4;

	public static ArrayList<String> parse(String sqlStr){
		CharacterLexer cmc = new CharacterLexer(sqlStr);
		ArrayList list = new ArrayList<>();
		int state;
		int startPos = 0;
		state = STATE_SPACING;
		
		if (cmc.next())
			while(true){
				if (state == STATE_STR_CONST){
					if (cmc.word == '\\'){
						cmc.next(); //把下一个直接过掉
						if (!cmc.next()) break;
					}else if (cmc.word == '\''){
						list.add(sqlStr.substring(startPos, cmc.position+1));
						state = STATE_SPACING;
						if (!cmc.next()) break;
					}else{
						if (!cmc.next()) {
							break;
						}
					}
				}else if (state == STATE_OPERATOR){
					if (cmc.word == ' '){
						list.add(sqlStr.substring(startPos,cmc.position));
						state = STATE_SPACING;
						if (!cmc.next()) break;
					}else if (isOperator(cmc.word)){
						if (isOperatorSet(sqlStr,startPos,cmc.position)){
							//组合作为一个，状态变化
							list.add(sqlStr.substring(startPos,cmc.position+1));
							state = STATE_SPACING;
						}else{
							//把上一个记录下来，状态不变
							list.add(sqlStr.substring(startPos,cmc.position));
							startPos = cmc.position;
						}
						if (!cmc.next()) break;
					}else if (cmc.word == '\''){
						list.add(sqlStr.substring(startPos,cmc.position));
						state = STATE_STR_CONST;
						startPos = cmc.position;
						if (!cmc.next()) 
							break;
					}else{
						list.add(sqlStr.substring(startPos,cmc.position));
						state = STATE_WORD;
						startPos = cmc.position;
						if (!cmc.next()) 
							break;
					}
				}else if (state == STATE_WORD){
					if (cmc.word == ' '){
						list.add(sqlStr.substring(startPos,cmc.position));
						state = STATE_SPACING;
						startPos = cmc.position;
						if (!cmc.next()) break;
					}else if (isOperator(cmc.word)){
						list.add(sqlStr.substring(startPos,cmc.position));
						startPos = cmc.position;
						state = STATE_OPERATOR;
						if (!cmc.next()) break;
					}else{
						if (!cmc.next()) break;
					}
				}else{ //STATE_SPACING
					if (cmc.word==' '){
						if (!cmc.next()) break;
					}else if (cmc.word=='\''){
						state = STATE_STR_CONST;
						startPos = cmc.position;
						if (!cmc.next()) break;
					}else if (isOperator(cmc.word)){
						state = STATE_OPERATOR;
						startPos = cmc.position;
						if (!cmc.next()) break;
					}else{
						state = STATE_WORD;
						startPos = cmc.position;
						if (!cmc.next()) break;
					}
				}	
			}
		
		//最后一段
		if (state!=STATE_SPACING && startPos<=sqlStr.length()-1){
			list.add(sqlStr.substring(startPos));
		}
		
		return list;
	}

	private static boolean isOperatorSet(String sql, int startPos, int endPos) {
		if (endPos - startPos!=1) return false;
		char c1 = sql.charAt(startPos), c2= sql.charAt(endPos);
		return (c1=='>'&& c2=='=') || (c1=='<'&& c2=='=') || (c1=='<'&& c2=='>');
	}

	private static boolean isOperator(char word) {
		return word == '=' || word == '<' || word == '>' || 
			   word == '(' || word == ')' || word==','|| 
			   word == '&'|| word == '^' || word== '+'||
			   word == '-'|| word=='*' || word=='/';
	}
	
	public static void main(String[] args) {
		ArrayList<String> result = SqlStrLexerTool.parse("  select * from customer a where a.x=?) and a.y='12' and (a.z<3 or a.z>=5)  ");
		for (String s:result){
			System.out.println("- "+ s);
		}
		
	}
}