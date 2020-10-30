package sort;

import java.util.Arrays;

/**
 * @author wusd
 * @description 快速排序
 * 基本思想：分治
 * 过程：每次选取一个key值，做一次排序，使比key小的值全部在它左边，比key大的值全部在它右边，然后将两边分别应用过程，直到左右区间只有一个数
 * 理解：理解不了，暂时使用挖坑填数来记忆
 * @create 2020/10/11 17:42
 */
public class QuickSort {

    public static void quickSort(int[] a, int l, int r){
        // 递归终止条件数组左极限=右极限，即只有一个元素
        if(l >= r){
            return;
        }
        //完成一次排序，使随机key左边元素全部小于key，右边元素大于key
        int mid = adjust(a, l, r);
        quickSort(a, l, mid-1);
        quickSort(a, mid+1, r);
    }

    private static int adjust(int[] a, int l, int r) {
        // 定义前后指针
        int p1 = l;
        int p2 = r;
        // 选取第一个元素作为key，还可以选其他元素，然后swap到第一个
        int key = a[l];
        while(p1 < p2){
            // 从右往左选一个小于key的元素，将key坑填补
            while (p1 < p2 && a[p2] >= key) {
                p2--;
            }
            // 填补后p1指针需要前移，此时坑位变成p2
            if(p1 < p2){
                a[p1] = a[p2];
                p1++;
            }
            // 从左往右选一个大于key的元素，将上一个坑填补
            while (p1 < p2 && a[p1] <= key) {
                p1++;
            }
            // 填补后p2指针需要后移，此时坑位变成p1
            if(p1 < p2){
                a[p2] = a[p1];
                p2--;
            }
            // 当p1=p2时终止，key两边元素符合要求
        }
        // 将最后留的坑用key填补
        a[p1] = key;
        return p1;
    }

    public static void main(String[] args) {
        int[] a = {3,1,2,5,4,7};
        quickSort(a, 0, 4);
        System.out.println(Arrays.toString(a));
    }
}
