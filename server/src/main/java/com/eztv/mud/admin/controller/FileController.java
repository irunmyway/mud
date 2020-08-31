package com.eztv.mud.admin.controller;

import com.eztv.mud.admin.constant.FileTypeEnum;
import com.eztv.mud.admin.uitls.FileTypeUtil;
import com.eztv.mud.utils.BFile;
import com.eztv.mud.utils.BObject;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;


/**
 * @author cinco
 * @description 文件服务器
 * @date 2019-1-21
 */
@RestController
@RequestMapping("/game/api/file/")
public class FileController {
    private static final String SLASH = "/";
    private String fileDir = System.getProperty("user.dir") + "/script/";

    /**
     * 获取全部文件
     *
     * @param dir
     * @param accept
     * @param exts
     * @return Map
     */
    @ResponseBody
    @RequestMapping("getFileList")
    public Map list(String dir, String accept, String exts) {
        String[] mExts = null;
        if (exts != null && !exts.trim().isEmpty()) {
            mExts = exts.split(",");
        }
        if (fileDir == null) {
            fileDir = SLASH;
        }
        if (!fileDir.endsWith(SLASH)) {
            fileDir += SLASH;
        }
        Map<String, Object> rs = new HashMap<>();
        if (dir == null || SLASH.equals(dir)) {
            dir = "";
        } else if (dir.startsWith(SLASH)) {
            dir = dir.substring(1);
        }
        File file = new File(fileDir + dir);
        File[] listFiles = file.listFiles();
        List<Map> dataList = new ArrayList<>();
        if (listFiles != null) {
            for (File f : listFiles) {
                if ("sm".equals(f.getName())) {
                    continue;
                }
                Map<String, Object> m = new HashMap<>(0);
                // 文件名称
                m.put("name", f.getName());
                // 修改时间
                m.put("updateTime", f.lastModified());
                // 是否是目录
                m.put("isDir", f.isDirectory());
                if (f.isDirectory()) {
                    // 文件类型
                    m.put("type", "dir");
                } else {
                    // 是否支持在线查看
                    boolean flag = false;
                    try {
//                        if (FileTypeUtil.canOnlinePreview(new Tika().detect(f))) {
//                            flag = true;
//                        }
                        m.put("preview", flag);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String type;
                    // 文件地址
//                    m.put("url", (dir.isEmpty() ? dir : (dir + SLASH)) + f.getName());
                    m.put("url",  f.getName());
                    // 获取文件类型
                    String contentType = null;
                    String suffix = f.getName().substring(f.getName().lastIndexOf(".") + 1);
                    try {
//                        contentType = new Tika().detect(f);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // 筛选文件类型
                    if (accept != null && !accept.trim().isEmpty() && !accept.equals("file")) {
                        if (contentType == null || !contentType.startsWith(accept + SLASH)) {
                            continue;
                        }
                        if (mExts != null) {
                            for (String ext : mExts) {
                                if (!f.getName().endsWith("." + ext)) {
                                    continue;
                                }
                            }
                        }
                    }
                    // 获取文件图标
                    m.put("type", getFileType(suffix, contentType));
                    // 是否有缩略图
                    String smUrl = "sm/" + (dir.isEmpty() ? dir : (dir + SLASH)) + f.getName();
                    if (new File(fileDir + smUrl).exists()) {
                        m.put("hasSm", true);
                        // 缩略图地址
                        m.put("smUrl", smUrl);
                    }
                }
                dataList.add(m);
            }
        }
        // 根据上传时间排序
        Collections.sort(dataList, new Comparator<Map>() {
            @Override
            public int compare(Map o1, Map o2) {
                Long l1 = (long) o1.get("updateTime");
                Long l2 = (long) o2.get("updateTime");
                return l1.compareTo(l2);
            }
        });
        // 把文件夹排在前面
        Collections.sort(dataList, new Comparator<Map>() {
            @Override
            public int compare(Map o1, Map o2) {
                Boolean l1 = (boolean) o1.get("isDir");
                Boolean l2 = (boolean) o2.get("isDir");
                return l2.compareTo(l1);
            }
        });
        rs.put("code", 200);
        rs.put("msg", "查询成功");
        rs.put("data", dataList);
        return rs;
    }

    /**
     * 获取文件类型
     *
     * @param suffix
     * @param contentType
     * @return
     */
    private String getFileType(String suffix, String contentType) {
        String type;
        if (FileTypeEnum.PPT.getName().equalsIgnoreCase(suffix) || FileTypeEnum.PPTX.getName().equalsIgnoreCase(suffix)) {
            type = FileTypeEnum.PPT.getName();
        } else if (FileTypeEnum.DOC.getName().equalsIgnoreCase(suffix) || FileTypeEnum.DOCX.getName().equalsIgnoreCase(suffix)) {
            type = FileTypeEnum.DOC.getName();
        } else if (FileTypeEnum.XLS.getName().equalsIgnoreCase(suffix) || FileTypeEnum.XLSX.getName().equalsIgnoreCase(suffix)) {
            type = FileTypeEnum.XLS.getName();
        } else if (FileTypeEnum.PDF.getName().equalsIgnoreCase(suffix)) {
            type = FileTypeEnum.PDF.getName();
        } else if (FileTypeEnum.HTML.getName().equalsIgnoreCase(suffix) || FileTypeEnum.HTM.getName().equalsIgnoreCase(suffix)) {
            type = FileTypeEnum.HTM.getName();
        } else if (FileTypeEnum.TXT.getName().equalsIgnoreCase(suffix)) {
            type = FileTypeEnum.TXT.getName();
        } else if (FileTypeEnum.SWF.getName().equalsIgnoreCase(suffix)) {
            type = FileTypeEnum.FLASH.getName();
        } else if (FileTypeEnum.ZIP.getName().equalsIgnoreCase(suffix) || FileTypeEnum.RAR.getName().equalsIgnoreCase(suffix) || FileTypeEnum.SEVENZ.getName().equalsIgnoreCase(suffix)) {
            type = FileTypeEnum.ZIP.getName();
        } else if (contentType != null && contentType.startsWith(FileTypeEnum.AUDIO.getName() + SLASH)) {
            type = FileTypeEnum.MP3.getName();
        } else if (contentType != null && contentType.startsWith(FileTypeEnum.VIDEO.getName() + SLASH)) {
            type = FileTypeEnum.MP4.getName();
        } else {
            type = FileTypeEnum.FILE.getName();
        }
        return type;
    }

    /**
     * 新建文件夹
     *
     * @param curPos
     * @param dirName
     * @return Map
     */
    @ResponseBody
    @RequestMapping("mkdir")
    public Map mkdir(String curPos, String dirName) {
        if (fileDir == null) {
            fileDir = SLASH;
        }
        if (!fileDir.endsWith(SLASH)) {
            fileDir += SLASH;
        }
        if (!BObject.isEmpty(curPos) && !BObject.isEmpty(dirName)) {
            curPos = curPos.substring(1);
            String dirPath = fileDir + curPos + SLASH + dirName;
            File f = new File(dirPath);
            if (f.exists()) {
                return getRS(500, "目录已存在");
            }
            if (!f.exists() && f.mkdir()) {
                return getRS(200, "创建成功");
            }
        }
        return getRS(500, "创建失败");
    }

    /**
     * 封装返回结果
     *
     * @param code
     * @param msg
     * @param url
     * @return Map
     */
    private Map getRS(int code, String msg, String url) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("msg", msg);
        if (url != null) {
            map.put("url", url);
        }
        return map;
    }

    /**
     * 封装返回结果
     *
     * @param code
     * @param msg
     * @return Map
     */
    private Map getRS(int code, String msg) {
        return getRS(code, msg, null);
    }

    /**
     * 查看缩略图
     *
     * @param p        文件全名
     * @param response
     * @return
     */
    @GetMapping("sm")
    public String fileSm(@RequestParam("p") String p, HttpServletResponse response) {
        return getFile(p, false, response);
    }

    /**
     * 获取源文件或者缩略图文件
     *
     * @param p
     * @param download 是否下载
     * @param response
     * @return
     */
    private String getFile(String p, boolean download, HttpServletResponse response) {
//        if (useNginx) {
//            return useNginx(p);
//        }
        if (fileDir == null) {
            fileDir = SLASH;
        }
        if (!fileDir.endsWith(SLASH)) {
            fileDir += SLASH;
        }
        outputFile(fileDir + p, download, response);
        return null;
    }

    /**
     * 输出文件流
     *
     * @param file
     * @param download 是否下载
     * @param response
     */
    private void outputFile(String file, boolean download, HttpServletResponse response) {
        // 判断文件是否存在
        File inFile = new File(file);
        // 文件不存在
        if (!inFile.exists()) {
            PrintWriter writer = null;
            try {
                response.setContentType("text/html;charset=UTF-8");
                writer = response.getWriter();
                writer.write("<!doctype html><title>404 Not Found</title><link rel=\"shorcut icon\" href=\"assets/images/logo.png\"><h1 style=\"text-align: center\">404 Not Found</h1><hr/><p style=\"text-align: center\">FMS Server</p>");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        // 获取文件类型
        String contentType = null;
        try {
            //contentType = new Tika().detect(inFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 图片、文本文件,则在线查看
        if (FileTypeUtil.canOnlinePreview(contentType) && !download) {
            response.setContentType(contentType);
            response.setCharacterEncoding("UTF-8");
        } else {
            // 其他文件,强制下载
            response.setContentType("application/force-download");
            String newName;
            try {
                newName = URLEncoder.encode(inFile.getName(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                newName = inFile.getName();
            }
            response.setHeader("Content-Disposition", "attachment;fileName=" + newName);
        }
        // 输出文件流
        OutputStream os = null;
        FileInputStream is = null;
        try {
            is = new FileInputStream(inFile);
            os = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int len;
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除
     *
     * @param file
     * @return Map
     */
    @ResponseBody
    @RequestMapping("del")
    public Map del(String file) {
        if (fileDir == null) {
            fileDir = SLASH;
        }
        if (!fileDir.endsWith(SLASH)) {
            fileDir += SLASH;
        }
        if (file != null && !file.isEmpty()) {
            File f = new File(fileDir + file);
            File smF = new File(fileDir + "sm/" + file);
            if (f.exists()) {
                // 文件
                if (f.isFile()) {
                    if (f.delete()) {
                        if (smF.exists() && smF.isFile()) {
                            smF.delete();
                        }
                        return getRS(200, "文件删除成功");
                    }
                } else {
                    // 目录
                    forDelFile(f);
                    if (smF.exists() && smF.isDirectory()) {
                        forDelFile(smF);
                    }
                    return getRS(200, "目录删除成功");
                }
            } else {
                return getRS(500, "文件或目录不存在");
            }
        }
        return getRS(500, "文件或目录删除失败");
    }

    /**
     * 递归删除目录下的文件以及目录
     *
     * @param file
     * @return
     */
    static boolean forDelFile(File file) {
        if (!file.exists()) {
            return false;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                forDelFile(f);
            }
        }
        return file.delete();
    }

    /**
     * 重命名
     *
     * @param oldFile
     * @param newFile
     * @return Map
     */
    @ResponseBody
    @RequestMapping("rename")
    public Map rename(String oldFile, String newFile) {
        if (fileDir == null) {
            fileDir = SLASH;
        }
        if (!fileDir.endsWith(SLASH)) {
            fileDir += SLASH;
        }
        if (!BObject.isEmpty(oldFile) && !BObject.isEmpty(newFile)) {
            File f = new File(fileDir + oldFile);
            File smF = new File(fileDir + "sm/" + oldFile);
            File nFile = new File(fileDir + newFile);
            File nsmFile = new File(fileDir + "sm/" + newFile);
            if (f.renameTo(nFile)) {
                if (smF.exists()) {
                    smF.renameTo(nsmFile);
                }
                return getRS(200, "重命名成功", SLASH + newFile);
            }
        }
        return getRS(500, "重命名失败");
    }
    @ResponseBody
    @RequestMapping("getFileContent")
    public String getFileContent(String file) {
        String src = System.getProperty("user.dir")+"/script"+file;
        return BFile.readFromFile(src);
    }
    @ResponseBody
    @RequestMapping("setFileContent")
    public String setFileContent(String file,String code) {
        String src = System.getProperty("user.dir")+"/script"+file;
        return BFile.writeFile(src,code)+"";
    }

}
