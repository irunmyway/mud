package com.ez.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.DataOutputStream;

public class BSystem {


    /**
     * 获取密度
     */
    public static float getDensity(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.density;
    }

    /**
     * 获取dpi
     */
    public static float getDPI(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.xdpi;
    }


    private static String getTempNumber(Context context) {
        return Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
    }


    /**
     * 重启 app.
     */
    public static void relaunchApp(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        if (intent == null) return;
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        context.startActivity(mainIntent);
        System.exit(0);

    }


    /**
     * 禁止休眠
     */
    public static void onKeep(Activity act) {

        act.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }


    /**
     * 获取电量
     */
    public static int getBatteryCapacity(Context context) {
        return ((BatteryManager) context.getSystemService(Context.BATTERY_SERVICE)).getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
    }

    public static int getBatteryCapacity(Intent arg1) {
        int level = arg1.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        int scale = arg1.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
        return (level * 100) / scale;
    }


    /**
     * 是否充电中
     */
    public static boolean getBatteryIsCharge(Context context) {
        boolean b = false;
        if (((BatteryManager) context.getSystemService(Context.BATTERY_SERVICE)).getIntProperty(BatteryManager.BATTERY_STATUS_CHARGING) > 0) {
            b = true;
        }
        return b;
    }

    public static boolean getBatteryIsCharge(Intent arg1) {
        int status = arg1.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        return status == 2;
    }


    /**
     * 关闭软键盘
     */
    public static void closeKeyboard(Context context) {
        try {
            InputMethodManager m = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            m.hideSoftInputFromWindow(((Activity) context).getWindow().peekDecorView().getWindowToken(), 0);
        } catch (Exception e) {
            BDebug.trace("Error closeKeyboard: ", e.getMessage());
        }
    }


    /**
     * 调整音量
     *
     * @param add    true加 false减
     * @param showUI 是否显示
     */
    public static void changeVolume(Context context, boolean add, boolean showUI) {
        try {
            ((AudioManager) context.getSystemService(Service.AUDIO_SERVICE)).adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    (add ? AudioManager.ADJUST_RAISE : AudioManager.ADJUST_LOWER),
                    (showUI ? AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI : AudioManager.FLAG_PLAY_SOUND));
        } catch (Exception e) {
            BDebug.trace("Error changeVolume: ", e.getMessage());
        }
    }

    public static void changeVolume(Context context, int vol) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_PLAY_SOUND);
        am.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);//得到听筒模式的最大值
        am.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
    }

    // 根据亮度值修改当前window亮度0-255
    public static void changeAppBrightness(Context context, int brightness) {
        Window window = ((Activity) context).getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (brightness == -1) {
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        } else {
            lp.screenBrightness = (brightness <= 0 ? 1 : brightness) / 255f;
        }
        window.setAttributes(lp);
    }


    /**
     * 使用SU命令
     *
     * @param command
     */
    public static void rootCommand(String command) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            BDebug.trace("*** DEBUG ***" + "ROOT REE" + e.getMessage());
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
                BDebug.trace("*** DEBUG ***" + "ROOT REE" + e.getMessage());
            }
        }
    }


    /**
     * 获取APP安装 时间戳
     *
     * @return
     */
    public static long getAppInstallMillis(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.firstInstallTime;
        } catch (Exception e) {

        }
        return 0;
    }


    /**
     * 判断 App 是否是 Debug 版本.
     *
     * @return
     */
    public static boolean isAppDebug(Context context) {
        if (isSpace(context.getPackageName())) return false;
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(context.getPackageName(), 0);
            return ai != null && (ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 判断 App 是否是系统应用.
     *
     * @return
     */
    public static boolean isAppSystem(Context context) {
        if (isSpace(context.getPackageName())) return false;
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(context.getPackageName(), 0);
            return ai != null && (ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 判断自己的应用程序是否为默认桌面
     */
    public static final boolean isDefaultHome(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);//Intent.ACTION_VIEW
        intent.addCategory("android.intent.category.HOME");
        intent.addCategory("android.intent.category.DEFAULT");
        PackageManager pm = context.getPackageManager();
        ResolveInfo info = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        boolean isDefault = context.getPackageName().equals(info.activityInfo.packageName);
        return isDefault;
    }


    /**
     * 判断设备是否是手机.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isPhone(Context context) {
        TelephonyManager tm =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null && tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }


    /**
     * 获取 App 图标.
     *
     * @return
     */
    public static Drawable getAppIcon(Context context) {
        if (isSpace(context.getPackageName())) return null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi == null ? null : pi.applicationInfo.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取 App 包名.
     *
     * @return the application's package name
     */
    public static String getAppPackageName(Context context) {
        return context.getPackageName();
    }


    /**
     * 获取 App 名称.
     *
     * @return the application's name
     */
    public static String getAppName(Context context) {
        if (isSpace(context.getPackageName())) return "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi == null ? null : pi.applicationInfo.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * 获取 App 路径.
     *
     * @return the application's path
     */
    public static String getAppPath(Context context) {
        if (isSpace(context.getPackageName())) return "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi == null ? null : pi.applicationInfo.sourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * 获取 App 版本号.
     *
     * @return the application's version name
     */
    public static String getAppVersionName(Context context) {
        if (isSpace(context.getPackageName())) return "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * 获取 App 版本码.
     *
     * @return the application's version code
     */
    public static int getAppVersionCode(Context context) {
        if (isSpace(context.getPackageName())) return -1;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }


    /**
     * 获取 App 签名.
     *
     * @return the application's signature
     */
    public static Signature[] getAppSignature(Context context) {
        if (isSpace(context.getPackageName())) return null;
        try {
            PackageManager pm = context.getPackageManager();
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            return pi == null ? null : pi.signatures;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取设备厂商.
     * <p>e.g. Xiaomi</p>
     *
     * @return the manufacturer of the product/hardware
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }


    /**
     * 获取设备型号.
     * <p>e.g. MI2SC</p>
     *
     * @return the model of device
     */
    public static String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }

    /**
     * 获取cpu架构
     *
     * @return an ordered list of ABIs supported by this device
     */
    public static String[] getABIs() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return Build.SUPPORTED_ABIS;
        } else {
            if (!TextUtils.isEmpty(Build.CPU_ABI2)) {
                return new String[]{Build.CPU_ABI, Build.CPU_ABI2};
            }
            return new String[]{Build.CPU_ABI};
        }
    }


    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    /**
     * The application's information.
     */
    public static class AppInfo {

        private String packageName;
        private String name;
        private Drawable icon;
        private String packagePath;
        private String versionName;
        private int versionCode;
        private boolean isSystem;

        public Drawable getIcon() {
            return icon;
        }

        public void setIcon(final Drawable icon) {
            this.icon = icon;
        }

        public boolean isSystem() {
            return isSystem;
        }

        public void setSystem(final boolean isSystem) {
            this.isSystem = isSystem;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(final String packageName) {
            this.packageName = packageName;
        }

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public String getPackagePath() {
            return packagePath;
        }

        public void setPackagePath(final String packagePath) {
            this.packagePath = packagePath;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(final int versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(final String versionName) {
            this.versionName = versionName;
        }

        public AppInfo(String packageName, String name, Drawable icon, String packagePath,
                       String versionName, int versionCode, boolean isSystem) {
            this.setName(name);
            this.setIcon(icon);
            this.setPackageName(packageName);
            this.setPackagePath(packagePath);
            this.setVersionName(versionName);
            this.setVersionCode(versionCode);
            this.setSystem(isSystem);
        }

        @Override
        public String toString() {
            return "pkg name: " + getPackageName() +
                    "\napp icon: " + getIcon() +
                    "\napp name: " + getName() +
                    "\napp path: " + getPackagePath() +
                    "\napp v name: " + getVersionName() +
                    "\napp v code: " + getVersionCode() +
                    "\nis system: " + isSystem();
        }
    }
}
