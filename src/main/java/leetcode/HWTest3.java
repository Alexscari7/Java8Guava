package leetcode;

import java.util.Scanner;

/**
 * @author wusd
 * @description 最大间隔找位置
 * 给定椅子数量seatNum，椅子索引位置从0到seatNum-1，每来一个人需要找到合适的位置使自己离其他人最远，并返回索引，每个人可以随时离开
 * 没有人时优先做最小的位置，当有两个位置优先级相同时坐在小位置，椅子坐满时再来人则返回-1
 * 入参：seatNum椅子数量，seatOrLeave[]坐椅子或离开的人，1表示来人，负数表示该索引位置上的人离开
 * 示例：seatNum=10,seatOrLeave[]={1,1,1,1,-4,1}，最后入座的最优位置是5
 * @create 2020/10/28 21:20
 */
public class HWTest3 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int seatNum = sc.nextInt();
        sc.nextLine();
        String seatOrLeaveLine = sc.nextLine();
        String[] c = seatOrLeaveLine.substring(1,seatOrLeaveLine.length()-1).split(", ");
        int[] seatOrLeave = new int[c.length];
        for (int i = 0; i < c.length; i++) {
            seatOrLeave[i] = Integer.parseInt(c[i]);
        }

        HWTest3 socialDistance = new HWTest3();
        int ans = socialDistance.conferenceSeatDistance(seatNum, seatOrLeave);
        System.out.println(ans);
    }

    /**
     * 计算最后进来的人，坐的位置
     * @param seatNum 会议室座位总数
     * @param seatOrLeave 员工的进出顺序
     * @return 最后进来的人，坐的位置
     */
    public int conferenceSeatDistance(int seatNum, int[] seatOrLeave) {
        boolean[] seat = new boolean[seatNum];
        int lastIndex = 0;
        for (int i = 0; i < seatOrLeave.length; i++) {
            if(seatOrLeave[i] > 0){
                lastIndex = sitDown(seat);
            }else{
                getUp(seat, -seatOrLeave[i]);
            }
        }

        return lastIndex;
    }

    private void getUp(boolean[] seat, int index) {
        seat[index] = false;
    }

    private int sitDown(boolean[] seat) {
        int index = 0;
        if (firstSitDown(seat)) {
            index = 0;
        }else if(secondSitDown(seat)){
            index = seat.length-1;
        }else if(noSeat(seat)){
            index = -1;
        } else{
            int start = 0;
            int end = 0;
            int p = 0;
            while (++p < seat.length) {
                if(seat[p]){
                    if (start == 0 && end == 0) {
                        end = p;
                    } else if(p - end > end - start + 1){
                        start = end;
                        end = p;
                    }
                }
            }
            index = (start + end)/2;
        }
        seat[index] = true;
        return index;
    }

    private boolean noSeat(boolean[] seat) {
        for (int i = 0; i < seat.length; i++) {
            if(!seat[i]){
                return false;
            }
        }
        return true;
    }

    private boolean secondSitDown(boolean[] seat) {
        int count = 0;
        for (int i = 0; i < seat.length; i++) {
            if(seat[i]){
                count++;
            }
        }
        return count == 1;
    }

    private boolean firstSitDown(boolean[] seat){
        for (int i = 0; i < seat.length; i++) {
            if(seat[i]){
                return false;
            }
        }
        return true;
    }
}