package com.baqi.str;

import java.util.ArrayList;
import java.util.List;

public class listAdd {
    public static void main(String[] args) {
        List<String> list1 = new ArrayList<>();
        list1.add("a");
        List<String> list2 = new ArrayList<>();
        list2.add("b");
        list1.addAll(list2);
        System.out.println(list1);
    }
}
