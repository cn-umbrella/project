package com.inext.utils;

import com.inext.entity.BackUser;
import com.inext.enumerate.AliyunOssClassify;
import org.springframework.web.context.ContextLoader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 上传文件工具类
 *
 * @author God
 */
public class UploadUtils {

    /**
     * 附件路径
     */
    public static final String FILE_PATH = "files";

    /**
     * 获得上传目录的相对地址
     *
     * @return
     */
    public static String getRelatedPath() {
        String filePathDir = "/" + FILE_PATH + "/" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        return filePathDir;
    }

    /**
     * 获得上传目录的完整路径
     *
     * @return
     */
    public static String getRealPath() {
        return getRealPath(getRelatedPath());
    }

    /**
     * 获得上传目录的完整路径
     *
     * @return
     */
    public static String getRealPath(String path) {

//        String fileRealPathDir = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"static"+path;

        String fileRealPathDir = path;
        File pathFile = new File(fileRealPathDir);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        return fileRealPathDir;
    }

    public static void main(String[] args) {
        BackUser user = new BackUser();
        System.out.println(user);
        doA(user);
        System.out.println(user.getId());
        //  System.out.println(ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static");
    }

    public static void doA(BackUser user) {
        BackUser usera = new BackUser();
        usera.setId(7);
        user = usera;
        System.out.println(user);
        System.out.println(user.getId());
    }

    /**
     * 获得上传目录的完整路径，如果目录不存在则创建目录
     *
     * @return
     */
    public static String getProjectPath() {
        String fileRealPathDir = ContextLoader
                .getCurrentWebApplicationContext().getServletContext()
                .getRealPath("/");
        File pathFile = new File(fileRealPathDir);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        return fileRealPathDir;
    }

    /**
     * 获得上传目录的完整路径，如果目录不存在不创建目录
     *
     * @return
     */
    public static String getRealPath2(String path) {
        String fileRealPathDir = ContextLoader
                .getCurrentWebApplicationContext().getServletContext()
                .getRealPath(path);
        return fileRealPathDir;
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 文件重命名
     *
     * @param path    文件目录
     * @param oldname 原来的文件名
     * @param newname 新文件名
     */
    public static boolean renameFile(String path, String oldname, String newname) {
        if (!oldname.equals(newname)) {// 新的文件名和以前文件名不同时,才有必要进行重命名
            File oldfile = new File(path + "/" + oldname);
            File newfile = new File(path + "/" + newname);
            if (!oldfile.exists()) {
                return false;// 重命名文件不存在
            }
            if (newfile.exists())// 若在该目录下已经有一个文件和新文件名相同，则不允许重命名
                return false;
            else {
                oldfile.renameTo(newfile);
                return true;
            }
        } else {
            return false;// 新文件名和旧文件名相同
        }
    }

    /**
     * 生成阿里云OSS存储的路径
     *
     * @param classify
     * @return
     */
    public static String getAliyunOssPath(String classify) {
        String filePathDir = classify + "/"
                + new SimpleDateFormat("yyyy-MM-dd-HHmmssSSS").format(new Date());

        return filePathDir;
    }

    public static String getAliyunOssFileName(String originalFilename) {
        String fileName = getAliyunOssPath(AliyunOssClassify.IMG.name());
        fileName += String.valueOf(Math.random()).substring(2).substring(0, 4) + originalFilename.substring(originalFilename.lastIndexOf("."));
        return fileName;
    }

}
