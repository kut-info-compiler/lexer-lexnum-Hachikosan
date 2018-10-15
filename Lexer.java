import java.util.Scanner;

/*
 * 文字列から数値を読み取るプログラム
 *   0                           -> 整数 (例: 0)
 *   [1-9][0-9]*                 -> 整数 (例: 100)
 *   0[xX][0-9a-fA-F]+           -> 整数 (例: 0xabc)
 *   [0-9]*[a-fA-F][0-9a-fA-F]*  -> 整数 (例: 0123456789a)
 *   [1-9][0-9]*\.[0-9]*         -> 小数 (例: 10.3)
 *   0\.[0-9]*                   -> 小数 (例: 0.12)
 *   \.[0-9]+                    -> 小数 (例: .12)*/
  

public class Lexer {
	static class Token {
		static final String TYPE_INT = "INT";
		static final String TYPE_DEC = "DEC";
		static final String TYPE_ERR = "ERR";
		
		Token(String tokenType, int start, int len) {
			this.tokenType = tokenType;
			this.start = start;
			this.len = len;
		}
		
		String tokenType;  /* トークンの種類 */
		int start;         /* 文字列中のトークン開始位置 */
		int len;           /* トークンの長さ */
	}
	
	static final int CT_P = 0;
	static final int CT_X = 1;
	static final int CT_0 = 2;
	static final int CT_1 = 3;
	static final int CT_A = 4;
	static final int CT_OTHER = 5;

	/*
	 * 文字を分類する
	 *   [1-9] や [a-f] をまとめて扱えるようにするため．
	 */
	static int getCharType(int c) {
		if (c == '.')             return CT_P;//0
		if (c == 'x' || c == 'X') return CT_X;//1
		if (c == '0')             return CT_0;//2
		if ('1' <= c && c <= '9') return CT_1;//3
		if ('a' <= c && c <= 'f') return CT_A;//4
		if ('A' <= c && c <= 'F') return CT_A;
		return CT_OTHER;//5
	}


	
	int[][] delta = {
		/* TODO */
		/* 状態遷移表を作る */
		/*   delta[現状態][入力記号] */


		// Init,    0     /* 初期状態                     */
		// Int,     1     /* 整数状態                     */
		//Real,     2    /* 実数状態                     */
		//Id,       3    /* 識別子状態                   */
		//Final,    4    /* 終了状態                     */
		//Error     5    /* エラー状態                   */

		/*  P  X  0  1  A  OTHER */
		/*{ ?, ?, ?, ?, ?, ?}, /* 状態0 */
		/*{ ?, ?, ?, ?, ?, ?}, /* 状態1 */
		/*...*/
		/* number, alpha,period, delim */
  		{     4,    3,    1,    1,    1,    5 },
  		{     2,    4,    1,    1,    1,    4 }, 
 	    {     4,    4,    2,    2,    4,    4 }, 
  		{     5,    5,    1,    1,    1,    4 },


	};

	/*
	 * 文字列 str の start 文字目から字句解析しトークンを一つ返す
	 */
	Token getToken(String str, int start) {
		/* 現在注目している文字 (先頭から p 文字目)  */
		int p = start;

		/* 最後の受理状態のラベルとその時何文字目まで読んだか */
		String acceptType = Token.TYPE_ERR;
		int acceptPos = start;

		/* 現在の状態 */
		int currentState = 0;

		while (p < str.length()) {
			int c = str.charAt(p); /* str の p 文字目を読み取る */
			p++;
			
			int ct = getCharType(c);
			int nextState = delta[currentState][ct];

			/* TODO */
			/* 行先がなければループを抜ける */
			/* 行先が受理状態であれば「最後の受理状態」を更新する */

			//if(nextState == 4) break;
			if(nextState == 1){
					acceptType = Token.TYPE_INT;
				}else if(nextState == 2){
					acceptType = Token.TYPE_DEC;
				}else if(nextState == 4){
					break;
				}else if(nextState == 5){
					acceptType = Token.TYPE_ERR;
					break;
				}else{
			}
			currentState = nextState;
		}
		
		return new Token(acceptType, start, acceptPos - start);
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String str = sc.nextLine();  /* 1行読み取る */
		Lexer lex = new Lexer();
		Token t = lex.getToken(str, 0);
		System.out.print(t.tokenType);
		System.out.println(str.substring(t.start, t.start + t.len));
	}
}
