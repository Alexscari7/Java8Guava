package leetcode;

import sort.QuickSort;

import java.util.Arrays;

/**
 * @author wusd
 * @description 空
 *
 * 给定一个排序好的数组，两个整数 x 和 y，从数组中找到最靠近 y（两数之差最小）的 x 个数。
 * 返回的结果必须要是按升序排好的。如果有两个数与 y 的差值一样，优先选择数值较小的那个数。
 *
 * 示例 1:
 * 输入: [1,2,3,4,5], x=4, y=3
 * 输出: [1,2,3,4]
 *
 * 示例 2:
 * 输入: [1,2,4,6,8], x=4, y=5
 * 输出: [2,4,6,8]
 * @create 2020/10/14 17:17
 */
public class Test001 {
    public static void main(String[] args) {
        int[] a = {1,2,4,6,8};
        int x=4;
        int y=3;
        int[] result = solution(a, x, y);
        System.out.println(Arrays.toString(result));
    }

    public static int[] solution(int[] a, int x, int y) {
        if (a == null || a.length == 0 || x <= 0 || x > a.length) {
            return null;
        }
        // x长度为整个a
        if (x == a.length) {
            return a;
        }

        int[] result = new int[x];
        int location = -1;
        for (int m = 0; m < a.length; m++) {
            if (a[m] >= y) {
                location = m;
                break;
            }
        }

        // a中数比y小
        if (location < 0) {
            for (int i = 0; i < x; i++) {
                result[i] =  a[a.length -x+i];
            }
            return result;
        }

        int prev = location -1;
        int next = location;


        int i = 0;
        while (i < x) {
            if(prev >= 0 && next < a.length){
                result[i++] = (a[next]-y) < (y - a[prev]) ? a[next++] : a[prev--];
                continue;
            }
            if (prev >= 0) {
                result[i++] = a[prev--];
            }
            if (next < a.length) {
                result[i++] = a[next++];
            }
        }
        QuickSort.quickSort(result, 0, result.length-1);

        return result;
    }
}
