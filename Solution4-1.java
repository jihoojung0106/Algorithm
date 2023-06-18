import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

/*
   1. 아래와 같은 명령어를 입력하면 컴파일이 이루어져야 하며, Solution4 라는 이름의 클래스가 생성되어야 채점이 이루어집니다.
       javac Solution4.java -encoding UTF8


   2. 컴파일 후 아래와 같은 명령어를 입력했을 때 여러분의 프로그램이 정상적으로 출력파일 output4.txt 를 생성시켜야 채점이 이루어집니다.
       java Solution4

   - 제출하시는 소스코드의 인코딩이 UTF8 이어야 함에 유의 바랍니다.
   - 수행시간 측정을 위해 다음과 같이 time 명령어를 사용할 수 있습니다.
       time java Solution4
   - 일정 시간 초과시 프로그램을 강제 종료 시키기 위해 다음과 같이 timeout 명령어를 사용할 수 있습니다.
       timeout 0.5 java Solution4   // 0.5초 수행
       timeout 1 java Solution4     // 1초 수행
 */

class Solution4 {
	static final int max_n = 1000;

	static int n, H;
	static int[] h = new int[max_n], d = new int[max_n-1];
	static int Answer;

	public static void main(String[] args) throws Exception {
		/*
		   동일 폴더 내의 input4.txt 로부터 데이터를 읽어옵니다.
		   또한 동일 폴더 내의 output4.txt 로 정답을 출력합니다.
		 */
		BufferedReader br = new BufferedReader(new FileReader("input4.txt"));
		StringTokenizer stk;
		PrintWriter pw = new PrintWriter("output4.txt");

		/*
		   10개의 테스트 케이스가 주어지므로, 각각을 처리합니다.
		 */
		for (int test_case = 1; test_case <= 10; test_case++) {
			/*
			   각 테스트 케이스를 표준 입력에서 읽어옵니다.
			   먼저 블록의 개수와 최대 높이를 각각 n, H에 읽어들입니다.
			   그리고 각 블록의 높이를 h[0], h[1], ... , h[n-1]에 읽어들입니다.
			   다음 각 블록에 파인 구멍의 깊이를 d[0], d[1], ... , d[n-2]에 읽어들입니다.
			 */
			stk = new StringTokenizer(br.readLine());
			n = Integer.parseInt(stk.nextToken()); H = Integer.parseInt(stk.nextToken());
			stk = new StringTokenizer(br.readLine());
			for (int i = 0; i < n; i++) {
				h[i] = Integer.parseInt(stk.nextToken());
			}
			stk = new StringTokenizer(br.readLine());
			for (int i = 0; i < n-1; i++) {
				d[i] = Integer.parseInt(stk.nextToken());
			}



			/////////////////////////////////////////////////////////////////////////////////////////////
			/*
			   이 부분에서 여러분의 알고리즘이 수행됩니다.
			   문제의 답을 계산하여 그 값을 Answer에 저장하는 것을 가정하였습니다.
			 */
			/////////////////////////////////////////////////////////////////////////////////////////////
			Answer = sol();



			// output4.txt로 답안을 출력합니다.
			pw.println("#" + test_case + " " + Answer);
			/*
			   아래 코드를 수행하지 않으면 여러분의 프로그램이 제한 시간 초과로 강제 종료 되었을 때,
			   출력한 내용이 실제로 파일에 기록되지 않을 수 있습니다.
			   따라서 안전을 위해 반드시 flush() 를 수행하시기 바랍니다.
			 */
			pw.flush();
		}

		br.close();
		pw.close();
	}
	/*시간을 dominate하는 것은 2번 중첩된 for문으로 시간복잡도는 Θ(n*H)로 구현했다*/
	public static int sol() {
		int[][] a=new int[n][H+1];
		int[][] b=new int[n][H+1];
		a[0][h[0]]=1;
		b[0][h[0]]=1;
		if(h[0]!=0){
			b[0][0]=1;
		}
		for(int i=1;i<n;i++){
			int tmp=0;
			for(int j=0;j<H+1;j++){
				if(j-h[i]>=0 && j-h[i]<H+1){
					if(j-h[i]+d[i-1]>=0 && j-h[i]+d[i-1]<H+1) {
						tmp = b[i - 1][j - h[i]] % 1000000;
						tmp %=1000000;
						if(a[i - 1][j - h[i] + d[i - 1]] - a[i - 1][j - h[i]]<0){
							tmp += (a[i - 1][j - h[i] + d[i - 1]] + 1000000 - a[i - 1][j - h[i]])%1000000;
						}
						else{tmp += (a[i - 1][j - h[i] + d[i - 1]] - a[i - 1][j - h[i]])%1000000;}
						a[i][j] = (int)(tmp% 1000000);

					}
					else{
						if(b[i - 1][j - h[i]] - a[i - 1][j - h[i]]<0){
							tmp += (b[i - 1][j - h[i]]+1000000 - a[i - 1][j - h[i]])%1000000;
						}
						else{tmp = b[i - 1][j - h[i]] - a[i - 1][j - h[i]];}

						a[i][j] = (int)(tmp% 1000000);

					}
				}
				else {
					if(j-h[i]+d[i-1]>=0 && j-h[i]+d[i-1]<H+1) {
						tmp = a[i - 1][j - h[i] + d[i - 1]];
						a[i][j] = (int)(tmp%1000000);

					}
					else{
						a[i][j] = 0;
					}
				}
				b[i][j]= b[i-1][j]%1000000;
				b[i][j] %=1000000;
				b[i][j]+=a[i][j]%1000000;
				b[i][j] %=1000000;
				a[i][j] %=1000000;
//
			}

		}
		int sum=0;
		for(int i=1;i<H+1;i++){
			sum+=b[n-1][i]%1000000;
			sum%=1000000;
		}
		return sum;
	}
}

