package per.yh.satellitemenu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by MoonLeo on 2015/12/29.
 * 卫星菜单View类
 */
public class SatelliteMenu extends ViewGroup{

    private int mRadio;//菜单半径

    private boolean isOpen;//菜单是否处于打开状态

    public SatelliteMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRadio = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        for(int i=0; i<getChildCount(); i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(changed) {
            //定位主按钮
            layoutMainButton();
            //定位子按钮
            layoutSubButton(l, t);
        }
    }

    /**
     * 定位主按钮
     */
    private void layoutMainButton() {
        final RelativeLayout mainBtn = (RelativeLayout) getChildAt(0);
        //主按钮左上位置
        int l = 0;
        int t = 0;
        //主按钮高和宽
        final int width = mainBtn.getMeasuredWidth();
        final int height = mainBtn.getMeasuredHeight();
        mainBtn.layout(l, t, l + width, t + height);
        //主按钮点击事件
        mainBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //主按钮旋转
                RotateAnimation rotateAnimation = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setDuration(1000);
                mainBtn.setAnimation(rotateAnimation);
                rotateAnimation.startNow();
                if(!isOpen) {
                    for(int i=1; i<getChildCount(); i++) {
                        ImageView imageView = (ImageView) getChildAt(i);
                        imageView.setVisibility(View.VISIBLE);
                        AnimationSet set = new AnimationSet(true);
                        AlphaAnimation alpha = new AlphaAnimation(0, 1);
                        alpha.setDuration(1500);
                        ScaleAnimation scale = new ScaleAnimation(0.1f, 1, 0.1f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        scale.setDuration(1500);
                        RotateAnimation rotate = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        rotate.setDuration(1500);
                        TranslateAnimation translate = new TranslateAnimation(-imageView.getLeft(), 0, -imageView.getTop(), 0);
                        translate.setDuration(1500);
                        set.addAnimation(alpha);
                        set.addAnimation(scale);
                        set.addAnimation(rotate);
                        set.addAnimation(translate);
                        imageView.setAnimation(set);
                        set.start();
                        isOpen = true;
                    }
                } else {
                    for(int i=1; i<getChildCount(); i++) {
                        ImageView imageView = (ImageView) getChildAt(i);
                        AnimationSet set = new AnimationSet(true);
                        AlphaAnimation alpha = new AlphaAnimation(1, 0);
                        alpha.setDuration(1500);
                        ScaleAnimation scale = new ScaleAnimation(1, 0.1f, 1, 0.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        scale.setDuration(1500);
                        RotateAnimation rotate = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        rotate.setDuration(1500);
                        TranslateAnimation translate = new TranslateAnimation(0, -imageView.getLeft(), 0, -imageView.getTop());
                        translate.setDuration(1500);
                        set.addAnimation(alpha);
                        set.addAnimation(scale);
                        set.addAnimation(rotate);
                        set.addAnimation(translate);
                        imageView.setAnimation(set);
                        set.start();
                        imageView.setVisibility(View.INVISIBLE);
                    }
                    isOpen = false;
                }
            }
        });
    }

    /**
     * 定位子菜单
     * @param l 主按钮的左上x坐标
     * @param t 主按钮的左上y坐标
     */
    private void layoutSubButton(int l, int t) {
        final int subMenuCount = getChildCount() - 1;//子菜单个数
        double start = 0;
        double angle = Math.PI/(2 * (subMenuCount-1));//子菜单之间的夹角
        int x , y;//子菜单的左上坐标
        for(int i=0; i< subMenuCount; i ++) {
            x = l +  (int) (mRadio *Math.cos(start));
            y = t + (int) (mRadio*Math.sin(start));
            ImageView subMenuItem = (ImageView) getChildAt(i+1);
            subMenuItem.setVisibility(View.INVISIBLE);
            subMenuItem.layout(x, y, x + subMenuItem.getMeasuredWidth(), y + subMenuItem.getMeasuredHeight());
            start += angle;
            final String tag = (String) subMenuItem.getTag();
            //子菜单点击事件，关闭菜单
            subMenuItem.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    for(int i=0; i<subMenuCount; i++) {
                        ImageView imageView = (ImageView) getChildAt(i+1);
                        if(!tag.equals(imageView.getTag())) {
                            imageView.setVisibility(View.INVISIBLE);
                        } else {
                            AnimationSet set = new AnimationSet(true);
                            ScaleAnimation scale = new ScaleAnimation(1, 1.5f, 1, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                            scale.setDuration(500);
                            AlphaAnimation alpha = new AlphaAnimation(1, 0.2f);
                            alpha.setDuration(500);
                            set.addAnimation(scale);
                            set.addAnimation(alpha);
                            imageView.setAnimation(set);
                            scale.startNow();
                            imageView.setVisibility(View.INVISIBLE);
                        }
                    }
                    isOpen = false;
                }
            });
        }
    }
}
