package com.eztv.mud.utils;

import java.util.List;
import java.util.Map;

public class BArray {

    public static double[] intToDoubleArray(int[] input) {
        int length = input.length;
        double[] output = new double[length];
        for (int i = 0; i < length; i++) {
            output[i] = Double.valueOf(String.valueOf(input[i]));
        }
        return output;
    }

    public static String ListToString(List<?> list) {
        StringBuffer sb = new StringBuffer();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == null || list.get(i) == "") {
                    continue;
                }
                if (sb.length() > 0) sb.append(",");
                // 如果值是list类型则调用自己
                if (list.get(i) instanceof List) {
                    sb.append(ListToString((List<?>) list.get(i)));
                } else if (list.get(i) instanceof Map) {
                    sb.append(MapToString((Map<?, ?>) list.get(i)));
                } else {
                    sb.append(list.get(i));
                }
            }
        }
        return sb.toString();
    }


    public static String MapToString(Map<?, ?> map) {
        StringBuffer sb = new StringBuffer();
        // 遍历map
        for (Object obj : map.keySet()) {
            if (obj == null) {
                continue;
            }
            if (sb.length() > 0) sb.append(",");
            Object key = obj;
            Object value = map.get(key);
            if (value instanceof List<?>) {
                sb.append(key.toString() + ListToString((List<?>) value));
            } else if (value instanceof Map<?, ?>) {
                sb.append(key.toString()
                        + MapToString((Map<?, ?>) value));
            } else {
                sb.append(key.toString() + value.toString());
            }
        }
        return sb.toString();
    }

    //求数组的最大值(int)
    public static int getMax(int[] arr) {
        int max = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if (max < arr[i]) {
                max = arr[i];
            }
        }
        return max;
    }

    //求数组的最小值(int)
    public static int getMin(int[] arr) {
        int min = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if (min > arr[i]) {
                min = arr[i];
            }
        }
        return min;
    }

    //得到数组最大值的下标(int)
    public static int getMaxIndex(int[] arr) {
        int maxIndex = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[maxIndex] < arr[i]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    //得到数组最小值的下标(int)
    public static int getMinIndex(int[] arr) {
        int minIndex = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[minIndex] > arr[i]) {
                minIndex = i;
            }
        }
        return minIndex;
    }

    //获得数组之和(int)
    public static int getSum(int[] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        return sum;
    }

    //获得平均值(int)
    public static int getAverage(int[] arr) {
        int avg = getSum(arr) / arr.length;
        return avg;
    }

    //选择排序对数据进行降序排序(int)
    public static void selectSortDescendingArray(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {//i<arr.length-1;最后一个不用比较
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] < arr[j]) {
                    int temp = arr[j];
                    arr[j] = arr[i];
                    arr[i] = temp;
                }
            }
        }
    }

    //选择排序对数据进行升序排序(int)
    public static void selectSortAscendingArray(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {//i<arr.length-1;最后一个不用比较
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > arr[j]) {
                    int temp = arr[j];
                    arr[j] = arr[i];
                    arr[i] = temp;
                }
            }
        }
    }

    //冒泡排序对数据进行降序排序(int)
    public static void bubbleSortDescendingArray(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] < arr[j + 1]) {
                    int temp = arr[j + 1];
                    arr[j + 1] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }

    //冒泡排序对数据进行升序排序(int)
    public static void bubbleSortAscendingArray(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j + 1];
                    arr[j + 1] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }

    //二分查找(int)
    public static int binarySearch(int[] arr, int key) {
        int min, mid, max;
        min = 0;
        max = arr.length - 1;
        while (arr[min] < arr[max]) {
            mid = (min + max) / 2;
            if (key > arr[mid]) {
                min = mid + 1;
            } else if (key < arr[mid]) {
                max = mid - 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    //求数组的最大值(double)
    public static double getMax(double[] arr) {
        double max = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if (max < arr[i]) {
                max = arr[i];
            }
        }
        return max;
    }

    //求数组的最小值(double)
    public static double getMin(double[] arr) {
        double min = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if (min > arr[i]) {
                min = arr[i];
            }
        }
        return min;
    }

    //得到数组最大值的下标(double)
    public static int getMaxIndex(double[] arr) {
        int maxIndex = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[maxIndex] < arr[i]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    //得到数组最小值的下标(double)
    public static int getMinIndex(double[] arr) {
        int minIndex = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[minIndex] > arr[i]) {
                minIndex = i;
            }
        }
        return minIndex;
    }

    //获得数组之和(double)
    public static double getSum(double[] arr) {
        double sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        return sum;
    }

    //获得平均值(double)
    public static double getAverage(double[] arr) {
        double avg = getSum(arr) / arr.length;
        return avg;
    }

    //打印数组(double)
    public static void printArray(double[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (i != arr.length - 1) {
                System.out.print(arr[i] + ",");
            } else {
                System.out.println(arr[i]);
            }
        }
    }

    //选择排序对数据进行降序排序(double)
    public static void selectSortDescendingArray(double[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {//i<arr.length-1;最后一个不用比较
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] < arr[j]) {
                    double temp = arr[j];
                    arr[j] = arr[i];
                    arr[i] = temp;
                }
            }
        }
    }

    //选择排序对数据进行升序排序(double)
    public static void selectSortAscendingArray(double[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {//i<arr.length-1;最后一个不用比较
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > arr[j]) {
                    double temp = arr[j];
                    arr[j] = arr[i];
                    arr[i] = temp;
                }
            }
        }
    }

    //冒泡排序对数据进行降序排序(double)
    public static void bubbleSortDescendingArray(double[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] < arr[j + 1]) {
                    double temp = arr[j + 1];
                    arr[j + 1] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }

    //冒泡排序对数据进行升序排序(double)
    public static void bubbleSortAscendingArray(double[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    double temp = arr[j + 1];
                    arr[j + 1] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }

    //二分查找(double)
    public static int binarySearch(double[] arr, double key) {
        int min, mid, max;
        min = 0;
        max = arr.length - 1;
        while (arr[min] < arr[max]) {
            mid = (min + max) / 2;
            if (key > arr[mid]) {
                min = mid + 1;
            } else if (key < arr[mid]) {
                max = mid - 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    //将byte数组置空
    public static byte[] resetArray(byte[] a) {
        byte[] b2 = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            a[i] = b2[i];
        }
        return a;
    }

    /**
     * int到byte[] 由几位字节存储
     */
    public static byte[] intToByteArray(int i,int byteNum) {
        byte[] result = new byte[byteNum];
        //由高位到低位
        for(int j=byteNum-1,k=0;j>=0;j--,k++){
            result[j] = (byte) ((i >> k*8) & 0xFF);
        }
        return result;
    }

    /**
     * byte[]转int 由几位字节存储
     */
    public static int byteArrayToInt(byte[] bytes,int byteNum) {
        int value = 0; //由高位到低位
        for (int i = 0; i < byteNum; i++) {
            int shift = (byteNum - 1 - i) * 8;
            value += (bytes[i] & 0x000000FF) << shift;//往高位游
        }
        return value;
    }

}
