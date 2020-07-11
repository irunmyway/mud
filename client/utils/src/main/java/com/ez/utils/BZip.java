package com.ez.utils;


import com.ez.utils.event.BEventManager;
import com.ez.utils.event.BEvents;
import com.ez.utils.event.BListener;
import com.ez.utils.model.BZipVO;
import com.ez.utils.zip.ZipEntry;
import com.ez.utils.zip.ZipFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class BZip {

    public static void readByApacheZipFile(final String zipPath, final String dirPath, final BListener listener) throws IOException {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    // 创建解压目标目录
                    File file = new File(dirPath);
                    // 如果目标目录不存在，则创建
                    if (!file.exists()) {
                        file.mkdirs();
                    }

                    List<ZipEntry> list = new ArrayList<ZipEntry>();

                    BufferedInputStream bi;
                    ZipFile zf = new ZipFile(zipPath, "GBK");//支持中文
                    Enumeration e = zf.getEntries();
                    while (e.hasMoreElements()) {
                        ZipEntry ze2 = (ZipEntry) e.nextElement();
                        list.add(ze2);
                    }

                    for (int i = 0; i < list.size(); i++) {

                        ZipEntry ze2 = list.get(i);
                        String entryName = ze2.getName();
                        String path = dirPath + "/" + entryName;

                        BZipVO vo = new BZipVO();
                        vo.count = i;
                        vo.total = list.size();
                        vo.entryName = entryName;
                        BEventManager.dispathEvent(BEvents.BZIP_RUNING, vo, listener);

                        if (ze2.isDirectory()) {
                            //System.out.println("正在创建解压目录 - " + entryName);
                            File decompressDirFile = new File(path);
                            if (!decompressDirFile.exists()) {
                                decompressDirFile.mkdirs();
                            }
                        } else {
                            //System.out.println("正在创建解压文件 - " + entryName);
                            String fileDir = path.substring(0, path.lastIndexOf("/"));
                            File fileDirFile = new File(fileDir);
                            if (!fileDirFile.exists()) {
                                fileDirFile.mkdirs();
                            }
                            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dirPath + "/" + entryName));
                            bi = new BufferedInputStream(zf.getInputStream(ze2));
                            byte[] readContent = new byte[1024];
                            int readCount = bi.read(readContent);
                            while (readCount != -1) {
                                bos.write(readContent, 0, readCount);
                                readCount = bi.read(readContent);
                            }
                            bos.close();
                        }
                    }

                    zf.close();

                } catch (Exception ex) {

                    BDebug.trace(this, ex);
                    BEventManager.dispathEvent(BEvents.BZIP_ERROR, listener);
                }

                BEventManager.dispathEvent(BEvents.BZIP_COMPLETE, listener);

            }
        }).start();
    }
}
