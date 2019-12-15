package com.example.abc.sigmak.MyClass;

import android.graphics.Bitmap;

import java.io.Serializable;

public class TextContent implements Serializable {
    public int PostID;//对应的post的id
    public char[] Text;
    public Bitmap[] Images;
    public int[] ImageLocation;//插图对应的位置
}
