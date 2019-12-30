package com.example.abc.sigmak.MyClass;

import android.graphics.Bitmap;

import com.example.abc.sigmak.Utility.Tools;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import java.io.Serializable;

public class TextContent implements Serializable {
    public String Text;
    public Bitmap[] Images;
    public int[] ImageLocation;//插图对应的位置

    public TextContent(String text, Bitmap[] images, int[] imageLocation) {
        Text = text;
        Images = images;
        ImageLocation = imageLocation;
    }

    public TextContent(String JsonText) {
        try {
            JSONTokener jsonParser = new JSONTokener(JsonText);
            JSONObject jo = (JSONObject) jsonParser.nextValue();
            Text = jo.getString("Text");
            Images = null;
            ImageLocation = null;
            //Images = Tools.jo.getString("Images");
            //ImageLocation = PostType.valueOf(jo.getString("Type"));

        } catch (JSONException ex) {
            // 异常处理代码
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public String toJsonString(){
        JSONStringer jsonText = new JSONStringer();
        try {
            jsonText.object();
            jsonText.key("Text");
            jsonText.value(Text);
            jsonText.endObject();

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return jsonText.toString();
    }
}
