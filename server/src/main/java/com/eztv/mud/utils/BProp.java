package com.eztv.mud.utils;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
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
    private static Map<String,Properties> props = new HashMap<>();
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
    public Properties getProp(String name) {
        Properties prop = props.get(name);
        if(prop==null){
            FileInputStream fis=null;
            try {
                Properties newProp = new Properties();
                String path = System.getProperty("user.dir")+"/script/conf/"+name+".properties";
                fis = new FileInputStream(path);
                newProp.load(new InputStreamReader(fis, "UTF-8"));
                props.put(name,newProp);
                return newProp;
            }catch (Exception e){e.printStackTrace();return null;}
            finally {
                try {
                    fis.close(); // 关闭流
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
    public String get(String propName,String key) {
        Properties prop = props.get(propName);
        if(prop==null){
            FileInputStream fis=null;
            try {
                Properties newProp = new Properties();
                String path = System.getProperty("user.dir")+"/script/conf/"+propName+".properties";
                fis = new FileInputStream(path);
                newProp.load(new InputStreamReader(fis, "UTF-8"));
                props.put(propName,newProp);
                String str = newProp.getProperty(key);
                return colorString(str);
            }catch (Exception e){e.printStackTrace();return "";}
            finally {
                try {
                    fis.close(); // 关闭流
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            String str = prop.getProperty(key);
            return colorString(str);
        }
    }

    public  String get(String key){
        String str = prop.getProperty(key);
        return colorString(str);
    }
}

