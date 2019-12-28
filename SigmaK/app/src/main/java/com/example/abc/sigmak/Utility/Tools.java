package com.example.abc.sigmak.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.nfc.FormatException;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {
    public static final String EmailPattern = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    public static String StringArrayToString(String[] sequence){
        String combined = "";
        for(int i=0;i<sequence.length;++i){
            combined += sequence[i]+";;;";//TODO:找到一个解决方法，这个方法可能造成隐藏的bugs
        }
        return combined;
    }

    public static String[] CombinedStringToStringArray(String CombinedSequence){
        List<String> list = new LinkedList<String>();
        int p = CombinedSequence.indexOf(";;;");
        int b = p;
        list.add(CombinedSequence.substring(0,p-1));
        for(int i=p;i<CombinedSequence.length();++i){
            b = p;
            p = CombinedSequence.indexOf(";;;",p+2);
            list.add(CombinedSequence.substring(b+3,p-1));
        }
        return list.toArray(new String[0]);
    }

    public static String IntArrayToString(Integer[] array){
        String combined = "";
        for(int i=0;i<array.length;++i){
            combined += array[i]+";;;";//TODO:找到一个解决方法，这个方法可能造成隐藏的bugs
        }
        return combined;
    }

    public static Integer[] CombinedStringToIntegerArray(String CombinedSequence){
        List<Integer> list = new LinkedList<Integer>();
        int p = CombinedSequence.indexOf(";;;");
        int b = p;
        list.add(Integer.parseInt(CombinedSequence.substring(0,p-1)));
        for(int i=p;i<CombinedSequence.length();++i){
            b = p;
            p = CombinedSequence.indexOf(";;;",p+2);
            list.add(Integer.parseInt(CombinedSequence.substring(b+3,p-1)));
        }
        return list.toArray(new Integer[0]);
    }

    /**
     * 将对象转换为Byte数组再转换为String
     * @param object
     * @return
     * @throws Exception
     */
    public static String ObjectToString(Object object) throws Exception {
        String temp = null;
        if(object instanceof Serializable) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(object);//把对象写到流里
                temp = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            throw new Exception("Object must implements Serializable");
        }
        return temp;
    }

    /**
     * 通过二进制流将对象存入SharedPreferences
     * @param context
     * @param preferenceName preference的名字
     * @param key preference的键值
     * @param toSave 要保存的对象
     * @throws Exception
     */
    public static void SaveObjectToPreference(Context context, String preferenceName, String key, Object toSave) throws Exception {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceName, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString(key, ObjectToString(toSave));
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将Byte的String转换为其对应的Object
     * @param Bytes
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object StringToObject(String Bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais =  new ByteArrayInputStream(Base64.decode(Bytes.getBytes(), Base64.DEFAULT));
        Object read = null;

        ObjectInputStream ois = new ObjectInputStream(bais);
        read = (Object) ois.readObject();
        return read;
    }

    /**
     * 将通过二进制流写入SharedPreference的对象读出来
     * @param context
     * @param preferenceName preference的名字
     * @param key preference的键值
     * @return 读取到的对象
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object ReadObjectFromPreference(Context context, String preferenceName,String key) throws IOException, ClassNotFoundException {
        SharedPreferences sharedPreferences=context.getSharedPreferences(preferenceName,context.MODE_PRIVATE);
        String temp = sharedPreferences.getString(key, "");
        return StringToObject(temp);
    }

    /**
     *将基本类型存入Preference
     * @param context
     * @param preferenceName preference的名字
     * @param key preference的键值
     * @param object 要存入的对象
     */
    public static void SaveToPreference(Context context, String preferenceName, String key, Object object){
        SharedPreferences sharedPreferences=context.getSharedPreferences(preferenceName,context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        editor.commit();
    }

    /**
     * 从Preference读取基本类型的数据
     * @param context
     * @param preferenceName preference的名字
     * @param key preference的键值
     * @param object 要读取的对象类型的实例
     * @return
     */
    public static Object ReadFromPreference(Context context, String preferenceName,String key,Object object){
        SharedPreferences sharedPreferences=context.getSharedPreferences(preferenceName,context.MODE_PRIVATE);
        if (object instanceof String) {
            return sharedPreferences.getString(key, (String) object);
        } else if (object instanceof Integer) {
            return sharedPreferences.getInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            return sharedPreferences.getBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            return sharedPreferences.getFloat(key, (Float) object);
        } else if (object instanceof Long) {
            return sharedPreferences.getLong(key, (Long) object);
        } else {
            return sharedPreferences.getString(key, object.toString());
        }
    }

    /**
     * 从Preference读取基本类型的数据
     * @param context
     * @param preferenceName preference的名字
     * @param key preference的键值
     * @return
     */
    public static String ReadFromPreference(Context context, String preferenceName,String key){
        SharedPreferences sharedPreferences=context.getSharedPreferences(preferenceName,context.MODE_PRIVATE);
            return sharedPreferences.getString(key,"");

    }

    /**
     * 将图片存入SharedPreference
     * @param context
     * @param preferenceName preference的名字
     * @param bitmap 要保存的图片
     * @param key preference的键值
     */
    public static void SaveBitmapToPreference(Context context, String preferenceName, Bitmap bitmap, /*int resId,*/String key) {
        SharedPreferences sharedPreferences=context.getSharedPreferences(preferenceName,context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        ////Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        //ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
        //String imageBase64 = new String(Base64.encodeToString(baos.toByteArray(),Base64.DEFAULT));
        //editor.putString(key,imageBase64);
        editor.putString(key, BitmapToString(bitmap));
        editor.commit();
    }

    /**
     * 从SharedPreference读取图片
     * @param context
     * @param preferenceName preference的名字
     * @param key preference的键值
     * @return
     */
    public static Bitmap ReadBitmapFromSharePreference(Context context, String preferenceName, String key) {
        SharedPreferences sharedPreferences=context.getSharedPreferences(preferenceName,context.MODE_PRIVATE);
        String temp = sharedPreferences.getString(key, "");
        //byte[] bytes = Base64.decode(temp.getBytes(), Base64.DEFAULT);
        ////ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(temp.getBytes(), Base64.DEFAULT));
        ////return Drawable.createFromStream(bais, "");
        //return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return StringToBitmap(temp);
    }

    public static boolean CheckEmailValid(String seq){
        Pattern regex = Pattern.compile(EmailPattern);

        Matcher matcher = regex.matcher(seq);

        return matcher.matches();
    }

    public static String BitmapToString(Bitmap image){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 50, baos);
        String imageBase64 = new String(Base64.encodeToString(baos.toByteArray(),Base64.DEFAULT));
        return imageBase64;
    }

    public static Bitmap StringToBitmap(String Bytes){
        byte[] bytes = Base64.decode(Bytes.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


    /**
     *
     * @param dateSeq 2019-12-19 20:59
     * @return
     */
    public static Date StringToDate(String dateSeq) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date date = format.parse(dateSeq);
        return date;
    }

    public static String DateToString(Date date){
        String pattern = "yyyy-MM-dd HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    public static ContentValues formContentValuesString(String... args) throws FormatException {
        if (args.length % 2 != 0) {
            throw new FormatException("args isn't form pairs.");
        }
        int value = args.length/2;
        int key = 0;
        ContentValues cv = new ContentValues();
        while(value < args.length){
            cv.put(args[key],args[value]);
            ++key;
            ++value;
        }
        return cv;
    }

    public static ContentValues formContentValuesInt(String... args) throws FormatException {
        if (args.length % 2 != 0) {
            throw new FormatException("args isn't form pairs.");
        }
        int value = args.length/2;
        int key = 0;
        ContentValues cv = new ContentValues();
        while(value < args.length){
            cv.put(args[key],Integer.parseInt(args[value]));
            ++key;
            ++value;
        }
        return cv;
    }
}
