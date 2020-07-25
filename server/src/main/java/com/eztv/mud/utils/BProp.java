package com.eztv.mud.utils;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import static com.eztv.mud.GameUtil.colorString;

/**
 * properties工具类
 *
 * @author xianhuang
 * @date 2019/03/02 15:46
 * @since 1.0
 */
public class BProp {

    /**
     * properties文件
     */
    private static Properties prop;
    private static BProp propertiesUtil;

    public static BProp getInstance() {
        if (propertiesUtil==null){
            prop = new Properties(); // 属性集合对象
            propertiesUtil=new BProp();
        }
        return propertiesUtil;
    }


    public  void load(String path) {
        FileInputStream fis=null;
        try {
            fis = new FileInputStream(path);
            prop.load(new InputStreamReader(fis, "UTF-8"));
//            prop.load(fis);
        }catch (Exception e){e.printStackTrace();}
        finally {
            try {
                fis.close(); // 关闭流
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public  Properties getProp() {
        return prop;
    }

    public  Boolean updatePro(String path, String key, String value) throws IOException {
        load(path);
        prop.setProperty(key, value);
        // 文件输出流
        FileOutputStream fos = new FileOutputStream(path);
        // 将Properties集合保存到流中
        prop.store(fos, "update [key:" + key + ", value:" + value + "]");
        fos.close(); // 关闭流
        return true;
    }


    public  String get(String key) throws IOException {
        String str = prop.getProperty(key);
        return colorString(str);
    }
}

