package top.cary61.carycode.commons.entity;

import java.io.File;

public class Delimiter {
    public static final String EMPTY_STRING = "";
    public static final String SINGLE_SPACE = " ";
    public static final String TAB = "\t";
    public static final String CR = "\r";
    public static final String LF = "\n";
    public static final String CRLF = CR + LF;
    public static final String LINE_SEPARATOR = System.lineSeparator();
    public static final String SLASH = "/";
    public static final String BACK_SLASH = "\\";
    public static final String PATH_SEPARATOR = File.pathSeparator;
    public static final String COMMA = ",";
    public static final String COMMA_FOLLOWED_BY_A_SPACE = ", ";

}