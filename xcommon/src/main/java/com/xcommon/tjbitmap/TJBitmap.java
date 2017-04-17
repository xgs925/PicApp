package com.xcommon.tjbitmap;

import android.content.Context;
import android.graphics.Bitmap;

import com.xcommon.utils.BitmapUtil;
import com.xcommon.utils.TJCAdapter;
import com.xcommon.utils.toolfile.ToolBitmapCache;
import com.xcommon.utils.toolfile.ToolFileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Photostsrs on 2016/7/6.
 */
public class TJBitmap {
    private Info info;
    private List<String> mMasks;
    private boolean mUndoAble = false;
    private int mMaskIndex = -1;

    @Deprecated
    public TJBitmap(Context context, Info info) {
        this.info = info;
    }

    @Deprecated
    public TJBitmap(Context context, Bitmap bitmap, Info info) {
        this.info = info;
        init(bitmap);
    }

    @Deprecated
    public TJBitmap(Context context, Bitmap bitmap, int type) {
        this.info = new Info(createTitle(), type);
        init(bitmap);
    }

    public TJBitmap(Info info) {
        this.info = info;
    }

    public TJBitmap(Bitmap bitmap, Info info) {
        this.info = info;
        init(bitmap);
    }

    public TJBitmap(Bitmap bitmap, int type) {
        this.info = new Info(createTitle(), type);
        init(bitmap);
    }

    public TJBitmap(Bitmap showBitmap, Bitmap rgbBitmap, Bitmap maskBitmap) {
        this.info = new Info(createTitle(), 2);
        ToolFileUtil.saveBitmap2Temp(rgbBitmap, getRGBName());
        ToolFileUtil.saveBitmap2Temp(maskBitmap, getMaskName());
        ToolBitmapCache.getInstance().addBitmap(info.getTitle(), showBitmap);
    }

    private void init(Bitmap bitmap) {
        if (bitmap == null) return;
        bitmap = BitmapUtil.scaleBitmapStandard(bitmap);
        switch (info.getType()) {
            case 0:
                ToolFileUtil.saveBitmap2Temp(bitmap, "rgba" + info.getTitle());
                break;
            case 1:
                ToolFileUtil.savePNGBitmap2Temp(bitmap, "rgba" + info.getTitle());
                break;
            case 2:
                Bitmap rgbBitmap = TJCAdapter.getRgbaImageWithSource(bitmap);
                Bitmap alphaBitmap = TJCAdapter.getMaskImageWithSource(bitmap);
                ToolFileUtil.saveBitmap2Temp(rgbBitmap, getRGBName());
                ToolFileUtil.saveBitmap2Temp(alphaBitmap, getMaskName());
                ToolBitmapCache.getInstance().addBitmap(info.getTitle(), bitmap);
                break;
        }
    }

    /**
     * 设置可以undo，目前只支持update
     */
    public void setUndoAble() {
        if (mMasks != null) return;
        change2Couple();
        mMasks = new ArrayList<>();
        String undoName=getMaskName()+"undo0";
        mMasks.add(undoName);
        ToolFileUtil.saveBitmap2Temp(getMaskBitmap(),undoName);
        mUndoAble = true;
        mMaskIndex = 0;
    }

    public void resetUndo(){
        if(!mUndoAble) return;
        for (int i=1;i<mMasks.size();i++) {
            ToolFileUtil.deleteJPGByName(mMasks.get(i));
        }
        String or=mMasks.get(0);
        mMasks.clear();
        mMasks.add(or);
        Bitmap preMask= ToolFileUtil.getBitmapInTempByName(or);
        ToolFileUtil.saveBitmap2Temp(preMask,getMaskName());
        mMaskIndex = 0;
        ToolBitmapCache.getInstance().deleteBitmap(info.getTitle());
    }

    /**
     * @return 是否还能undo
     */
    public boolean undo(){
        if(!mUndoAble) return false;
        if(mMaskIndex==0)return false;
        mMaskIndex--;
        Bitmap preMask= ToolFileUtil.getBitmapInTempByName(mMasks.get(mMaskIndex));
        ToolFileUtil.saveBitmap2Temp(preMask,getMaskName());
        ToolBitmapCache.getInstance().deleteBitmap(info.getTitle());
        if(mMaskIndex==0)return false;
        return true;
    }
    /**
     * @return 是否还能next
     */
    public boolean next(){
        if(!mUndoAble) return false;
        if(mMaskIndex==mMasks.size()-1)return false;
        mMaskIndex++;
        Bitmap nextMask= ToolFileUtil.getBitmapInTempByName(mMasks.get(mMaskIndex));
        ToolFileUtil.saveBitmap2Temp(nextMask,getMaskName());
        ToolBitmapCache.getInstance().deleteBitmap(info.getTitle());
        if(mMaskIndex==mMasks.size()-1)return false;
        return true;
    }

    public void updateInfo(Info info) {
        this.info = info;
    }

    public void change2Couple() {
        if (info.getType() == 2) return;
        Bitmap bitmap = getShowBitmap();
        info.setType(2);
        Bitmap rgbBitmap = TJCAdapter.getRgbaImageWithSource(bitmap);
        Bitmap alphaBitmap = TJCAdapter.getMaskImageWithSource(bitmap);

        ToolFileUtil.saveBitmap2Temp(rgbBitmap, getRGBName());
        ToolFileUtil.saveBitmap2Temp(alphaBitmap, getMaskName());
    }

    public static String createTitle() {
        return ToolFileUtil.getSystemTimeMillis() + "c0m0";
    }

    public Info getInfo() {
        return info;
    }

    public String getRGBName() {
        if (info == null) return null;
        return "rgba" + info.getTitle();
    }

    public String getMaskName() {
        if (info == null) return null;
        return "mask" + info.getTitle();
    }

    public Bitmap getShowBitmap() {
        Bitmap bitmap = null;
        switch (info.getType()) {
            case 0:
                bitmap = ToolFileUtil.getBitmapInTempByName("rgba" + info.getTitle());
                break;
            case 1:
                bitmap = ToolFileUtil.getPNGBitmapInTempByName("rgba" + info.getTitle());
                break;
            case 2:
                bitmap = ToolBitmapCache.getInstance().getBitmap(info.getTitle());
                if(bitmap!=null) break;
                Bitmap rgb = getRGBBitmap();
                Bitmap mask = getMaskBitmap();
                if (rgb == null || mask == null) return null;
                bitmap = TJCAdapter.combineRgbAndMask(rgb, mask);
                ToolBitmapCache.getInstance().addBitmap(info.getTitle(),bitmap);
                break;
        }
        return bitmap;
    }

    public Bitmap getRGBBitmap() {
        if (info.getType() == 1) return null;
        return ToolFileUtil.getBitmapInTempByName(getRGBName());
    }

    public Bitmap getMaskBitmap() {
        if (info.getType() != 2) return null;
        Bitmap bitmap = ToolFileUtil.getBitmapInTempByName(getMaskName());
        return bitmap;
    }

//
//    public Bitmap writeMaskchannelByIndex(int index, Bitmap bitmap) {
//        if (info.getType() != 2) return null;
//        return ImageBasicOperation.writeMaskchannelWithIndex(getMaskBitmap(), bitmap, index);
//    }

    //modify更新时间戳和'm0'，判断是否删除原bitmap
    public void modifyRGBAndMask(Bitmap rgbBitmap, Bitmap maskBitmap) {
        switch (info.getType()) {
            case 0:
                modifyBitmap(TJCAdapter.combineRgbAndMask(rgbBitmap, maskBitmap));
                break;
            case 1:
                modifyBitmap(TJCAdapter.combineRgbAndMask(rgbBitmap, maskBitmap));
                break;
            case 2:
                String title = info.getTitle();
                String newTitle = ToolFileUtil.getSystemTimeMillis() + title.substring(title.indexOf("c"), title.indexOf("m")) + "m0";
                ToolFileUtil.saveBitmap2Temp(rgbBitmap, "rgba" + newTitle);
                ToolFileUtil.saveBitmap2Temp(maskBitmap, "mask" + newTitle);
                info.setTitle(newTitle);
                break;
        }
    }

    public void modifyRGBBitmap(Bitmap rgbBitmap) {
        String title = info.getTitle();
        String newTitle = ToolFileUtil.getSystemTimeMillis() + title.substring(title.indexOf("c"), title.indexOf("m")) + "m0";
        ToolFileUtil.saveBitmap2Temp(rgbBitmap, "rgba" + newTitle);
        if (info.getType() == 2)
            ToolFileUtil.copyFile("mask" + info.getTitle() + ".jpg", "mask" + newTitle + ".jpg");
        info.setTitle(newTitle);
    }

    public void modifyMaskBitmap(Bitmap maskBitmap) {
        String title = info.getTitle();
        String newTitle = ToolFileUtil.getSystemTimeMillis() + title.substring(title.indexOf("c"), title.indexOf("m")) + "m0";
        ToolFileUtil.saveBitmap2Temp(maskBitmap, "mask" + newTitle);
        ToolFileUtil.copyFile("rgba" + info.getTitle() + ".jpg", "rgba" + newTitle + ".jpg");
        info.setTitle(newTitle);
    }

    public void modifyBitmap(Bitmap bitmap) {
        String title = info.getTitle();
        String newTitle = ToolFileUtil.getSystemTimeMillis() + title.substring(title.indexOf("c"), title.indexOf("m")) + "m0";
        switch (info.getType()) {
            case 0:
                ToolFileUtil.saveBitmap2Temp(bitmap, "rgba" + newTitle);
                break;
            case 1:
                ToolFileUtil.savePNGBitmap2Temp(bitmap, "rgba" + newTitle);
                break;
        }
        info.setTitle(newTitle);
    }

    //update只更新不改名
    public void updateBitmap(Bitmap bitmap) {
        switch (info.getType()) {
            case 0:
                ToolFileUtil.saveBitmap2Temp(bitmap, getRGBName());
                break;
            case 1:
                ToolFileUtil.savePNGBitmap2Temp(bitmap, getRGBName());
                break;
            case 2:
                return;
        }
    }

    public void updateRGBBitmap(Bitmap bitmap) {
        if (info.getType() == 1) return;
        ToolFileUtil.saveBitmap2Temp(bitmap, getRGBName());
        ToolBitmapCache.getInstance().deleteBitmap(info.getTitle());
    }

    public void updateMaskBitmap(Bitmap bitmap) {
        if (info.getType() != 2) return;
        if(mUndoAble){
            int size=mMasks.size();
            for (int i=mMaskIndex+1;i<size;i++) {
                String removeName=getMaskName()+"undo"+i;
                mMasks.remove(removeName);
                ToolFileUtil.deleteJPGByName(removeName);
            }
            mMaskIndex++;
            String undoName=getMaskName()+"undo"+mMaskIndex;
            mMasks.add(undoName);
            ToolFileUtil.saveBitmap2Temp(bitmap, undoName);
        }
        ToolFileUtil.saveBitmap2Temp(bitmap, getMaskName());
        ToolBitmapCache.getInstance().deleteBitmap(info.getTitle());
    }

    //当title以'm0'结尾才执行
    public void delete() {
        String title = info.getTitle();
        boolean needDelete = title.endsWith("m0");
        if (!needDelete) return;
        switch (info.getType()) {
            case 0:
                ToolFileUtil.deleteJPGByName("rgba" + info.getTitle());
                break;
            case 1:
                ToolFileUtil.deletePNGByName("rgba" + info.getTitle());
                break;
            case 2:
                ToolFileUtil.deleteJPGByName(getRGBName());
                ToolFileUtil.deleteJPGByName(getMaskName());
                break;
        }

    }

    public TJBitmap copyTJBitmap() {
        String title = info.getTitle();
        String newTitle = ToolFileUtil.getSystemTimeMillis() + title.substring(title.indexOf("c"), title.indexOf("m")) + "m0";
        switch (info.getType()) {
            case 0:
                ToolFileUtil.copyFile("rgba" + info.getTitle() + ".jpg", "rgba" + newTitle + ".jpg");
                break;
            case 1:
                ToolFileUtil.copyFile("rgba" + info.getTitle() + ".png", "rgba" + newTitle + ".png");
                break;
            case 2:
                ToolFileUtil.copyFile("rgba" + info.getTitle() + ".jpg", "rgba" + newTitle + ".jpg");
                ToolFileUtil.copyFile("mask" + info.getTitle() + ".jpg", "mask" + newTitle + ".jpg");
                break;
        }
        Info newInfo = new Info(newTitle, info.getType());
        ToolBitmapCache.getInstance().addBitmap(info.getTitle(),getShowBitmap());
        return new TJBitmap(newInfo);
    }

    public void renameByCreatorId(String creatorId) {
        renameById(creatorId, null);
    }

    public void renameByModifierId(String modifierId) {
        renameById(null, modifierId);
    }

    public void renameById(String creatorId, String modifierId) {
        String title = info.getTitle();
        if (creatorId != null & title.contains("c0")) {
            title = title.replace(title.substring(title.indexOf("c"), title.indexOf("m")), "c" + creatorId);
        }
        if (modifierId != null & title.contains("m0")) {
            title = title.replace(title.substring(title.indexOf("m"), title.length()), "m" + modifierId);
        }
        if (title.equals(info.getTitle())) return;

        switch (info.getType()) {
            case 0:
                ToolFileUtil.renameFile("rgba" + info.getTitle() + ".jpg", "rgba" + title + ".jpg");
                info.setTitle(title);
                break;
            case 1:
                ToolFileUtil.renameFile("rgba" + info.getTitle() + ".png", "rgba" + title + ".png");
                info.setTitle(title);
                break;
            case 2:
                String orRGBName = getRGBName();
                String orAlphaName = getMaskName();
                info.setTitle(title);
                ToolFileUtil.renameFile(orRGBName + ".jpg", getRGBName() + ".jpg");
                ToolFileUtil.renameFile(orAlphaName + ".jpg", getMaskName() + ".jpg");
                break;
        }

    }

    //    蒙版临时图片
    private String backMaskTempName = "backMaskTempImage";
    private String mrgbMaskTempName = "mrgbMaskTempImage";

    public void createBackBitmap(Bitmap bitmap) {
        ToolFileUtil.savePNGBitmap2Temp(bitmap, backMaskTempName);
    }

    public void createRgbMaskBitmap(Bitmap bitmap) {
        ToolFileUtil.savePNGBitmap2Temp(bitmap, mrgbMaskTempName);
    }

    public Bitmap getBackBitmap() {
        return ToolFileUtil.getPNGBitmapInTempByName(backMaskTempName);
    }

    public Bitmap getRgbMaskBitmap() {
        return ToolFileUtil.getPNGBitmapInTempByName(mrgbMaskTempName);
    }


    //黑白特效需要 防止oom
    private String temAlphaBitmap = "temAlphaBitmap";

    public void saveTemAlpha(Bitmap alphaBitmap) {
        ToolFileUtil.saveBitmap2Temp(alphaBitmap, temAlphaBitmap);
    }

    public Bitmap getTemAlpha() {
        return ToolFileUtil.getBitmapInTempByName(temAlphaBitmap);
    }

}
