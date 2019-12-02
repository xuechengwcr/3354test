package cn.projects.team.demo.widget;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class PinyinUtils {

public static String getPingYin(String inputString) {
    HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
    format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
    format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    format.setVCharType(HanyuPinyinVCharType.WITH_V);

    char[] input = inputString.trim().toCharArray();
    String output = "";

    try {
        for (char curChar : input) {
            if (Character.toString(curChar).matches("[\\u4E00-\\u9FA5]+")) {
                String[] temp = PinyinHelper.toHanyuPinyinStringArray(curChar, format);
                output += temp[0];
            } else
                output += Character.toString(curChar);
        }
    } catch (BadHanyuPinyinOutputFormatCombination e) {
        e.printStackTrace();
    }
    return output;
}


        public static String getFirstSpell(String chinese) {
            StringBuffer pinYinBF = new StringBuffer();
            char[] arr = chinese.toCharArray();
            HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
            defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            for (char curChar : arr) {
                if (curChar > 128) {
                    try {
                        String[] temp = PinyinHelper.toHanyuPinyinStringArray(curChar, defaultFormat);
                        if (temp != null) {
                            pinYinBF.append(temp[0].charAt(0));
                        }
                    } catch (BadHanyuPinyinOutputFormatCombination e) {
                        e.printStackTrace();
                    }
                } else {
                    pinYinBF.append(curChar);
                }
            }
            return pinYinBF.toString().replaceAll("\\W", "").trim();
        }


        public static String converterToFirstSpell(String chines) {
            StringBuffer pinyinName = new StringBuffer();
            char[] nameChar = chines.toCharArray();
            HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
            defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            for (int i = 0; i < nameChar.length; i++) {
                if (nameChar[i] > 128) {
                    try {

                        String[] str = PinyinHelper.toHanyuPinyinStringArray(
                                nameChar[i], defaultFormat);
                        if (str != null) {
                            for (int j = 0; j < str.length; j++) {

                                pinyinName.append(str[j].charAt(0));
                                if (j != str.length - 1) {
                                    pinyinName.append(",");
                                }
                            }
                        }

                    } catch (BadHanyuPinyinOutputFormatCombination e) {
                        e.printStackTrace();
                    }
                } else {
                    pinyinName.append(nameChar[i]);
                }
                pinyinName.append(" ");
            }
            // return pinyinName.toString();
            return parseTheChineseByObject(discountTheChinese(pinyinName.toString()));
        }


        public static String converterToSpell(String chines) {
            StringBuffer pinyinName = new StringBuffer();
            char[] nameChar = chines.toCharArray();
            HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
            defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            for (int i = 0; i < nameChar.length; i++) {
                if (nameChar[i] > 128) {
                    try {

                        String[] str = PinyinHelper.toHanyuPinyinStringArray(
                                nameChar[i], defaultFormat);
                        if (str != null) {
                            for (int j = 0; j < str.length; j++) {
                                pinyinName.append(str[j]);
                                if (j != str.length - 1) {
                                    pinyinName.append(",");
                                }
                            }
                        }
                    } catch (BadHanyuPinyinOutputFormatCombination e) {
                        e.printStackTrace();
                    }
                } else {
                    pinyinName.append(nameChar[i]);
                }
                pinyinName.append(" ");
            }
            // return pinyinName.toString();
            return parseTheChineseByObject(discountTheChinese(pinyinName.toString()));
        }


        private static List<Map<String, Integer>> discountTheChinese(String theStr) {
            List<Map<String, Integer>> mapList = new ArrayList<>();
            Map<String, Integer> onlyOne;
            String[] firsts = theStr.split(" ");
            for (String str : firsts) {
                onlyOne = new Hashtable<>();
                String[] china = str.split(",");
                for (String s : china) {
                    Integer count = onlyOne.get(s);
                    if (count == null) {
                        onlyOne.put(s, new Integer(1));
                    } else {
                        onlyOne.remove(s);
                        count++;
                        onlyOne.put(s, count);
                    }
                }
                mapList.add(onlyOne);
            }
            return mapList;
        }


        private static String parseTheChineseByObject(
                List<Map<String, Integer>> list) {
            Map<String, Integer> first = null;
            for (int i = 0; i < list.size(); i++) {
                Map<String, Integer> temp = new Hashtable<>();
                if (first != null) {
                    for (String s : first.keySet()) {
                        for (String s1 : list.get(i).keySet()) {
                            String str = s + s1;
                            temp.put(str, 1);
                        }
                    }
                    if (temp != null && temp.size() > 0) {
                        first.clear();
                    }
                } else {
                    for (String s : list.get(i).keySet()) {
                        String str = s;
                        temp.put(str, 1);
                    }
                }
                if (temp != null && temp.size() > 0) {
                    first = temp;
                }
            }
            String returnStr = "";
            if (first != null) {
                for (String str : first.keySet()) {
                    returnStr += (str + ",");
                }
            }
            if (returnStr.length() > 0) {
                returnStr = returnStr.substring(0, returnStr.length() - 1);
            }
            return returnStr;
        }

}