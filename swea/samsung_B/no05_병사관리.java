package samsung_B;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.StringTokenizer;


    class UserSolution //(4고유번호, 1그룹, 1점수)
    {
        HashMap<List<Integer>, LinkedList<int[]>> linkedBoard; // 점수 관리 보드
        int[] version;//제거 여부 및 버전 관리 배열
        int[] teamInfo;
        public void init()
        {
            linkedBoard = new HashMap<>(5*10);
            for(int i=1 ; i <= 5 ; i++) {
                for (int j = 1; j <= 10; j++) {
                    linkedBoard.put(Arrays.asList(i,j), new LinkedList<>());
                }
            }

            version = new int[100001];
            teamInfo = new int[100001];
        }
        //	병사 고용
        public void hire(int mID, int mTeam, int mScore)
        {
            int v = version[mID];
            linkedBoard.getOrDefault(Arrays.asList(mTeam,mScore), new LinkedList<>()).add(new int[]{mID, v});
            version[mID]++;
            teamInfo[mID] = mTeam;
        }

        //	병사 해고
        public void fire(int mID)
        {
            version[mID]++;
            teamInfo[mID] = -1;
        }

        // 병사의 평판 점수 변경
        public void updateSoldier(int mID, int mScore)
        {

            int t = teamInfo[mID];
            linkedBoard.getOrDefault(Arrays.asList(t, mScore),new LinkedList<>()).add(new int[]{mID, version[mID]});
            version[mID]++;
        }

        //특정 팀에 속한 병사들의 평판 점수를 일괄 변경
        public void updateTeam(int mTeam, int mChangeScore) {
            if (mChangeScore < 0) {
                for (int j = 1; j <= 5; j++) {
                    int k = j + mChangeScore;
                    k = k < 1 ? 1 : (k > 5 ? 5 : k);
                    if (j == k) continue;

                    LinkedList<int[]> ee = linkedBoard.getOrDefault(Arrays.asList(mTeam, j), null);
                    if (ee == null) continue;
                    linkedBoard.get(Arrays.asList(mTeam, k)).addAll(ee);
                    linkedBoard.get(Arrays.asList(mTeam, j)).removeAll(ee);
                }
            }
            if (mChangeScore > 0) {
                for (int j = 5; j >= 1; j--) {
                    int k = j + mChangeScore;
                    k = k < 1 ? 1 : (k > 5 ? 5 : k);
                    if (j == k) continue;

                    LinkedList<int[]> ee = linkedBoard.getOrDefault(Arrays.asList(mTeam, j), null);
                    if (ee == null) continue;
                    linkedBoard.get(Arrays.asList(mTeam, k)).addAll(ee);
                    linkedBoard.get(Arrays.asList(mTeam, j)).removeAll(ee);
                }
            }
        }
        // 특정 팀 안에서 가장 평판 점수가 높은 병사를 검색
        public int bestSoldier(int mTeam) {
            int maxId = 0;
            int maxScore = 0;
            for (int i = 0; i <=9; i++) {
                LinkedList<int[]> ids = linkedBoard.getOrDefault(Arrays.asList(mTeam, i), null);
                if(ids == null) continue;
                for (int[] each : ids) {
                    if (version[each[0]]-1 == each[1]) {
                        if(maxScore < i){
                            maxId =  each[0];
                            maxScore = i;
                        }else if(maxScore == i){
                            maxId = Math.max(maxId, each[0]);
                        }
                    }
                }
            }//1. 점수가장 큰 순서 2.고유번호 큰 순서
            return maxId;
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
