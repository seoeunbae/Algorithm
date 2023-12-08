package samsung_B;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.StringTokenizer;


    class UserSolution
    {
        public class Node{
            int id;
            int v;
            Node nxt;

            Node(int id, int v, Node nxt){
                this.id = id;
                this.v = v;
                this.nxt = nxt;
            }

            Node(int id, int v){
                this.id = id;
                this.v = v;
                this.nxt = null;
            }

            Node(){}
        }// 점수 관리 링크드 리스트에 쓰일 노드

        public Node[] node = new Node[200001];
        public int[] version =new int[100001];;//제거 여부 및 버전 관리 배열
        public int[] teamInfo = new int[100001];
        public int cnt = 0;



        public class Team {
            Node[] head = new Node[6];
            Node[] tail = new Node[6];
        }

        Team[] t = new Team[6];

        public Node pushAndGet(int id, Node nxt){
            Node ret = node[cnt++];
            ret.id = id;
            ret.nxt = nxt;
            ret.v = ++version[id];
            return ret;

        }
        public void init()
        {
            cnt = 0;
            for(int i=0 ; i < 200001 ; i++){
                if(node[i] == null) node[i]= new Node();
            }
            for(int i=0 ; i <= 5 ; i++){
                t[i] = new Team();
                for(int j=0 ; j < 6 ; j++){
                    t[i].tail[j] = t[i].head[j] = pushAndGet(0,null);
                }
            }

            for(int i=0 ; i <100001 ; i++){
                version[i] = 0;
                teamInfo[i] = 0;
            }
        }
        //	병사 고용
        public void hire(int mID, int mTeam, int mScore)
        {
            Node node = pushAndGet(mID, null);
            t[mTeam].tail[mScore].nxt = node;
            t[mTeam].tail[mScore] = node;
            teamInfo[mID] = mTeam;
            //0
        }

        //	병사 해고
        public void fire(int mID)
        {

            version[mID] = -1;
        }

        // 병사의 평판 점수 변경
        public void updateSoldier(int mID, int mScore)
        {
            hire(mID, teamInfo[mID] ,mScore);
        }

        //특정 팀에 속한 병사들의 평판 점수를 일괄 변경
        public void updateTeam(int mTeam, int mChangeScore) {
            if (mChangeScore < 0) {
                for (int j = 1; j <= 5; j++) {
                    int k = j + mChangeScore;
                    k = k < 1 ? 1 : (k > 5 ? 5 : k);
                    if (j == k) continue;

                    if(t[mTeam].head[j].nxt == null) continue;
                    t[mTeam].tail[k].nxt=t[mTeam].head[j].nxt;
                    t[mTeam].tail[k] = t[mTeam].tail[j];
                    t[mTeam].head[j].nxt = null;
                    t[mTeam].tail[j] = t[mTeam].head[j];
                }
            }
            if (mChangeScore > 0) {
                for (int j = 5; j >= 1; j--) {
                    int k = j + mChangeScore; // j : 업데이트 전 점수
                    k = k < 1 ? 1 : (k > 5 ? 5 : k); //k : 업데이트 후 점수
                    if (j == k) continue;

                    if(t[mTeam].head[j].nxt == null) continue;
                    t[mTeam].tail[k].nxt=t[mTeam].head[j].nxt;
                    t[mTeam].tail[k] = t[mTeam].tail[j];
                    t[mTeam].head[j].nxt = null;
//                    System.out.println(t[mTeam].head[j]);
                    t[mTeam].tail[j] = t[mTeam].head[j];
                 }
            }
        }
        // 특정 팀 안에서 가장 평판 점수가 높은 병사를 검색
        public int bestSoldier(int mTeam) {
            for(int j=5 ; j>=1 ; j--){
                Node node = t[mTeam].head[j].nxt;
                if(node == null) continue;

                int ans  = 0;
                while (node != null) {
                    if (node.v == version[node.id]) {
                        ans = ans < node.id ? node.id : ans;
                    }
                    node = node.nxt;
                }
                if (ans != 0) return ans;
            }
            return 0;
        }
}


class Solution
{
    private final static int CMD_INIT				= 1;
    private final static int CMD_HIRE				= 2;
    private final static int CMD_FIRE				= 3;
    private final static int CMD_UPDATE_SOLDIER		= 4;
    private final static int CMD_UPDATE_TEAM		= 5;
    private final static int CMD_BEST_SOLDIER		= 6;

    private final static UserSolution usersolution = new UserSolution();

    private static boolean run(BufferedReader br) throws Exception
    {
        StringTokenizer st;

        int numQuery;

        int mID, mTeam, mScore, mChangeScore;

        int userAns, ans;

        boolean isCorrect = false;

        numQuery = Integer.parseInt(br.readLine());

        for (int q = 0; q < numQuery; ++q)
        {
            st = new StringTokenizer(br.readLine(), " ");

            int cmd;
            cmd = Integer.parseInt(st.nextToken());

            switch(cmd)
            {
                case CMD_INIT:
                    usersolution.init();
                    isCorrect = true;
                    break;
                case CMD_HIRE:
                    mID = Integer.parseInt(st.nextToken());
                    mTeam = Integer.parseInt(st.nextToken());
                    mScore = Integer.parseInt(st.nextToken());
                    usersolution.hire(mID, mTeam, mScore);
                    break;
                case CMD_FIRE:
                    mID = Integer.parseInt(st.nextToken());
                    usersolution.fire(mID);
                    break;
                case CMD_UPDATE_SOLDIER:
                    mID = Integer.parseInt(st.nextToken());
                    mScore = Integer.parseInt(st.nextToken());
                    usersolution.updateSoldier(mID, mScore);
                    break;
                case CMD_UPDATE_TEAM:
                    mTeam = Integer.parseInt(st.nextToken());
                    mChangeScore = Integer.parseInt(st.nextToken());
                    usersolution.updateTeam(mTeam, mChangeScore);
                    break;
                case CMD_BEST_SOLDIER:
                    mTeam = Integer.parseInt(st.nextToken());
                    userAns = usersolution.bestSoldier(mTeam);
                    ans = Integer.parseInt(st.nextToken());
                    if (userAns != ans) {
                        isCorrect = false;
                    }
                    break;
                default:
                    isCorrect = false;
                    break;
            }
        }

        return isCorrect;
    }

    public static void main(String[] args) throws Exception
    {
        int TC, MARK;

        //System.setIn(new java.io.FileInputStream("res/sample_input.txt"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");

        TC = Integer.parseInt(st.nextToken());
        MARK = Integer.parseInt(st.nextToken());

        for (int testcase = 1; testcase <= TC; ++testcase)
        {
            int score = run(br) ? MARK : 0;
            System.out.println("#" + testcase + " " + score);
        }

        br.close();
    }
}
