package com.example.mahdi.simplewebview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * description：  保存图片的util <br/>
 * ===============================<br/>
 * creator：Feng Fengjun<br/>
 * create time：2016/8/3 17:14<br/>
 * ===============================<br/>
 * reasons for modification：  <br/>
 * Modifier：  <br/>
 * Modify time：  <br/>
 */
public class SaveImageUtil {
    public static String RESULT_CODE = "resultCode";
    public static String IMAGE_PATH = "imagePath";

    /**
     * 下载和保存图片
     *
     * @param originalImagePath 图片所在的路径
     * @return 保存的是否成功的结果（0：表示成功，负数表示保存失败）
     */
    public static Map<String, Object> downloadImage(String originalImagePath) {
        Map<String, Object> map = new HashMap<>();
        int result = -1;
        if (TextUtils.isEmpty(originalImagePath)) {
            map.put(SaveImageUtil.RESULT_CODE, -1);
            map.put(SaveImageUtil.IMAGE_PATH, null);
            return map;
        }
        String imageName = "simplewebview_" + Math.abs(originalImagePath.hashCode()) + "_" + System.currentTimeMillis() + ".png";
        String imageSavePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        if (TextUtils.isEmpty(imageSavePath)) {
            map.put(SaveImageUtil.RESULT_CODE, -1);
            map.put(SaveImageUtil.IMAGE_PATH, imageSavePath);
            return map;
        }
        File imageSaveFile = new File(imageSavePath, imageName);

        createNewFileAndParentDir(imageSaveFile);
        if (isUrl(originalImagePath)) { //download http(s)-image
            result = saveImageWithUrl(originalImagePath, imageSaveFile);
        } else if (checkBase64(originalImagePath)) { // save base64 image
            result = saveImageWithBase64(originalImagePath, imageSaveFile);
        }

        // 通知图库更新
        SimpleApp.getInstannce().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imageSaveFile)));
        if (result != 0) {
            try {
                imageSaveFile.delete();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        map.put(SaveImageUtil.RESULT_CODE, result);
        map.put(SaveImageUtil.IMAGE_PATH, imageSavePath);
        return map;
    }

    public static boolean isUrl(String url) {
        return !TextUtils.isEmpty(url) && url.startsWith("http");
    }

    private static boolean checkBase64(String args) {
        return !TextUtils.isEmpty(args) && args.contains("data:image");
    }

    private static int saveImageWithBase64(String base64, File saveFile) {
        int result;
        OutputStream stream = null;
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(base64.split(",")[1], Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
            stream = new FileOutputStream(saveFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.close();
            result = 0;
        } catch (Throwable e) {
            e.printStackTrace();
            result = -4;
        } finally {
            closeQuietly(stream);
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
        return result;
    }

    private static int saveImageWithUrl(String originalUrl, File saveFile) {
        int result = -1;
        FileOutputStream fos = null;
        try {
            URL url = new URL(originalUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream inputStream = conn.getInputStream();
            Bitmap imgSave = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            fos = new FileOutputStream(saveFile);
            boolean compress = imgSave.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            //图片保存成功
            if (compress) {
                result = 0;
            }
        } catch (FileNotFoundException e1) {
            result = -1;
            e1.printStackTrace();
        } catch (IOException e2) {
            result = -2;
            e2.printStackTrace();
        } catch (Throwable t) {
            result = -3;
            t.printStackTrace();
        } finally {
            closeQuietly(fos);
        }
        return result;
    }

    /**
     * 关闭{@code Closeable},已处理异常
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.w("wjc", e.toString());
            }
        }
    }

    /**
     * 创建文件及其父目录
     *
     * @param file
     * @return
     */
    public static boolean createNewFileAndParentDir(File file) {
        if (null == file) {
            return false;
        }
        boolean isCreateNewFileOk = true;
        isCreateNewFileOk = createParentDir(file);
        //创建父目录失败，直接返回false，不再创建子文件
        if (isCreateNewFileOk) {
            if (!file.exists()) {
                try {
                    isCreateNewFileOk = file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    isCreateNewFileOk = false;
                }
            }
        }
        Log.d("wjc", "createFileAndParentDir.file = " + file + ", isCreateNewFileOk = " + isCreateNewFileOk);
        return isCreateNewFileOk;
    }

    /**
     * 创建文件父目录
     *
     * @param file
     * @return
     */
    public static boolean createParentDir(File file) {
        if (null == file) {
            return false;
        }
        boolean isMkdirs = true;
        if (!file.exists()) {
            File dir = file.getParentFile();
            if (!dir.exists()) {
                isMkdirs = dir.mkdirs();
                Log.e("wjc", "createParentDir.dir = " + dir + ", isMkdirs = " + isMkdirs);
            }
        }
        return isMkdirs;
    }
}