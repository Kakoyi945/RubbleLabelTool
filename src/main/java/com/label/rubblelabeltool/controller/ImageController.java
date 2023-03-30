package com.label.rubblelabeltool.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import com.label.rubblelabeltool.util.*;
import com.label.rubblelabeltool.util.ex.ImageNotFoundException;
import io.swagger.annotations.*;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("imgs")
public class ImageController extends BaseController{
    @Autowired(required = false)
    private IImageService iImageService;

    @Autowired(required = false)
    private IPointsService iPointsService;

    /**
     * 功能：显示所有文件夹
     * 请求路径： /imgs/directories
     * 请求参数：
     * 请求类型： GET
     * 请求结果： JsonResult<List<DirectoryBody>>
     */
    @GetMapping("directories")
    @ApiOperation("显示所有文件夹")
    public ListResult<DirectoryBody> listDirectory(HttpSession session){
        List<DirectoryBody> directoryBodyList = new ArrayList<>();
        for(int i = 0; i <= 4; i++) {
            // 忽略ice文件夹，因为ice不需要标注
            if(i == 3)
                continue;
            DirectoryBody directoryBody = new DirectoryBody();
            directoryBody.setDirId(i);
            ImgMode imgMode = ImageConfigurer.getImgMode(i);
            directoryBody.setDirName(imgMode.name());
            String imgDirStr = ImageConfigurer.getImgDir(i, "");
            // 获取修改时间和文件夹大小
            String fileTime = FileUtils.getFileTime(imgDirStr);
            File imgDir = new File(imgDirStr);
            Double fileSizes = null;
            fileSizes = (double) FileUtils.getDirSize(imgDir);
            directoryBody.setLastEditDate(fileTime);
            directoryBody.setDirSize(fileSizes);
            directoryBodyList.add(directoryBody);
        }
        // 设置session，用于设置访问顺序，即只有访问过/imgs/diretories界面才可以访问/imgs/list界面
        session.setAttribute("list", "list");
        return new ListResult<DirectoryBody>(OK, directoryBodyList);
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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "img_mode", value = "图片模式（0-原生rgb图， 1-原生ice图）"),
            @ApiImplicitParam(paramType = "form",  name = "image", value = "图片", required = true, dataType = "__file")
    })
    @ApiOperation("接收原图、冰雪覆盖图")
    public ListResult<Integer> uploadImage(@RequestParam("image") MultipartFile[] images,
                                        @RequestParam("img_mode") Integer imgModeInt) {
        List<Integer> imgIdList = new ArrayList<>();
        for(MultipartFile image: images) {
            // 判断上传的图片是否为空
            if (image.isEmpty()) {
                throw new FileEmptyException("上传的图片不允许为空");
            }
            // 判断上传文件大小是否超过限制
            Double size = (double)image.getSize();
            if (size > ImageConfigurer.IMAGE_MAX_SIZE) {
                throw new FileOutOfSizeException("不允许上传超过" + (ImageConfigurer.IMAGE_MAX_SIZE / 1024) + "KB的文件");
            }
            String contentType = image.getContentType();
            // 判断上传文件类型是否超出限制
            if (!ImageConfigurer.IMAGE_TYPE.contains(contentType)) {
                throw new FileTypeException("不支持使用该类型的图片");
            }
            // 获取图片名字以及二级路径（即日期）
            String imageName = image.getOriginalFilename().substring(0, image.getOriginalFilename().lastIndexOf("."));
            String[] strs = imageName.split("_");
            if(strs.length < 6) {
                throw new FileStateException("文件名不符合要求");
            }
            // fileName为保存于数据库中的图片的名字，即截取实际图片名字的日期加时间部分，如：20200101_3000
            String fileName = strs[4]+"_"+strs[5];
            String secDir = strs[4];

            String suffix = ImageConfigurer.getImgSuffix(contentType);
            String dirPath = null;
            if(suffix == null) {
                throw new FileStateException("文件后缀名为空或不符合要求");
            }
            ImageInfoEntity imageInfo = iImageService.getImageInfoByFileName(fileName);
            // 若图片信息为空，说明是新上传的文件，则使用insert，否则使用update
            if(imageInfo == null) {
                // 获取上传日期
                Date uploadTime = new Date();
                // 获取文件夹，例如：{basepath}/rgb/20200101/
                dirPath = ImageConfigurer.getImgDir(imgModeInt, secDir);
                // 更新数据库
                iImageService.uploadImage(fileName, uploadTime, imgModeInt, dirPath + imageName + suffix, contentType);
            } else {
                dirPath = ImageConfigurer.getImgDir(imgModeInt, secDir);
                // 更新数据库
                iImageService.updateImage(imageInfo, dirPath + imageName + suffix, imgModeInt);
            }
            // 创建文件夹
            File folder = new File(dirPath);
            if (!folder.exists()) {
                boolean isSucceed = folder.mkdirs();
                if(!isSucceed) {
                    throw new FileUploadIOException("文件保存时发生错误");
                }
            }
            // 文件保存
            try {
                image.transferTo(new File(folder, imageName + suffix));
            } catch (IOException e) {
                throw new FileUploadIOException("文件保存失败");
            }
            // 获取图像的id，保存到list中
            ImageInfoEntity lastImageInfo = iImageService.getImageInfoByFileName(fileName);
            imgIdList.add(lastImageInfo.getId());
        }
        return new ListResult<Integer>(OK, imgIdList);
    }

    /**
     * 用于根据图片模式获取图片路径
     * 由于该方法在list以及download都需要用到，故单独提出来
     * @param imageInfo 图片信息
     * @param imgModeInt 图片模式（int）
     * @return 图片路径
     */
    private String getImagePathByMode(ImageInfoEntity imageInfo, Integer imgModeInt) {
        String path = null;
        ImgMode imgMode = ImageConfigurer.getImgMode(imgModeInt);
        if(imgMode == ImgMode.RAWRGB) {
            path = imageInfo.getRawRgbPath();
        } else if (imgMode == ImgMode.RAWICE) {
            path = imageInfo.getRawIcePath();
        } else if (imgMode == ImgMode.BINARY) {
            path = imageInfo.getBiPath();
        } else if (imgMode == ImgMode.HIGHLIGHT) {
            path = imageInfo.getHighLightPath();
        }
        return path;
    }

    /**
     * 功能：图片列表界面
     * 请求路径： /imgs/list
     * 请求参数： Integer page
     *          Integer limit
     *          HttpSession session
     * 请求类型： GET
     * 请求结果： JsonResult<PageResultBody>
     */
    @GetMapping("list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "每页多少项", dataType = "int"),
            @ApiImplicitParam(name = "img_mode", value = "要展示什么类型的图片（0为原始rgb，1为原始ice，2为bi，3为ice，4为高亮）", dataType = "int")
    })
    @ApiOperation("图片列表界面，当不需要分页时，传递page为0，limit为0，示例：list/page=1&img_mode=0")
    public JsonResult<PageResultBody> listImages(@RequestParam("page") Integer page,
                                                 @RequestParam("limit") Integer limit,
                                                 @RequestParam("img_mode") Integer imgModeInt,
                                                 HttpSession session) {
        if(page == 0 || limit == 0) {
            return  null;
        }
//        System.out.println("进入list界面");
//        // 当用户进入图片列表界面时，代表该该用户已经编辑完图片，则将图片session信息从imageSessionMap中删除
//        BMap<Integer, String> imageSessionMap = BMap.getImageSessionMap();
//        String sessionId = session.getId();
//        System.out.println("用户" + session.getId() + "释放对图片" + imageSessionMap.getByValue(sessionId) + "的编辑权");
//        if(imageSessionMap.containsValue(sessionId))
//            imageSessionMap.removeByValue(sessionId);

        // 获取图片总数
        Integer totalCount = iImageService.getTotalCountByImgMode(imgModeInt);
        // 获取图片信息列表
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("limit", limit);
        params.put("imgMode", imgModeInt);
        PageUtils pageUtils = new PageUtils(params, 1);
        List<ImageInfoEntity> imageInfos = iImageService.getImageInfos(pageUtils);
        List<PageEntryBody> pageEntries = new ArrayList<>();
        for(ImageInfoEntity imageInfo : imageInfos) {
            String imagePath = getImagePathByMode(imageInfo, imgModeInt);
            File image = new File(imagePath);
            PageEntryBody pageEntry = new PageEntryBody();
            pageEntry.setImgId(imageInfo.getId());
            pageEntry.setFileName(image.getName());
            Double fileSize = (double)FileUtils.getFileSizes(image);
            pageEntry.setSize(fileSize);
            pageEntry.setType(imageInfo.getType());
            pageEntry.setEditTime(imageInfo.getEditTime());
            pageEntries.add(pageEntry);
        }
        PageResultBody pageResultBody = new PageResultBody(pageEntries, totalCount, pageUtils.getLimit(), pageUtils.getPage());
        return new JsonResult<PageResultBody>(OK, pageResultBody);
    }

    /**
     * 获取图片对象，
     * 当flag为0时，填充4种图片（用于edit、submit接口）
     * 当flag为1时，填充原始图片（用于del_points接口）
     * @param imageId 图片id
     * @param flag
     * @return
     */
    private ImageEntity getImage(Integer imageId, Integer flag) {
        // 获取图片信息
        ImageInfoEntity imageInfo = iImageService.getImageInfoById(imageId);
        if(imageInfo == null) {
            throw new ImageInfoEmptyException("服务器不存在该图片信息");
        }
        // 新建image缓存
        ImageEntity image = new ImageEntity();
        // 设置image缓存的id和名字
        image.setId(imageId);
        image.setFileName(imageInfo.getFileName());
        // 填充图片
        String rawRgbPath = imageInfo.getRawRgbPath();
        String rawIcePath = imageInfo.getRawIcePath();
        if(rawRgbPath == null || rawIcePath == null) {
            throw new FileEmptyException("服务器不存在原始rgb或ice图的信息，请重新上传");
        }
        try {
            BufferedImage rawRgbImg = ImageIO.read(new File(rawRgbPath));
            // 填充image缓存的width、height
            image.setWidth((double) rawRgbImg.getWidth());
            image.setHeight((double) rawRgbImg.getHeight());
            // 保存到image缓存中
            Mat rawRgbMat = CvTools.ImageToMat(rawRgbImg);
            image.setRawRgbImg(rawRgbMat);
            // 填充原始ice
            BufferedImage rawIceImg = ImageIO.read(new File(rawIcePath));
            // 将原始ice填充到缓存中
            Mat rawIceMat = CvTools.ImageToMat(rawIceImg);
            image.setRawIceImg(rawIceMat);
            // 获取文件类型的字符串形式
            String rgbImgType = ImageConfigurer.getImgTypeStr(rawRgbImg.getType());
            image.setRgbImgType(rgbImgType);
            String iceImgType = ImageConfigurer.getImgTypeStr(rawIceImg.getType());
            image.setIceImgType(iceImgType);
            // 判断是否标注过
            if (flag == 0 && imageInfo.isLabeled()) {
                // 填充两种图像，并将图像加入缓存中
                // binary
                String biPath = imageInfo.getBiPath();
                BufferedImage biImg = ImageIO.read(new File(biPath));
                Mat biMat = CvTools.ImageToMat(biImg);
                image.setBiImg(biMat);
                // highLight
                String highLightPath = imageInfo.getHighLightPath();
                BufferedImage highLightImg = ImageIO.read(new File(highLightPath));
                Mat highLightMat = CvTools.ImageToMat(highLightImg);
                image.setHighLightImg(highLightMat);
            }
        } catch (IOException e) {
            throw new FileEmptyException("文件不存在");
        }
        return image;
    }

    /**
     * 功能：当打开编辑或者标注页面时返回四种图像以及点集
     * 请求路径： /imgs/edit
     * 请求参数： img_id 图片id
     * 请求类型： GET
     * 请求结果： JsonResult<InitializationBody>
     */
    @GetMapping("edit")
    @ApiImplicitParam(name = "img_id", value = "图片id", dataType = "int")
    @ApiOperation("当打开编辑页面或者打开标注页面时，返回4种图片（如果有的话），点集以及点集id")
    public JsonResult<InitializationBody> editImage(@RequestParam("img_id") Integer imageId,
                                                    HttpSession session) {
        if(imageId == null) {
            throw new ParameterErrorException("未接收到图片id");
        }
//        // 判断该session是否能够访问图片
//        BMap<Integer, String> sessionImageMap = BMap.getImageSessionMap();
//        if(sessionImageMap.containsKey(imageId) && !Objects.equals(sessionImageMap.getByKey(imageId), session.getId()))
//            throw new ImageBeingUsedException("该图片正被其他用户编辑中，请稍后重试");
//        else{
//            // 否则将该图片以及session插入sessionImageMap中
//            sessionImageMap.replace(imageId, session.getId());
//            System.out.println("用户" + session.getId() + "获取对图片" + imageId + "的编辑权");
//        }


//        ImageEntity image = getImage(imageId, 0);
//        InitializationBody initialization = new InitializationBody();
//        Mat rawRgbImg = image.getRawRgbImg();
//        String rawRgbImgStr = CvTools.MatToBase64(rawRgbImg, image.getRgbImgType());
//        initialization.setRawRgbImg(rawRgbImgStr);
//        Mat rawIceImg = image.getRawIceImg();
//        String rawIceImgStr = CvTools.MatToBase64(rawIceImg, image.getIceImgType());
//        initialization.setRawIceImg(rawIceImgStr);
//        Mat biImg = image.getBiImg();
//        if(biImg != null) {
//            String biImgStr = CvTools.MatToBase64(biImg, ImageConfigurer.DEFAULT_IMAGE_TYPE);
//            initialization.setBiImg(biImgStr);
//        }
//        Mat highLightImg = image.getHighLightImg();
//        if(highLightImg != null) {
//            String highLightImgStr = CvTools.MatToBase64(highLightImg, ImageConfigurer.DEFAULT_IMAGE_TYPE);
//            initialization.setHighLightImg(highLightImgStr);
//        }
        // 获取图片信息
        ImageInfoEntity imageInfo = iImageService.getImageInfoById(imageId);
        if(imageInfo == null) {
            throw new ImageInfoEmptyException("服务器不存在该图片信息");
        }
        // 新建image缓存
        ImageEntity image = new ImageEntity();
        // 设置image缓存的id和名字
        image.setId(imageId);
        image.setFileName(imageInfo.getFileName());
        InitializationBody initialization = new InitializationBody();
        // 填充图片
        String rawRgbPath = imageInfo.getRawRgbPath();
        String rawIcePath = imageInfo.getRawIcePath();
        if(rawRgbPath == null || rawIcePath == null) {
            throw new FileEmptyException("服务器不存在原始rgb或ice图的信息，请重新上传");
        }
        try {
            BufferedImage rawRgbImg = ImageIO.read(new File(rawRgbPath));
            // 填充image缓存的width、height
            image.setWidth((double) rawRgbImg.getWidth());
            image.setHeight((double) rawRgbImg.getHeight());
            // 判断是否为jpg或png
            Integer type = rawRgbImg.getType();
            // 获取文件类型的字符串形式
            String typeStr = ImageConfigurer.getImgTypeStr(type);
            // 填充rawRgb
            String rawRgbBase64 = CvTools.BufferedImageToBase64(rawRgbImg, typeStr);
            initialization.setRawRgbImg(rawRgbBase64);
            Mat rawRgbMat = CvTools.ImageToMat(rawRgbImg);
            image.setRawRgbImg(rawRgbMat);
            // 填充ice
            BufferedImage rawIceImg = ImageIO.read(new File(rawIcePath));
            String rawIceBase64 = CvTools.BufferedImageToBase64(rawIceImg, typeStr);
            initialization.setIceImg(rawIceBase64);
            Mat rawIceMat = CvTools.ImageToMat(rawIceImg);
            image.setRawIceImg(rawIceMat);
            image.setIceImg(rawIceMat);
            // 判断是否标注过
            if (imageInfo.isLabeled()) {
                // 填充两种图像，并将图像加入缓存中
                // binary
                String biPath = imageInfo.getBiPath();
                BufferedImage biImg = ImageIO.read(new File(biPath));
                String biBase64 = CvTools.BufferedImageToBase64(biImg, typeStr);
                initialization.setBiImg(biBase64);
                Mat biMat = CvTools.ImageToMat(biImg);
                image.setBiImg(biMat);
                // highLight
                String highLightPath = imageInfo.getHighLightPath();
                BufferedImage highLightImg = ImageIO.read(new File(highLightPath));
                String highLightBase64 = CvTools.BufferedImageToBase64(highLightImg, typeStr);
                initialization.setHighLightImg(highLightBase64);
                Mat highLightMat = CvTools.ImageToMat(highLightImg);
                image.setHighLightImg(highLightMat);

            }
            // 将图片放入缓存中，方便submit时使用，以及拦截器设置
            session.setAttribute("imageCache", image);
        } catch (IOException e) {
            throw new FileEmptyException("文件不存在");
        }
        // 填充点集id列表和点集列表
        List<PointsEntity> oldPointsList = iPointsService.getPointsListByImgId(imageId);
        List<PointsDataBody> pointsDataBodyList = new ArrayList<>();
        for(PointsEntity points: oldPointsList) {
            PointsDataBody pointsData = new PointsDataBody();
            pointsData.setPid(points.getId());
            pointsData.setPoints(points.getPointList());
            pointsDataBodyList.add(pointsData);
        }
        initialization.setPointsData(pointsDataBodyList);
        return new JsonResult<InitializationBody>(OK, initialization);
    }

    /**
     * 由于该方法在delete和submit都需要，故放到此处
     * @param image
     * @param pids
     * @return
     */
    private ChangeBody getChangeBody(ImageEntity image, List<Integer> pids) {

        ChangeBody changeBody = new ChangeBody();
        // highlight
        Mat highLightMat = image.getHighLightImg();
        String highLightStr = CvTools.MatToBase64(highLightMat, ImageConfigurer.DEFAULT_IMAGE_TYPE);
        changeBody.setHighLightImg(highLightStr);
//        // ice
//        Mat iceMat = image.getIceImg();
//        String iceStr = CvTools.MatToBase64(iceMat, "png");
//        changeBody.setIceImg(iceStr);
        // binary
        Mat biMat = image.getBiImg();
        String biStr = CvTools.MatToBase64(biMat, ImageConfigurer.DEFAULT_IMAGE_TYPE);
        changeBody.setBiImg(biStr);
        // 将点集id的集合存到changeBody中
        changeBody.setPids(pids);

        // 将图片保存到文件夹中
        String fileName = image.getFileName();
        // 获取标注时间
        Date labeledTime = new Date();
        String suffix = ".png";
        // 获取二级路径
        String secDir = fileName.split("_")[0];
//        // 保存ice
//        String iceDirPath = ImageConfigurer.getImgDir(ImageConfigurer.ICE);
//        // 创建文件夹
//        File iceFolder = new File(iceDirPath);
//        if(!iceFolder.exists()) {
//            iceFolder.mkdirs();
//        }
//        if(!Imgcodecs.imwrite(iceDirPath + fileName + suffix, iceMat) ) {
//            throw new FileUploadIOException("文件保存失败");
//        }
        // 保存binary
        String biDirPath = ImageConfigurer.getImgDir(ImageConfigurer.BINARY, secDir);
        // 创建文件夹
        File biFolder = new File(biDirPath);
        if(!biFolder.exists()) {
            biFolder.mkdirs();
        }
        if(!Imgcodecs.imwrite(biDirPath + fileName + suffix, biMat) ) {
            throw new FileUploadIOException("文件保存失败");
        };
        // 保存high_light
        String highLightDirPath = ImageConfigurer.getImgDir(ImageConfigurer.HIGHLIGHT, secDir);
        // 创建文件夹
        File highLightFolder = new File(highLightDirPath);
        if(!highLightFolder.exists()) {
            highLightFolder.mkdirs();
        }
        if(!Imgcodecs.imwrite(highLightDirPath + fileName + suffix, highLightMat)){
            throw new FileUploadIOException("文件保存失败");
        }
        // 将信息填入数据库
        iImageService.storeImageInfo(image.getId(), biDirPath + fileName + suffix,
                "", highLightDirPath + fileName + suffix, labeledTime);
        return changeBody;
    }

    /**
     * 功能：获取上传的点集，经过处理后得到黑白图、高亮图和冰雪覆盖图并使用json格式回传
     * 请求路径： /imgs/submit
     * 请求参数： PointsEntriesBody pointsEntriesBody,
     * 请求类型： POST
     * 请求结果： JsonResult<ChangeBody>
     */
    @PostMapping("submit")
    @ApiOperation("获取上传的点集，经过处理后得到黑白图、高亮图并使用json格式回传。")
    public JsonResult<ChangeBody> submitImage(@RequestBody PointsEntriesBody pointsEntriesBody,
                                              HttpSession session) {
//        ImageEntity image = (ImageEntity)session.getAttribute("imageCache");
        Integer imageId = pointsEntriesBody.getImageId();
        ImageEntity image = getImage(imageId, 0);
        List<List<Double[]>> pointsList = pointsEntriesBody.getPoints();
//        if(image == null || !image.getId().equals(imageId)) {
//            throw new CacheNotFoundException("后端不存在该图片的缓存，请重新进入编辑界面");
//        }
        List<PointsEntity> pointsEntityList = new ArrayList<>();
        // 保存点集到数据库中
        for(List<Double[]> points: pointsList) {
            PointsEntity pointsEntity = new PointsEntity();
            pointsEntity.setImgId(imageId);
            pointsEntity.setPointList(points);
            String pointsStr = JSONObject.toJSONString(points);
            pointsEntity.setPointsStr(pointsStr);
            iPointsService.storePoints(pointsEntity);
            pointsEntityList.add(pointsEntity);
        }
        ImageEntity newImage = iImageService.changeImage(image, pointsEntityList);
        List<Integer> pids = iPointsService.getPointsIdsByImgId(imageId);
        ChangeBody changeBody = getChangeBody(newImage, pids);
//        // 更新缓存
//        session.setAttribute("imageCache", newImage);
        return new JsonResult<ChangeBody>(OK, changeBody);
    }



    /**
     * 功能：根据图片id删除图片，并重定向到list
     * 请求路径： /imgs/del_image
     * 请求参数： Integer img_id
     * 请求类型： GET
     * 请求结果： JsonResult<Void>
     */
    @GetMapping("del_image")
    @ApiImplicitParam(name = "img_id", value = "图片id", dataType = "int")
    @ApiOperation("根据图片id和图片模式删除图片，删除图片的规则如下：\n"+
            "如果图片为原始rgb或原始ice，则四种图片都删除；\n" +
            "如果图片为黑白图或高亮图，则同时删除这两张图；\n" +
            "调试时需要先调试list接口")
    public JsonResult<String> deleteImage(@RequestParam("img_id") Integer imgId,
                                        @RequestParam("img_mode") Integer imgModeInt,
                                        HttpSession session,
                                        HttpServletResponse servletResponse) throws IOException {
        // 获取图片路径
        ImageInfoEntity imageInfo = iImageService.getImageInfoById(imgId);
        ImgMode imgMode = ImageConfigurer.getImgMode(imgModeInt);
        // 对于rawrgb和rawice，两个文件需一起删除
        if(imgMode == ImgMode.RAWRGB || imgMode == ImgMode.RAWICE) {
            iImageService.deleteImageInfoById(imgId);
            String rawRgbPath = imageInfo.getRawRgbPath();
            String rawIcePath = imageInfo.getRawIcePath();
            if(rawRgbPath != null){
                FileUtils.deleteFile(rawRgbPath);
            }
            if(rawIcePath != null) {
                FileUtils.deleteFile(rawIcePath);
            }
        }
        // 无论删除什么类型的图片，都需要删除binary图片和highlight图片
        if(imageInfo.isLabeled()) {
            iImageService.dislabelImageById(imgId);
            String biPath = imageInfo.getBiPath();
            String highLightPath = imageInfo.getHighLightPath();
            if(biPath != null){
                FileUtils.deleteFile(biPath);
            }
            if(highLightPath != null) {
                FileUtils.deleteFile(highLightPath);
            }
        }
        // 无论删除什么类型的图片，都需要删除对应的点集
        iPointsService.deletePointsByImgId(imgId);
//        Integer page = (Integer) session.getAttribute("page");
//        Integer limit = (Integer) session.getAttribute("limit");
//        Integer isPaged = (Integer) session.getAttribute("is_paged");
        return new JsonResult<String>(OK, "删除成功");
//            String url = "list?page=" + page + "&limit=" + limit + "&is_paged=" + isPaged;
//            System.out.println(url);
//            servletResponse.sendRedirect(url);
    }


    @GetMapping("download")
    @ApiOperation("下载固定类型的图片并打包为zip,0->rgb, 1->ice, 2->黑白, 4->高亮")
    public JsonResult<String> downloadImages(@RequestParam("img_id") String imgIdsStr,
                                           @RequestParam("img_mode") Integer imgModeInt,
                                           HttpServletResponse response) {
        // 将imgIdsStr转化为list
        List<Integer> imgIdList = new ArrayList<>();
        String[] imgIdStrs = imgIdsStr.split(",");
        for(String imgIdStr: imgIdStrs) {
            int imgId = Integer.parseInt(imgIdStr);
            imgIdList.add(imgId);
        }
        // 生成zip文件存放位置
        long timeMillis = System.currentTimeMillis();
        String strZipPath = ImageConfigurer.zipDir + timeMillis + ".zip";
        File zipDir = new File(ImageConfigurer.zipDir);
        // 创建文件夹
        if(!zipDir.isDirectory() && !zipDir.exists()) {
            zipDir.mkdirs();
        }
        List<ImageInfoEntity> imageInfos = iImageService.getImageInfosByIdList(imgIdList);
        List<DownloadBody> downloadBodyList = new ArrayList<>();
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(strZipPath));
            for(ImageInfoEntity imageInfo : imageInfos){
                // out.putNextEntry(new ZipEntry(imageInfo.getFileName() + ImageConfigurer.getImgSuffix(imageInfo.getType())));
                // 读取需要下载的文件内容，打包到zip中
                String imgPath = getImagePathByMode(imageInfo, imgModeInt);
                File file = new File(imgPath);
                String fileName = file.getName();
                out.putNextEntry(new ZipEntry(fileName));
                byte[] bytes = Files.readAllBytes(file.toPath());
                out.write(bytes);
                out.closeEntry();
            }
            out.close();

            System.out.println(strZipPath);
//            // 通过文件流的形式发送
//            response.setHeader("Content-Disposition", "attachment;filename=downloads.zip");
//            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(strZipPath));
//            FileCopyUtils.copy(bis, outputStream);
        } catch (IOException | NullPointerException e) {
            throw new FileEmptyException("后端找不到图片文件，若所下载的为标注文件，请确定该文件已标注;"
                    + "若所下载的为原始文件，则确定已正确上传");
        }
        // removeDir(zipDir);
        // 通过文件地址的形式传送
        String strZipUrl = ImageConfigurer.zipUrl+timeMillis+".zip";
        return new JsonResult<String>(OK, strZipUrl);
    }

    /**
     * 功能：根据上传的图片id和点集删除点集以及对应的图片上的标注区域
     * 请求路径： /imgs/del_points
     * 请求参数： Integer img_id
     *          Integer pid
     * 请求类型： GET
     * 请求结果： JsonResult<ChangeBody>
     */
    @GetMapping("del_points")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "img_id", value = "图片id"),
            @ApiImplicitParam(name = "pid", value = "点集id")
    })
    @ApiOperation("根据上传的图片id和点集删除点集以及对应的图片上的标注区域")
    public JsonResult<ChangeBody> deletePoints(@RequestParam("img_id") Integer imageId,
                                               @RequestParam("pid") String pidsStr,
                                               HttpSession session) {
        // 将imgIdsStr转化为list
        List<Integer> pidList = new ArrayList<>();
        String[] pidStrs = pidsStr.split(",");
        for(String pidStr: pidStrs) {
            int pid = Integer.parseInt(pidStr);
            pidList.add(pid);
        }
        // 删除点集
        for(Integer pid: pidList){
            iPointsService.deletePointsById(pid);
        }
        // 获取该图片的所有点集
        List<PointsEntity> pointsList = iPointsService.getPointsListByImgId(imageId);
        List<Integer> pids = iPointsService.getPointsIdsByImgId(imageId);
        // 获取原始图片
        if(imageId == null) {
            throw new ParameterErrorException("未接收到图片id");
        }
        ImageEntity image = getImage(imageId, 1);
//        // 获取图片信息
//        ImageInfoEntity imageInfo = iImageService.getImageInfoById(imageId);
//        if(imageInfo == null) {
//            throw new ImageInfoEmptyException("服务器不存在该图片信息");
//        }
//        // 新建image缓存
//        ImageEntity image = new ImageEntity();
//        // 设置image缓存的id和名字
//        image.setId(imageId);
//        image.setFileName(imageInfo.getFileName());
//        // 填充图片
//        String rawRgbPath = imageInfo.getRawRgbPath();
//        String rawIcePath = imageInfo.getRawIcePath();
//        if(rawRgbPath == null || rawIcePath == null) {
//            throw new FileEmptyException("服务器不存在原始rgb或ice图的信息，请重新上传");
//        }
//        try {
//            BufferedImage rawRgbImg = ImageIO.read(new File(rawRgbPath));
//            // 填充image缓存的width、height
//            image.setWidth((double) rawRgbImg.getWidth());
//            image.setHeight((double) rawRgbImg.getHeight());
//            // 保存到image缓存中
//            Mat rawRgbMat = CvTools.ImageToMat(rawRgbImg);
//            image.setRawRgbImg(rawRgbMat);
//            // 填充原始ice
//            BufferedImage rawIceImg = ImageIO.read(new File(rawIcePath));
//            // 将原始ice填充到缓存中
//            Mat rawIceMat = CvTools.ImageToMat(rawIceImg);
//            image.setRawIceImg(rawIceMat);
//
//        } catch (IOException e) {
//            throw new FileEmptyException("文件不存在");
//        }
        ImageEntity newImage = iImageService.changeImage(image, pointsList);
        ChangeBody changeBody = getChangeBody(newImage, pids);
//        // 更新缓存
//        session.setAttribute("imageCache", newImage);
        return new JsonResult<ChangeBody>(OK, changeBody);
    }

}
