package com.label.rubblelabeltool.controller;

import com.alibaba.fastjson.JSONArray;
import com.label.rubblelabeltool.config.ImageConfigurer;
import com.label.rubblelabeltool.config.ImgMode;
import com.label.rubblelabeltool.controller.body.*;
import com.label.rubblelabeltool.controller.ex.*;
import com.label.rubblelabeltool.entity.ImageEntity;
import com.label.rubblelabeltool.entity.ImageInfoEntity;
import com.label.rubblelabeltool.entity.PointsEntity;
import com.label.rubblelabeltool.service.IImageService;
import com.label.rubblelabeltool.service.IPointsService;
import com.label.rubblelabeltool.service.ex.ImageInfoEmptyException;
import com.label.rubblelabeltool.util.CvTools;
import com.label.rubblelabeltool.util.FileUtils;
import com.label.rubblelabeltool.util.JsonResult;
import com.label.rubblelabeltool.util.PageUtil;
import io.swagger.annotations.*;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/imgs")
public class ImageController extends BaseController{
    @Autowired(required = false)
    private IImageService iImageService;

    @Autowired(required = false)
    private IPointsService iPointsService;

    private ImageEntity image = null;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");

    /**
     * 功能：显示所有文件夹
     * 请求路径： /imgs/directories
     * 请求参数：
     * 请求类型： GET
     * 请求结果： JsonResult<List<DirectoryBody>>
     */
    @GetMapping("directories")
    @ApiOperation("显示所有文件夹")
    public JsonResult<List<DirectoryBody>> listDirectory(){
        List<DirectoryBody> directoryBodyList = new ArrayList<>();
        for(int i = 0; i <= 4; i++) {
            DirectoryBody directoryBody = new DirectoryBody();
            directoryBody.setDirId(i);
            ImgMode imgMode = ImageConfigurer.getImgMode(i);
            directoryBody.setDirName(imgMode.name());
            String imgDirStr = ImageConfigurer.getImgDir(i);
            // 获取修改时间和文件夹大小
            String fileTime = FileUtils.getFileTime(imgDirStr);
            File imgDir = new File(imgDirStr);
            Double fileSizes = (double) FileUtils.getFileSizes(imgDir);
            directoryBody.setLastEditDate(fileTime);
            directoryBody.setDirSize(fileSizes);
        }
        return new JsonResult<List<DirectoryBody>>(OK, directoryBodyList);
    }

    /**
     * 功能：接收原图和冰雪覆盖图
     * 请求路径： /imgs/upload
     * 请求参数： HttpSession session,
     *          MultipartFile file,
     *          ImageEntity imageEntity
     * 请求类型： POST
     * 请求结果： JsonResult<Void>
     */
    @PostMapping (value = "upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiImplicitParam(name = "img_mode", value = "图片模式（0-原生rgb图， 1-原生ice图）")
    @ApiOperation("接收原图、冰雪覆盖图")
    public JsonResult<Void> uploadImage(@RequestParam("image") MultipartFile[] images,
                                        @RequestParam("img_mode") Integer imgModeInt) {
        for(MultipartFile image: images) {
            // 判断上传的图片是否为空
            if (image.isEmpty()) {
                throw new FileEmptyException("上传的图片不允许为空");
            }
            // 判断上传文件大小是否超过限制
            if (image.getSize() > ImageConfigurer.IMAGE_MAX_SIZE) {
                throw new FileOutOfSizeException("不允许上传超过" + (ImageConfigurer.IMAGE_MAX_SIZE / 1024) + "KB的文件");
            }
            System.out.println(image.getContentType());
            // 判断上传文件类型是否超出限制
            if (!ImageConfigurer.IMAGE_TYPE.contains(image.getContentType())) {
                throw new FileTypeException("不支持使用该类型的图片");
            }
            String imageName = image.getOriginalFilename().substring(0, image.getOriginalFilename().lastIndexOf("."));

            System.out.println(imageName);

            String suffix = ImageConfigurer.getImgSuffix(image.getContentType());
            String dirPath = null;
            if(suffix == null) {
                throw new FileStateException("文件后缀名为空或不符合要求");
            }
            ImageInfoEntity imageInfo = iImageService.getImageInfoByFileName(imageName);
            // 若图片信息为空，说明是新上传的文件，则使用insert，否则使用update
            if(imageInfo == null) {
                // 获取上传日期
                Date uploadTime = new Date();
                // 获取文件夹，例如：{basepath}/raw/raw_rgb/xx.png
                dirPath = ImageConfigurer.getImgDir(imgModeInt);
                // 更新数据库
                iImageService.uploadImage(imageName, uploadTime, imgModeInt, dirPath + imageName + suffix);
            } else {
                dirPath = ImageConfigurer.getImgDir(imgModeInt);
                // 更新数据库
                iImageService.updateImage(imageInfo, dirPath + imageName + suffix, imgModeInt);
            }
            // 创建文件夹
            File folder = new File(dirPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            // 文件保存
            try {
                image.transferTo(new File(folder, imageName + suffix));
            } catch (IOException e) {
                throw new FileUploadIOException("文件保存失败");
            }
        }
        return new JsonResult<Void>(OK);
    }

    /**
     * 功能：图片列表界面
     * 请求路径： /imgs/list
     * 请求参数： Integer page
     *          Integer limit
     *          Integer is_paged
     * 请求类型： GET
     * 请求结果： JsonResult<PageResultBody>
     */
    @GetMapping("list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "params", value = "分页需要的开始位置以及每页大小"),
            @ApiImplicitParam(name = "is_paged", value = "是否要分页，是为1，否为0，默认不分页")
    })
    @ApiOperation("图片列表界面，当不需要分页时，传递page为0，limit为0")
    public JsonResult<PageResultBody> listImages(@RequestParam Map<String, Object> params,
                                                 @RequestParam("is_paged") Integer isPaged) {
        if(isPaged == 1 && ("0".equals(params.get("page")) || "0".equals(params.get("limit")))) {
            return  null;
        }
        PageUtil pageUtil = new PageUtil(params, isPaged);
        PageResultBody imageInfos = iImageService.getImageInfos(pageUtil);
        return new JsonResult<PageResultBody>(OK, imageInfos);
    }

    /**
     * 功能：当打开编辑或者标注页面时返回四种图像以及点集
     * 请求路径： /imgs/edit
     * 请求参数： img_id 图片id
     * 请求类型： GET
     * 请求结果： JsonResult<InitializationBody>
     */
    @GetMapping(value = {"edit", "label"})
    @ApiImplicitParam(name = "img_id", value = "图片id")
    @ApiOperation("当打开编辑页面或者打开标注页面时，返回4种图片（如果有的话），点集以及点集id")
    public JsonResult<InitializationBody> editImage(@RequestParam("img_id") Integer imageId) {
        if(imageId == null) {
            throw new ParameterErrorException("未接收到图片id");
        }
        // 获取图片信息
        ImageInfoEntity imageInfo = iImageService.getImageInfoById(imageId);
        if(imageInfo == null) {
            throw new ImageInfoEmptyException("服务器不存在该图片信息");
        }
        InitializationBody initialization = new InitializationBody();
        // 填充图片
        String rawRgbPath = imageInfo.getRawRgbPath();
        String rawIcePath = imageInfo.getRawIcePath();
        if(rawRgbPath == null || rawIcePath == null) {
            throw new FileEmptyException("服务器不存在原始rgb或ice图的信息，请重新上传");
        }
        try {
            BufferedImage rawRgbImg = ImageIO.read(new File(rawRgbPath));
            // 判断是否为8bitRGB或ARGB
            Integer type = rawRgbImg.getType();
            if(type != BufferedImage.TYPE_INT_RGB && type != BufferedImage.TYPE_3BYTE_BGR && type != BufferedImage.TYPE_INT_ARGB) {
                throw new FileStateException("后端图片格式错误，无法正确解析图片，请重新上传");
            }
            // 获取文件类型的字符串形式
            String typeStr = ImageConfigurer.getImgTypeStr(type);
            String rawRgbBase64 = CvTools.BufferedImageToBase64(rawRgbImg, typeStr);
            initialization.setRawRgbImg(rawRgbBase64);
            // 判断是否标注过
            if(imageInfo.isLabeled()) {
                // 填充三种图像
                // ice
                String icePath = imageInfo.getIcePath();
                BufferedImage iceImg = ImageIO.read(new File(icePath));
                Integer iceType = iceImg.getType();
                if(iceType != BufferedImage.TYPE_INT_RGB && iceType != BufferedImage.TYPE_3BYTE_BGR && iceType != BufferedImage.TYPE_INT_ARGB) {
                    throw new FileStateException("后端图片格式错误，无法正确解析图片，请重新上传");
                }
                String iceTypeStr = ImageConfigurer.getImgTypeStr(iceType);
                String iceBase64 = CvTools.BufferedImageToBase64(iceImg, iceTypeStr);
                initialization.setIceImg(iceBase64);
                // binary
                String biPath = imageInfo.getBiPath();
                BufferedImage biImg = ImageIO.read(new File(biPath));
                Integer biType = biImg.getType();
                if(biType != BufferedImage.TYPE_INT_RGB && biType != BufferedImage.TYPE_3BYTE_BGR && biType != BufferedImage.TYPE_INT_ARGB) {
                    throw new FileStateException("后端图片格式错误，无法正确解析图片，请重新上传");
                }
                String biTypeStr = ImageConfigurer.getImgTypeStr(biType);
                String biBase64 = CvTools.BufferedImageToBase64(biImg, biTypeStr);
                initialization.setBiImg(biBase64);
                // highLight
                String highLightPath = imageInfo.getHighLightPath();
                BufferedImage highLighImg = ImageIO.read(new File(highLightPath));
                Integer highLightType = highLighImg.getType();
                if(highLightType != BufferedImage.TYPE_INT_RGB && highLightType != BufferedImage.TYPE_3BYTE_BGR && highLightType != BufferedImage.TYPE_INT_ARGB) {
                    throw new FileStateException("后端图片格式错误，无法正确解析图片，请重新上传");
                }
                String highLightTypeStr = ImageConfigurer.getImgTypeStr(highLightType);
                String highLightBase64 = CvTools.BufferedImageToBase64(highLighImg, highLightTypeStr);
                initialization.setHighLightImg(highLightBase64);
            } else {
                // 填充原始ice
                BufferedImage rawIceImg = ImageIO.read(new File(rawIcePath));
                Integer rawIceType = rawIceImg.getType();
                if(rawIceType != BufferedImage.TYPE_INT_RGB && rawIceType != BufferedImage.TYPE_3BYTE_BGR && rawIceType != BufferedImage.TYPE_INT_ARGB) {
                    throw new FileStateException("后端图片格式错误，无法正确解析图片，请重新上传");
                }
            }
        } catch (IOException e) {
            throw new FileEmptyException("图片信息存在，但无法找到该图片，请重新上传");
        }
        // 填充点集id列表和点集列表
        List<PointsEntity> oldPointsList = iPointsService.getPointsListByImgId(imageId);
        List<PointsEntity> pointsList = new ArrayList<>();
        List<Integer> pointsIds = new ArrayList<>();
        for(PointsEntity points: oldPointsList){
            Integer id = points.getId();
            pointsIds.add(id);
            // 将str类型的点集转化位List
            JSONArray array = JSONArray.parseArray(points.getPointsStr());
            List<Double[]> pointList = array.toJavaList(Double[].class);
            points.setPointList(pointList);
            pointsList.add(points);
        }
        initialization.setPoints(pointsList);
        initialization.setPids(pointsIds);
        return new JsonResult<InitializationBody>(OK, initialization);
    }

    /**
     * 功能：获取上传的点集，经过处理后得到黑白图、高亮图和冰雪覆盖图并使用json格式回传
     * 请求路径： /imgs/submit
     * 请求参数： PointsEntriesBody pointsEntriesBody,
     *          HttpSession session
     * 请求类型： POST
     * 请求结果： JsonResult<ChangeBody>
     */
    @PostMapping("submit")
    @ApiImplicitParam(name = "pointsEntriesBody", value = "点集的集合以及图片id", allowMultiple = true, dataType = "PointsEntriesBody")
    @ApiOperation("获取上传的点集，经过处理后得到黑白图、高亮图和冰雪覆盖图并使用json格式回传")
    public JsonResult<ChangeBody> submitImage(@RequestBody PointsEntriesBody pointsEntriesBody, HttpSession session) {
//        ImageEntity image = new ImageEntity();
//        Integer imgId = pointsEntriesBody.getImageId();
//        image.setId(imgId);
//        // 从本地文件中获取图片
//        ImageInfoEntity imageInfo = iImageService.getImageInfoById(imgId);
//        String rawRgbPath = imageInfo.getRawRgbPath();
//        String rawIcePath = imageInfo.getRawIcePath();
//        if(rawRgbPath == null || rawIcePath == null) {
//            throw new ImageInfoErrorException("服务器不存在rgb或ice图的信息，请重新上传");
//        }
//        BufferedImage rgbImg;
//        BufferedImage iceImg;
//        try {
//            rgbImg = ImageIO.read(new File(rawRgbPath));
//            iceImg = ImageIO.read(new File(rawIcePath));
//        } catch (IOException e) {
//            throw new FileEmptyException("图片信息存在，但无法找到该图片，请重新上传");
//        }
//        // 判断是否为8bitRGB，是为1或5
//        if((rgbImg.getType() != 1 && rgbImg.getType() != 5) || (iceImg.getType() != 1 && iceImg.getType() != 5)) {
//            throw new FileStateException("后端图片格式错误，无法正确解析图片，请重新上传");
//        }
//        // 将jpg转化为矩阵
//        Mat highLightMat = CvTools.JPEGToMat(rgbImg);
//        Mat iceMat = CvTools.JPEGToMat(iceImg);
//        // 向image注入冰雪覆盖图和高亮图的矩阵
//        image.setHighLightImg(highLightMat);
//        image.setIceImg(iceMat);
//        // 获取图片高和宽
//        int height = rgbImg.getHeight();
//        int width = rgbImg.getWidth();
//        image.setHeight((double)height);
//        image.setWidth((double)width);
        Integer imageId = pointsEntriesBody.getImageId();
        if(image == null || !image.getId().equals(imageId)) {
            throw new CacheNotFoundException("后端不存在该图片的缓存，请重新进入编辑界面");
        }
        List<PointsEntity> pointsList = pointsEntriesBody.getPoints();
        // 保存点集到数据库中
        for(PointsEntity points: pointsList) {
            iPointsService.storePoints(points);
        }
        // 保存到session中，方便后续回显
        session.setAttribute("pointsList", pointsList);
        ImageEntity newImage = iImageService.changeImage(image, pointsList);
        // highlight
        Mat highLightMat = newImage.getHighLightImg();
        // 将图片以base64编码格式转化为json格式数据返回
        ChangeBody changeBody = new ChangeBody();
        // ice
        Mat iceMat = newImage.getIceImg();
        String iceStr = CvTools.matToBase64(iceMat);
        changeBody.setIceImg(iceStr);
        // binary
        Mat biMat = newImage.getBiImg();
        String biStr = CvTools.matToBase64(biMat);
        changeBody.setBiImg(biStr);
        // 将点集id的集合存到changeBody中
        List<Integer> pids = iPointsService.getPointsIdsByImgId(imageId);

        // 将图片保存到文件夹中
        String fileName = image.getFileName();
        // 获取标注完成时间
        Date labeledTime = new Date();
        String formatDate;
        String suffix = ".png";
        // 保存ice
        String iceDirPath = ImageConfigurer.getLabeledImgDir(ImageConfigurer.ICE);
        // 创建文件夹
        File iceFolder = new File(iceDirPath);
        if(!iceFolder.exists()) {
            iceFolder.mkdirs();
        }
        if(!Imgcodecs.imwrite(iceDirPath + fileName + suffix, iceMat) ) {
            throw new FileUploadIOException("文件保存失败");
        }
        // 保存binary
        String biDirPath = ImageConfigurer.getLabeledImgDir(ImageConfigurer.BINARY);
        // 创建文件夹
        File biFolder = new File(biDirPath);
        if(!biFolder.exists()) {
            biFolder.mkdirs();
        }
        if(!Imgcodecs.imwrite(biDirPath + fileName + suffix, biMat) ) {
            throw new FileUploadIOException("文件保存失败");
        };
        // 保存high_light
        String highLightDirPath = ImageConfigurer.getLabeledImgDir(ImageConfigurer.HIGHLIGHT);
        // 创建文件夹
        File highLightFolder = new File(highLightDirPath);
        if(!highLightFolder.exists()) {
            highLightFolder.mkdirs();
        }
        if(!Imgcodecs.imwrite(highLightDirPath + fileName + suffix, highLightMat)){
            throw new FileUploadIOException("文件保存失败");
        }
        // 将信息填入数据库
        iImageService.storeImageInfo(imageId, biDirPath + fileName + suffix,
                iceDirPath + fileName + suffix, highLightDirPath + fileName + suffix, labeledTime);
        // 清除image的缓存
        image = null;
        return new JsonResult<ChangeBody>(OK, changeBody);
    }

    /**
     * 功能：根据图片id删除图片，并重定向到list
     * 请求路径： /imgs/delete
     * 请求参数： Integer img_id
     * 请求类型： GET
     * 请求结果： JsonResult<Void>
     */
    @GetMapping("delete")
    @ApiImplicitParam(name = "img_id", value = "图片id")
    @ApiOperation("根据图片id删除图片，并重定向到list")
    public JsonResult<Void> deleteImage(@RequestParam("img_id") Integer imgId,
                                        HttpServletResponse servletResponse) throws IOException {
        Integer count = iImageService.deleteImageInfoById(imgId);
        if(count == 1) {
            servletResponse.sendRedirect("list");
        }
        return new JsonResult<Void>(DELETE_ERROR);
    }

    @GetMapping("download")
    @ApiOperation("下载固定类型的图片,0->rgb, 2->黑白, 3->ice, 4->高亮")
    public JsonResult<List<DownloadBody>> downloadImages(@RequestParam("img_id") List<Integer> imgIds,
                                                         @RequestParam("img_mode") Integer imgModeInt) {
        String imgDir;
        if(imgModeInt == 0) {
            imgDir = ImageConfigurer.getImgDir(imgModeInt);
        } else {
            imgDir = ImageConfigurer.getLabeledImgDir(imgModeInt);
        }
        List<ImageInfoEntity> imageInfos = iImageService.getImageInfosByIdList(imgIds);
        List<DownloadBody> downloadBodyList = new ArrayList<>();
        for(ImageInfoEntity imageInfo : imageInfos){
            DownloadBody downloadBody = new DownloadBody();
            downloadBody.setImageId(imageInfo.getId());
            String fileName = imageInfo.getFileName();
            String type = imageInfo.getType();
            String suffix = ImageConfigurer.getImgSuffix(type);
            String imgPath = imgDir + fileName + suffix;
            try {
                BufferedImage image = ImageIO.read(new File(imgPath));
                Integer typeInt = image.getType();
                if(typeInt != BufferedImage.TYPE_INT_RGB && typeInt != BufferedImage.TYPE_3BYTE_BGR && typeInt != BufferedImage.TYPE_INT_ARGB) {
                    throw new FileTypeException("后端图片格式错误");
                }
                String imgType = ImageConfigurer.getImgTypeStr(typeInt);
                String imageStr = CvTools.BufferedImageToBase64(image, imgType);
                downloadBody.setImgBase64(imageStr);
            } catch (IOException e) {
                throw new FileEmptyException("图片信息存在，但无法找到该图片，图片名字为：" + fileName + suffix);
            }
            downloadBodyList.add(downloadBody);
        }
        return new JsonResult<List<DownloadBody>>(OK, downloadBodyList);
    }
//    /**
//     * 功能：保存标注完成的图片到后端
//     * 请求路径：/imgs/store
//     * 请求参数：Integer id
//     * 请求类型： GET
//     * 请求结果： JsonResult<Void>
//     */
//    @GetMapping("store")
//    @ApiImplicitParam(name = "id", value = "图片id", required = true)
//    @ApiOperation("保存标注完成的图片到后端")
//    private Boolean storeImages(ImageEntity image) {
//        if(image == null) {
//            throw new CacheNotFoundException("服务器未存储该图片的缓存");
//        }
//        String fileName = image.getFileName();
//        // 获取标注完成时间
//        Date labeledTime = new Date();
//        String formatDate;
//        synchronized (sdf) {
//            formatDate = sdf.format(labeledTime);
//        }
//        String suffix = ".jpg";
//        // 保存ice
//        Mat iceMat = image.getIceImg();
//        String iceDirPath = ImageConfigurer.getLabeledImgDir(formatDate, ImageConfigurer.ICE);
//        // 创建文件夹
//        File iceFolder = new File(iceDirPath);
//        if(!iceFolder.exists()) {
//            iceFolder.mkdirs();
//        }
//        if(!Imgcodecs.imwrite(iceDirPath + fileName + suffix, iceMat) ) {
//            throw new FileUploadIOException("文件保存失败");
//        }
//        // 保存binary
//        Mat biMat = image.getBiImg();
//        String biDirPath = ImageConfigurer.getLabeledImgDir(formatDate, ImageConfigurer.BINARY);
//        // 创建文件夹
//        File biFolder = new File(biDirPath);
//        if(!biFolder.exists()) {
//            biFolder.mkdirs();
//        }
//        if(!Imgcodecs.imwrite(biDirPath + fileName + suffix, biMat) ) {
//            throw new FileUploadIOException("文件保存失败");
//        };
//        // 保存high_light
//        Mat highLightMat = image.getHighLightImg();
//        String highLightDirPath = ImageConfigurer.getLabeledImgDir(formatDate, ImageConfigurer.HIGHLIGHT);
//        // 创建文件夹
//        File highLightFolder = new File(highLightDirPath);
//        if(!highLightFolder.exists()) {
//            highLightFolder.mkdirs();
//        }
//        if(!Imgcodecs.imwrite(highLightDirPath + fileName + suffix, highLightMat)){
//            throw new FileUploadIOException("文件保存失败");
//        }
//        // 将信息填入数据库
//        iImageService.storeImageInfo(id, biDirPath + fileName + suffix,
//                iceDirPath + fileName + suffix, highLightDirPath + fileName + suffix, labeledTime);
//        // 清除image的缓存
//        image = null;
//        return true;
//    }
}
