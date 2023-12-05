package samsung_B;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import java.util.StringTokenizer;

public class no08_공통조상 {
    static BufferedReader br;
    static StringBuilder sb;

    static int[] parent;
    static int[] sz;
    static ArrayList<Integer>[] adj;

    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();

        int t = Integer.parseInt(br.readLine());
        for(int tc = 1 ; tc <= t ; tc++){
            sb.append("#").append(tc).append(" ");
            String[] s = br.readLine().split(" ");

            int v = Integer.parseInt(s[0]);
            int e = Integer.parseInt(s[1]);
            int x = Integer.parseInt(s[2]);
            int y = Integer.parseInt(s[3]);
            parent = new int[v+1];
            sz = new int[v+1];
            adj = new ArrayList[v+1];
            for(int i=0 ; i < adj.length ; i++){
                adj[i] = new ArrayList<>();
            }
            StringTokenizer st = new StringTokenizer(br.readLine());
            for(int i = 0 ; i < e ; i++){
                int par = Integer.parseInt(st.nextToken());
                int ch = Integer.parseInt(st.nextToken());
                parent[ch] = par;
                adj[par].add(ch);
            }
            //초기화
            ArrayList<Integer> subx = new ArrayList<>();
            ArrayList<Integer> suby = new ArrayList<>();

            int cur = x;
            //루트가 나올때까지 리스트에 해당 노드보다 조상 노드를 더한다.
            while(true){
                subx.add(cur);
                cur = parent[cur];
                if(cur == 1){
                    break;
                }
            }

            cur = y;
            while(true){
                suby.add(cur);
                cur = parent[cur];
                if(cur == 1){
                    break;
                }
            }
            //이제 가장 가까운 노드 순서대로 저장되어있다.
            //가장 상위노드부터 비교하기 위해서 가장 뒷 인덱스부터 순회, 비교한다.
            int idxY = suby.size()-1;
            int idxX = subx.size()-1;
            int ans = 0;
            while(idxY >=0 && idxX >= 0){
                if(suby.get(idxY).equals(subx.get(idxX))){
                    ans = suby.get(idxY);
                    //최소공통조상을 갱신하고, 마지막으로 갱신된 조상이 최소공통조상이다.
                    //break를 하면 안됨!
                }
                idxX--;
                idxY--;
            }

            //자식노드의 개수를 구한다.
            dfs(1);
            //1이 아닌 최소공통조상이 없는 경우 1로 처리한다.
            ans = ans == 0 ? 1 : ans;
            sb.append(ans+ " " + sz[ans]);

        }
        System.out.println(sb.toString());
    }

    public static void dfs(int start){
        sz[start] = 1;
        for(int i=0 ; i < adj[start].size() ; i++){
            int node = adj[start].get(i);
            dfs(node);

            sz[start] += sz[node];
        }
    }
}
