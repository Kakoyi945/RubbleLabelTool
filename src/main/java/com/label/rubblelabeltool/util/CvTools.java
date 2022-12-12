package com.label.rubblelabeltool.util;

import com.label.rubblelabeltool.config.ImageConfigurer;
import com.label.rubblelabeltool.config.ImgMode;
import com.label.rubblelabeltool.entity.PointsEntity;
import com.label.rubblelabeltool.util.ex.ImageNotFoundException;
import com.label.rubblelabeltool.util.ex.MatEmptyException;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.net.URL;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class CvTools {
    /**
     * 表示行、列的缩放尺寸
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
        rowScaler = (times == 0) ? width : width/times;
        colScaler = (times == 0) ? height : height/times;
    }

//    /**
//     * 加载opencv库的dll文件
//     */
//    static {
//        System.setProperty("java.awt.headless", "false");
//        System.out.println(System.getProperty("java.library.path"));
//        URL url = ClassLoader.getSystemResource("dlls/opencv_java460.dll");
//        System.load(url.getPath());
//    }

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
        } else {
            for(int i = 0; i < mops.size(); i++) {
                Imgproc.drawContours(mat, mops, i, new Scalar(255,255,255), Imgproc.FILLED);
            }
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
                Double colPoint = colPoints.get(i) * colScaler;
                Double rowPoint = rowPoints.get(i) * rowScaler;
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
        Integer cvType = 0;
        switch (image.getType()) {
            case BufferedImage.TYPE_3BYTE_BGR:
                BufferedImage rgbImage = convertTo3ByteRGBType(image);
                cvType = CvType.CV_8UC3;
                break;
            case BufferedImage.TYPE_INT_RGB:
                cvType = CvType.CV_8UC3;
                break;
            case BufferedImage.TYPE_INT_ARGB:
                cvType = CvType.CV_8UC4;
                break;
            case BufferedImage.TYPE_INT_ARGB_PRE:
                cvType = CvType.CV_8UC4;
                break;
            case BufferedImage.TYPE_BYTE_GRAY:
                cvType = CvType.CV_8UC1;
                break;
            default:
                cvType = -1;
        }
        if(cvType == -1) {
            return null;
        }
        Mat mat = new Mat(image.getHeight(), image.getWidth(), cvType);
        byte[] data = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
        mat.put(0,0,data);
        System.out.println(mat.size());
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
     * 将rgb的图片转化为bgr
     * @param image
     * @return
     */
    public static BufferedImage convertTo3ByteBGRType(BufferedImage image) {
        BufferedImage convertedImage = new BufferedImage(image.getWidth(),image.getHeight(),
                BufferedImage.TYPE_3BYTE_BGR);
        convertedImage.getGraphics().drawImage(image, 0, 0, null);
        return convertedImage;
    }

    /**
     * 将
     * @param image
     * @return
     */
    public static BufferedImage convertTo3ByteRGBType(BufferedImage image) {
        BufferedImage convertedImage = new BufferedImage(image.getWidth(),image.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        convertedImage.getGraphics().drawImage(image, 0, 0, null);
        return convertedImage;
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
