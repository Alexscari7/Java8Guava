package leetcode;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import entity.TreeNode;

import java.util.HashMap;
import java.util.List;

/**
 * @author wusd
 * @description 根据先序遍历和中序遍历重建二叉树
 * @create 2020/10/30 10:57
 */
public class rebuildBTree {
    public static void main(String[] args) {
        int[] preorder = {1, 2, 4, 7, 3, 5, 6, 8};
        int[] inorder = {4, 7, 2, 1, 5, 3, 8, 6};
        // 根据先序和中序序列重建二叉树
        TreeNode root =  rebuildTree(preorder, 0, preorder.length-1,
                inorder, 0, inorder.length-1);
    }

    private static TreeNode rebuildTree(int[] preorder, int startPreorder, int endPreorder, int[] inorder,
                                     int startInorder,
                                 int endInorder) {
        TreeNode root = new TreeNode();
        // 获取root元素在中序中的相对位置
        int breakLength = getBreakLength(preorder, startPreorder, inorder, startInorder, endInorder);

        int lStartInorder = startInorder;
        int lEndInorder = startInorder + breakLength - 1;
        int lStartPreorder = startPreorder + 1;
        int lEndPreorder = startPreorder + breakLength;

        int rStartInorder = startInorder + breakLength + 1;
        int rEndInorder = endInorder;
        int rStartPreorder = startPreorder + breakLength + 1;
        int rEndPreorder = endPreorder;

        root.value = preorder[startPreorder];
        root.left = breakLength == 0 ? null :
                rebuildTree(preorder, lStartPreorder, lEndPreorder, inorder, lStartInorder, lEndInorder);
        root.right = (breakLength == endInorder - startInorder) ? null :
                rebuildTree(preorder, rStartPreorder, rEndPreorder, inorder, rStartInorder, rEndInorder);
        return root;
    }
    private static int getBreakLength(int[] preorder, int startPreorder, int[] inorder, int startInorder,
                                    int endInorder) {
        for (int i = startInorder; i < endInorder + 1; i++) {
            if(inorder[i] == preorder[startPreorder])
                return i - startInorder;
        }
        return -1;
    }

}
