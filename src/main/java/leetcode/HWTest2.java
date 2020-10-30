package leetcode;

import com.google.common.primitives.Chars;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author wusd
 * @description 数字加密
 * 给定一串字符串abcd，对字符串的每个字符进行加密，加密规则为，对每个字符在字母表中向后瞬移n位，可循环
 * 其中索引位置i=0,n=1、i=1,n=2、i=2,n=4、a[i]=a[i-3]+a[i-2]+a[i-1]，以此类推，求加密后的字符串
 * 实例：入参 abcd 出参 bdgj
 * @create 2020/10/28 20:15
 */
public class HWTest2 {

    public static char[] dict = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h','i', 'j', 'k', 'l', 'm', 'n'
                                ,'o', 'p', 'q', 'r', 's', 't', 'u', 'v','w', 'x', 'y', 'z'};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int count = Integer.valueOf(scanner.nextLine());

        int maxLength = 3;
        String[] input = new String[count];
        for (int i = 0; i < count; i++) {
            input[i] = scanner.nextLine();
            maxLength = maxLength < input[i].length() ? input[i].length() : maxLength;
        }

        int[] offset = new int[maxLength];
        offset[0] = 1;
        offset[1] = 2;
        offset[2] = 4;

        for (int i = 0; i < count; i++) {
            String orgin = input[i];
            char[] chars = orgin.toCharArray();
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < chars.length; j++) {
                chars[j] = trans(chars[j], j, offset);
                sb.append(chars[j]);
            }

            System.out.println(sb.toString());
        }
    }

    private static char trans(char before, int index, int[] offset) {
        if (offset[index] == 0) {
            offset[index] = offset[index-3] + offset[index-2] + offset[index-1];
        }
        return dict[(before - 97 + offset[index])%dict.length];
    }
}
