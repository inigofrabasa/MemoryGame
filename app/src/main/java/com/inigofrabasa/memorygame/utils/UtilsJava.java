package com.inigofrabasa.memorygame.utils;

import android.graphics.Bitmap;

import com.inigofrabasa.memorygame.MemoryGameApplication;
import com.inigofrabasa.memorygame.manager.GameManager;

import static com.inigofrabasa.memorygame.utils.ConstantsKt.URI_DRAWABLE;

public class UtilsJava {

    public static Bitmap loadCardBitmap(int id, int width, int height) {
        String string = GameManager.Companion.getInstance().getCardsPath().get(id);

        assert string != null;
        String drawableResourceName = string.substring(URI_DRAWABLE.length());

        int drawableResourceId = MemoryGameApplication.Companion.getInstance().getResources().getIdentifier(
                drawableResourceName, "drawable",
                MemoryGameApplication.Companion.getInstance().getAppContext().getPackageName()
        );

        return Utils.scaleDownImage(drawableResourceId, width, height);
    }
}
