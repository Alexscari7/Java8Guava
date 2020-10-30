package test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;

/**
 * @author wusd
 * @description 空
 * @create 2020/10/22 16:04
 */
public class Main{
    public static void main(String[] args){
        System.out.println(lengthOfLongestSubstring("bbbbb"));
    }

    public static int lengthOfLongestSubstring(String s) {
        int head = 0;
        int tail = 0;
        int max = 1;
        LinkedList<Character> window = new LinkedList<>();
        window.add(s.charAt(0));
        while(++tail < s.length()){
            if (window.contains(s.charAt(tail))){
                do{
                    window.removeFirst();
                    head++;
                }while(window.contains(s.charAt(tail)));
                window.add(s.charAt(tail));
            }else{
                window.add(s.charAt(tail));
                max = window.size() > max ? window.size() : max;
            }
        }
        return max;
    }
}

