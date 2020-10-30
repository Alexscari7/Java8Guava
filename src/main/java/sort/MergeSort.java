package sort;

import java.util.Arrays;

/**
 * @author wusd
 * @description 归并排序
 * 原理：分治
 * 过程：每次将数组分为两部分，两部分排好序之后再进行合并。
 * @create 2020/10/11 19:17
 */
public class MergeSort {

    private static void mergeSort(int[] a, int l, int r) {
        if (l >= r) {
            return;
        }
        // 找到中间索引位置，分为左右两部分，进行递归，直到左右大小都是1，默认有序，依次向上合并
        int mid = l + (r - l)>>1;
        mergeSort(a, l, mid);
        mergeSort(a, mid+1, r);
        merge(a, l, mid, r);
    }

    private static void merge(int[] a, int l, int mid, int r) {
        int i=0;
        // 定义两个指针，同时遍历两个数组，生成有序队列复制到temp中，如果最后有一个数组没有遍历完，还需要额外复制
        int p1 = l;
        int p2 = mid+1;
        // 定义临时数组，用于存放合并后的有序数列
        int[] temp = new int[r-l+1];
        while (p1 <= mid && p2 <= r) {
            temp[i++] = a[p1] < a[p2] ? a[p1++] : a[p2++];
        }
        while (p1 <= mid) {
            temp[i++] = a[p1++];
        }
        while (p2 <= r) {
            temp[i++] = a[p2++];
        }
        // 将temp复制到目标数组指定位置
        for (i = 0; i < temp.length; i++) {
            a[l + i] = temp[i];
        }
    }

    public static void main(String[] args) {
        int[] a = {3,1,2,5,4,7};
        mergeSort(a, 0, 5);
        System.out.println(Arrays.toString(a));
    }
}
