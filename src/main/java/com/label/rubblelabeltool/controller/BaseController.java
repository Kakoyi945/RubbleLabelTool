package com.label.rubblelabeltool.controller;

import com.label.rubblelabeltool.controller.ex.*;
import com.label.rubblelabeltool.service.ex.*;
import com.label.rubblelabeltool.util.JsonResult;
import com.label.rubblelabeltool.util.ex.UtilException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpSession;
import java.io.IOException;

public class BaseController {
    /**
     * 操作成功时的状态码
     */
    public static final int OK = 200;

    public static final int DELETE_ERROR = 3000;

    // 请求处理方法，这个方法的返回值就是需要传递给前端的数据
    // 自动将异常对象传递给此方法的参数列表上
    @ExceptionHandler({ServiceException.class, FileUploadException.class, IOException.class, ControllerException.class, UtilException.class})
    public JsonResult<Void> handleException(Throwable e) {
        JsonResult<Void> result = new JsonResult<>(e);
        if(e instanceof ImageDuplicatedException) {
            result.setState(4000);
            result.setMessage("文件名已被使用");
        } else if(e instanceof InsertException) {
            result.setState(5000);
            result.setMessage("保存图片时发生错误");
        } else if(e instanceof FileEmptyException) {
            String message = e.getMessage();
            result.setState(6000);
            result.setMessage(message);
        } else if(e instanceof FileStateException) {
            String message = e.getMessage();
            result.setState(6001);
            result.setMessage(message);
        } else if(e instanceof FileTypeException) {
            result.setState(6002);
            result.setMessage("上传的文件类型错误");
        } else if(e instanceof FileUploadIOException) {
            result.setState(6003);
            result.setMessage("无法正确接受到文件");
        } else if(e instanceof IOException) {
            result.setState(7000);
            result.setMessage("读取图片时发生错误");
        } else if(e instanceof ImageInfoEmptyException) {
            result.setState(7001);
            result.setMessage("图片信息为空");
        } else if(e instanceof ImageInfoErrorException) {
            result.setState(7002);
            result.setMessage("图片信息存在错误");
        } else if(e instanceof PointsEmptyException) {
            result.setState(8000);
            result.setMessage("传送的点集为空");
        } else if(e instanceof ImageModeUnsatisfiedException) {
            result.setState(7003);
            result.setMessage("图片模式不符合要求（如需要rgb而上传的为二值图）");
        } else if(e instanceof CacheNotFoundException) {
            String message = e.getMessage();
            result.setState(9000);
            result.setMessage(message);
        }
        return result;
    }

    /**
     * 获取session对象中的uid
     * @param session session对象
     * @return 当前登录用户的uid值
     */
    protected final Integer getUidFromSession(HttpSession session) {
        return Integer.valueOf(session.getAttribute("uid").toString());
    }

    protected final String getFileNameFromSession(HttpSession session) {
        return session.getAttribute("fileName").toString();
    }
}
