package leetcode;

/**
 * @author wusd
 * @description
 * 请实现有重复数字的有序数组的二分查找。
 * 输出在数组中第一个大于等于查找值的位置，如果数组中不存在这样的数，则输出数组长度。
 * 输入：5,4,[1,2,4,4,5]
 * 输出：3
 * @create 2020/10/15 17:30
 */
public class BinarySearch {
    public static int upper_bound_ (int n, int v, int[] a) {
        int l=0;
        int r=n-1;
        while (l < n - 1) {
            int mid = (l + r) / 2;
            if(a[mid] == v){
                // 针对条件：可重复
                if (mid > 0 && a[mid - 1] < a[mid]) {
                    return mid;
                }
                r = mid-1;
            }else if(a[mid] < v){
                l = mid+1;
            }else if(a[mid] > v){
                // 针对条件：可能不存在
                if(a[mid-1] < v){
                    return mid;
                }
                r=mid-1;
            }
        }
        return n;
    }

    public static void main(String[] args) {
        int v = 3;
        int[] a= {1,2,4,4,5};
        System.out.println(upper_bound_(a.length, v, a));
    }
}
