package cn.projects.team.demo.widget;

import java.util.Comparator;

import cn.projects.team.demo.model.Contact;

public class PinyinComparator implements Comparator<Contact> {

public int compare(Contact o1, Contact o2) {
        if (o1.getLetters().equals("@")
        || o2.getLetters().equals("#")) {
        return 1;
        } else if (o1.getLetters().equals("#")
        || o2.getLetters().equals("@")) {
        return -1;
        } else {
        return o1.getLetters().compareTo(o2.getLetters());
        }
        }

        }
