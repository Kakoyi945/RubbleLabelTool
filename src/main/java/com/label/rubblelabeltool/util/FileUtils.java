package com.label.rubblelabeltool.util;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FileUtils {

    /**
     * 获取文件修改时间
     * @Title: getFileTime
     * @param filepath 文件路径
     * @return String 文件修改时间
     * @author projectNo
     */
    public static String getFileTime(String filepath){
        File f = new File(filepath);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        long time = f.lastModified();
        cal.setTimeInMillis(time);
        return formatter.format(cal.getTime());
    }

    /**
     * 获取文件大小
     * @Title: getFileSizes
     * @param file 文件
     * @return String 转换后的文件大小
     * @author projectNo
     *
     */
    public static long getFileSizes(final File file) {
        if (file.isFile())
            return file.length();
        final File[] children = file.listFiles();
        long total = 0;
        if (children != null)
            for (final File child : children)
                total += getFileSizes(child);
        return total;
    }

    /**
     * 递归获取目录大小
     * @Title: getFileSize
     * @param f
     * @return long 目录大小
     * @author projectNo
     * @throws Exception
     *
     */
    public static long getFileSize(File f)throws Exception
    {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++)
        {
            if (flist[i].isDirectory())
            {
                size = size + getFileSize(flist[i]);
            } else
            {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     * @Title: FormetFileSize
     * @param fileS 文件夹总大小
     * @return String 文件大小
     * @author projectNo
     *
     */
    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        DecimalFormat d = new DecimalFormat("#");
        String fileSizeString = "";
        if(fileS < 1024){
            fileSizeString = d.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) +"GB";
        }
        return fileSizeString;
    }

}
