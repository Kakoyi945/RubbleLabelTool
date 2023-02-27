package com.label.rubblelabeltool.util;

import com.label.rubblelabeltool.controller.ex.DeleteFileFailedException;
import com.label.rubblelabeltool.util.ex.FileInfoErrorException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
     * @Title: getDirSize
     * @param f
     * @return long 目录大小
     * @author projectNo
     *
     */
    public static long getDirSize(File f)
    {
        long size = 0;
        try{
            File flist[] = f.listFiles();
            for (int i = 0; i < flist.length; i++)
            {
                if (flist[i].isDirectory())
                {
                    size = size + getDirSize(flist[i]);
                } else
                {
                    size = size + flist[i].length();
                }
            }
        } catch (Exception e){
            throw new FileInfoErrorException("获取文件夹大小时发生错误！");
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

    /**
     * 根据文件路径删除文件
     * @param fileUrl 文件路径
     */
    public static void deleteFile(String fileUrl){
        try{
            Path path = Paths.get(fileUrl);
            Files.delete(path);
        } catch (IOException e) {
            throw new DeleteFileFailedException("删除文件失败");
        }
    }
}
