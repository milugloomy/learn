package com.baqi.str;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 *      ┌─┐       ┌─┐
 *   ┌──┘ ┴───────┘ ┴──┐
 *   │                 │
 *   │       ───       │
 *   │  ─┬┘       └┬─  │
 *   │                 │
 *   │       ─┴─       │
 *   │                 │
 *   └───┐         ┌───┘
 *       │         │
 *       │         │
 *       │         │
 *       │         └──────────────┐
 *       │                        │
 *       │                        ├─┐
 *       │                        ┌─┘
 *       │                        │
 *       └─┐  ┐  ┌───────┬──┐  ┌──┘
 *         │ ─┤ ─┤       │ ─┤ ─┤
 *         └──┴──┘       └──┴──┘
 *                神兽保佑
 *               代码无BUG!
 */
public class Zhengze {
    public static void main(String[] args) {
        String str = "1762次阅读 9个赞";

        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(str);
        if(m.find()) {
            System.out.println(m.group());
        }
        if(m.find()) {
            System.out.println(m.group());
        }

    }
}
