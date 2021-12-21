package leetcode;

/**
 * @author wusd
 */
public class BinarySearch {

    public static void main(String[] args) {
        int v = 3;
        int[] a= {1,2,4,4,5};
        System.out.println(repeatBinarySearch(v, a));
    }


    // 标准二分，判断是否能找到target
    public static boolean binarySearch(int[] array, int target) {
        int start = 0;
        int end = array.length - 1;
        while (start <= end) {
            int middle = ((end - start) >> 1) + start;
            if (target < array[middle]) {
                end = middle - 1;
            } else if (target > array[middle]) {
                start = middle + 1;
            } else {
                return true;
            }
        }
        return false;
    }

    // 找到最大的小于等于target的数
    public static int leBinarySearch(int[] array, int target) {
        int start = -1;
        int end = array.length - 1;
        while (start < end) {
            int middle = ((end - start + 1) >> 1) + start;
            if (target < array[middle]) {
                end = middle - 1;
            } else {
                start = middle;
            }
        }
        return start < 0 ? -1 : array[start];
    }

    // 请实现有重复数字的有序数组的二分查找。
    public static int repeatBinarySearch (int v, int[] a) {
        int l=0;
        int r=a.length-1;
        while (l < a.length - 1) {
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
        return a.length;
    }
}
