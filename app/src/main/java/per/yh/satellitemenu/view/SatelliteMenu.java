package per.yh.satellitemenu.view;

import android.content.Context;
import android.content.res.TypedArray;
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
    /**
     * 枚举菜单可能的位置
     */
    enum Position {
        LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM
    }

    private Position mPosition;//菜单在屏幕上的位置
    private int mRadio;//菜单半径
    private boolean isOpen;//菜单打开状态

    public SatelliteMenu(Context context) {
        this(context, null);
    }

    public SatelliteMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SatelliteMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRadio = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());
        mPosition = Position.LEFT_BOTTOM;
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
        }
    }

    /**
     * 定位主按钮
     */
    private void layoutMainButton() {
        final RelativeLayout mainBtn = (RelativeLayout) getChildAt(0);
        //主按钮左上位置坐标
        int l = 0;
        int t = 0;
        //主按钮高和宽
        final int width = mainBtn.getMeasuredWidth();
        final int height = mainBtn.getMeasuredHeight();
        switch (mPosition) {
            case LEFT_TOP:
                break;
            case LEFT_BOTTOM:
                t = getMeasuredHeight() - height;
                break;
            case RIGHT_TOP:
                l = getMeasuredWidth() - width;
                break;
            case RIGHT_BOTTOM:
                l = getMeasuredWidth() - width;
                t = getMeasuredHeight() - height;
                break;
            default:
                break;
        }
        mainBtn.layout(l, t, l + width, t + height);

        final int mainBtnLeft = l;
        final int mainBtnTop = t;
        //主按钮点击事件
        mainBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //主按钮旋转
                RotateAnimation rotateAnimation = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setDuration(1000);
                mainBtn.setAnimation(rotateAnimation);
                rotateAnimation.startNow();
                if (!isOpen) {
                    int offset = 100;
                    int startOffset = 0;
                    for (int i = 1; i < getChildCount(); i++) {
                        ImageView imageView = (ImageView) getChildAt(i);
                        imageView.setVisibility(View.VISIBLE);
                        AnimationSet set = new AnimationSet(true);
                        AlphaAnimation alpha = new AlphaAnimation(0, 1);
                        alpha.setDuration(800);
                        ScaleAnimation scale = new ScaleAnimation(0.1f, 1, 0.1f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        scale.setDuration(800);
                        RotateAnimation rotate = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        rotate.setDuration(800);
                        TranslateAnimation translate = new TranslateAnimation(mainBtnLeft - imageView.getLeft(),
                                0, mainBtnTop -  imageView.getTop(), 0);
                        translate.setDuration(800);
                        set.addAnimation(alpha);
                        set.addAnimation(scale);
                        set.addAnimation(rotate);
                        set.addAnimation(translate);
                        set.setStartOffset(startOffset);
                        startOffset += offset;
                        imageView.setAnimation(set);
                        set.start();
                    }
                    isOpen = true;
                } else {
                    int offset = 100;
                    int startOffset = 0;
                    for (int i = 1; i < getChildCount(); i++) {
                        ImageView imageView = (ImageView) getChildAt(i);
                        AnimationSet set = new AnimationSet(true);
                        AlphaAnimation alpha = new AlphaAnimation(1, 0);
                        alpha.setDuration(800);
                        ScaleAnimation scale = new ScaleAnimation(1, 0.1f, 1, 0.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        scale.setDuration(800);
                        RotateAnimation rotate = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        rotate.setDuration(800);
                        TranslateAnimation translate = new TranslateAnimation(0, mainBtnLeft - imageView.getLeft(),
                                0, mainBtnTop - imageView.getTop());
                        translate.setDuration(800);
                        set.addAnimation(alpha);
                        set.addAnimation(scale);
                        set.addAnimation(rotate);
                        set.addAnimation(translate);
                        set.setStartOffset(startOffset);
                        startOffset += offset;
                        imageView.setAnimation(set);
                        set.start();
                        imageView.setVisibility(View.INVISIBLE);
                    }
                    isOpen = false;
                }
            }
        });
        //定位子按钮
        layoutSubButton(l, t);
    }

    /**
     * 定位子菜单
     * @param l 主按钮的左上x坐标
     * @param t 主按钮的左上y坐标
     */
    private void layoutSubButton(int l, int t) {
        final int subMenuCount = getChildCount() - 1;//子菜单个数
        double startAngle = 0;//子菜单项的起始角度
        switch (mPosition) {
            case LEFT_TOP:
                startAngle = 0;
                break;
            case LEFT_BOTTOM:
                startAngle = Math.PI * 3 / 2;
                break;
            case RIGHT_TOP:
                startAngle = Math.PI / 2;
                break;
            case RIGHT_BOTTOM:
                startAngle = Math.PI;
                break;
            default:
                break;
        }

        double angle = Math.PI/(2 * (subMenuCount-1));//子菜单之间的夹角
        int x , y;//子菜单的左上坐标
        for(int i=0; i< subMenuCount; i ++) {
            x = l +  (int) (mRadio *Math.cos(startAngle));
            y = t + (int) (mRadio*Math.sin(startAngle));
            ImageView subMenuItem = (ImageView) getChildAt(i+1);
            subMenuItem.setVisibility(View.INVISIBLE);
            subMenuItem.layout(x, y, x + subMenuItem.getMeasuredWidth(), y + subMenuItem.getMeasuredHeight());
            startAngle += angle;
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
