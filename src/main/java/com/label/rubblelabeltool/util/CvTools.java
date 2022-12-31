package com.label.rubblelabeltool.util;

import com.label.rubblelabeltool.config.ImageConfigurer;
import com.label.rubblelabeltool.config.ImgMode;
import com.label.rubblelabeltool.entity.PointsEntity;
import com.label.rubblelabeltool.util.ex.ImageNotFoundException;
import com.label.rubblelabeltool.util.ex.MatEmptyException;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.net.URL;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class CvTools {
    /**
     * rowScaler: y轴缩放比例
     * colScaler: x轴缩放比例
     */
    private static Double rowScaler;
    private static Double colScaler;

    /**
     * 计算行、列缩放尺寸
     * @param width 行长度
     * @param height 列长度/高度
     * @param times 倍数
     */
    public static void calScaler(Double width, Double height, Integer times) {
        colScaler = (times == 0) ? width : width/times;
        rowScaler = (times == 0) ? height : height/times;
    }


    /**
     * 根据点集生成图片
     * @param mat 图片
     * @param mops 点集
     * @param imgMode 图片模式
     */
    public static void fillArea(Mat mat, List<MatOfPoint> mops, ImgMode imgMode) {
        if(mat.empty()) {
            throw new MatEmptyException("所传送的图像矩阵为空");
        }
        if(imgMode == ImgMode.BINARY) {
            for(int i = 0; i < mops.size(); i++) {
                Imgproc.drawContours(mat, mops, i, new Scalar(255), Imgproc.FILLED);
            }
//            Imgproc.fillPoly(mat, mops, new Scalar(255), Imgproc.FILLED);
        } else {
            for(int i = 0; i < mops.size(); i++) {
                Imgproc.drawContours(mat, mops, i, new Scalar(255, 255, 255, 255), Imgproc.FILLED);
            }
//            Imgproc.fillPoly(mat, mops, new Scalar(255,255,255,255), Imgproc.FILLED);
        }
    }

    /**
     * 将前端传过来的点集转化为List<MatOfPoint>，以便cv算法使用
     * @param ptes
     * @return List<MatOfPoint>
     */
    public static List<MatOfPoint> changePtsToMops(List<PointsEntity> ptes) {
        // 如果未初始化calScaler或者rowScaler，则返回空值，由service抛出异常
        if(colScaler == null || rowScaler == null) {
            return null;
        }
        List<MatOfPoint> mops = new LinkedList<>();
        for(PointsEntity pte : ptes) {
            List<Double> rowPoints = pte.getRowPoints();
            List<Double> colPoints = pte.getColPoints();
            MatOfPoint mop = new MatOfPoint();
            List<Point> pts = new LinkedList<>();
            for(int i = 0; i < rowPoints.size(); i++) {
                // 将坐标进行放缩
                double colPoint = colPoints.get(i) * colScaler;
                double rowPoint = rowPoints.get(i) * rowScaler;
                Point point = new Point(colPoint, rowPoint);
                pts.add(point);
            }
            mop.fromList(pts);
            mops.add(mop);
        }
        return mops;
    }

    /**
     * 将jpg格式图像转化为矩阵类型图像
     * @param image
     * @return
     */
    public static Mat ImageToMat(BufferedImage image) {
        if(image == null) {
            throw new ImageNotFoundException("生成图片矩阵时发生错误：图片不存在");
        }
        // Integer cvType = 0;
        Mat mat;
        switch (image.getType()) {
            case BufferedImage.TYPE_3BYTE_BGR:
                mat = bgrToMat(image);
                break;
            case BufferedImage.TYPE_INT_RGB:
                BufferedImage bgr = convertToBGRType(image);
                mat = bgrToMat(bgr);
                break;
            case BufferedImage.TYPE_INT_ARGB:
            case BufferedImage.TYPE_INT_ARGB_PRE:
                mat = abgrToMat(convertToBGRType(image));
                break;
            case BufferedImage.TYPE_BYTE_GRAY:
                mat = grayToMat(image);
                break;
            case BufferedImage.TYPE_4BYTE_ABGR:
                mat = abgrToMat(image);
                break;
            default:
                return null;
        }
        return mat;
    }

    private static Mat grayToMat(BufferedImage image){
        Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC1);
        byte[] data = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
        mat.put(0,0,data);
        return mat;
    }

    private static Mat abgrToMat(BufferedImage image){
        // 转化为bgra
        Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC4);
        byte[] data = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
        for(int i = 0; i < data.length; i += 4){
            byte tmp = data[i];
            data[i] = data[i+1];
            data[i+1] = data[i+2];
            data[i+2] = data[i+3];
            data[i+3] = tmp;
        }
        mat.put(0,0,data);
        return mat;
    }

    private static Mat bgrToMat(BufferedImage image){
        Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
        mat.put(0,0,data);
        return mat;
    }

    /**
     * 将MultipartFile转化为矩阵类型图像
     * @param file
     * @return
     * @throws IOException
     */
    public static BufferedImage multipartFileToBufferedImage(MultipartFile file){
        BufferedImage bufferedImage;
        try {
            FileInputStream fis = (FileInputStream)file.getInputStream();
            bufferedImage = ImageIO.read(fis);
            return bufferedImage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将argb/rgb的图片转化为abgr/bgr
     * @param image
     * @return
     */
    public static BufferedImage convertToBGRType(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        // 获取图片中每个rgb像素的int类型表示
        int[] rgbPixels = image.getRGB(0, 0, width, height, null, 0, width);
        int[] bgrPixels = new int[rgbPixels.length];
        for (int i = 0; i < rgbPixels.length; i++) {
            int color = rgbPixels[i];
            int red = ((color & 0x00FF0000) >> 16);
            int green = ((color & 0x0000FF00) >> 8);
            int blue = color & 0x000000FF;
            // 将rgb三个通道的数值合并为一个int数值，同时调换b通道和r通道
            bgrPixels[i] = (red & 0x000000FF) | (green << 8 & 0x0000FF00) | (blue << 16 & 0x00FF0000);
        }
        image.setRGB(0, 0, width, height, bgrPixels, 0, width);
        return image;
    }

    /**
     * 将矩阵格式的图片转化为base64格式
     * @param mat 矩阵
     * @return String base64格式文件
     */
    public static String matToBase64(Mat mat, String imgType){
        imgType = "." + imgType;
        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(imgType, mat, mob);
        Base64.Encoder encoder = Base64.getEncoder();
        String imgBase64 = encoder.encodeToString(mob.toArray());
        return imgBase64;
    }

    public static BufferedImage base64ToImage(String base64) throws IOException {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] origin = decoder.decode(base64);
        InputStream in = new ByteArrayInputStream(origin);
        BufferedImage image = ImageIO.read(in);
        return image;
    }

    public static String BufferedImageToBase64(BufferedImage bufferedImage, String typeStr) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, typeStr, baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();
        Base64.Encoder encoder = Base64.getEncoder();
        String imgStr = encoder.encodeToString(bytes).trim();
        imgStr = imgStr.replaceAll("\n","").replaceAll("\r","");
        return "data:image/" + typeStr + ";base64," + imgStr;
    }
}
