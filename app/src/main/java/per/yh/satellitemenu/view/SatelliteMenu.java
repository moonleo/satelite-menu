package per.yh.satellitemenu.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
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

import per.yh.satellitemenu.R;

/**
 * Created by MoonLeo on 2015/12/29.
 * 卫星菜单View类
 */
public class SatelliteMenu extends ViewGroup implements View.OnClickListener {
    private static final String TAG = SatelliteMenu.class.getSimpleName();

    /**
     * 枚举菜单可能的位置
     */
    enum Position {
        LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM
    }

    private Position mPosition;//菜单在屏幕上的位置
    private int mRadius;//菜单半径
    private boolean isOpen;//菜单打开状态

    private RelativeLayout mainBtn;//主按钮

    private int mainBtnLeft;//主按钮左上x坐标
    private int mainBtnTop;//主按钮左上y坐标

    public SatelliteMenu(Context context) {
        this(context, null);
    }

    public SatelliteMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SatelliteMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.satellitemenu);
//        int r = (int) a.getDimension(R.styleable.satellitemenu_radius, 150);
        mRadius = (int) a.getDimension(R.styleable.satellitemenu_radius, 150);//(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, r, getResources().getDisplayMetrics());
        int pos = a.getInt(R.styleable.satellitemenu_position, 0);
//        Log.d(TAG, pos+"");
        switch (pos) {
            case 0:
                mPosition = Position.LEFT_TOP;
                break;
            case 1:
                mPosition = Position.LEFT_BOTTOM;
                break;
            case 2:
                mPosition = Position.RIGHT_TOP;
                break;
            case 3:
                mPosition = Position.RIGHT_BOTTOM;
                break;
            default:
                mPosition = Position.LEFT_TOP;
                break;
        }
        a.recycle();
        Log.d(TAG, mPosition+"");


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
            layoutMainButton(500);
        }
    }

    /**
     * 定位主按钮
     */
    private void layoutMainButton(final long duration) {
        mainBtn = (RelativeLayout) getChildAt(0);

        int l = 0;//主按钮左上位置x坐标
        int t = 0;//主按钮左上位置y坐标

        final int width = mainBtn.getMeasuredWidth();//主按钮宽
        final int height = mainBtn.getMeasuredHeight();//主按钮高
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
        mainBtn.layout(l, t, l + width, t + height);//测量主按钮

        mainBtnLeft = l;
        mainBtnTop = t;
        //主按钮点击事件
        mainBtn.setOnClickListener(this);
        //定位子按钮
        layoutSubMenu(l, t, duration);
    }

    @Override
    public void onClick(View view) {
        //主按钮旋转
        rotateMainButton(0, 359, 500);
        //切换菜单
        toggleMenu(500);

    }

    /**
     * 切换菜单状态标志位
     */
    private void changeMenuStatus() {
        isOpen = !isOpen;
    }

    /**
     * 定位子菜单
     * @param l 主按钮的左上x坐标
     * @param t 主按钮的左上y坐标
     * @param duration 子菜单项动画持续时间
     */
    private void layoutSubMenu(int l, int t, final long duration) {
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
            x = l +  (int) (mRadius *Math.cos(startAngle));
            y = t + (int) (mRadius *Math.sin(startAngle));
            ImageView subMenuItem = (ImageView) getChildAt(i+1);
            subMenuItem.setVisibility(View.GONE);
            subMenuItem.layout(x, y, x + subMenuItem.getMeasuredWidth(), y + subMenuItem.getMeasuredHeight());
            startAngle += angle;
            final String tag = (String) subMenuItem.getTag();
            //子菜单点击事件，关闭菜单
            subMenuItem.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < subMenuCount; i++) {
                        final ImageView imageView = (ImageView) getChildAt(i + 1);
                        if (!tag.equals(imageView.getTag())) {
                            imageView.setVisibility(View.INVISIBLE);
                        } else {
                            AnimationSet set = new AnimationSet(true);
                            ScaleAnimation scale = new ScaleAnimation(1, 1.5f, 1, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                            scale.setDuration(duration);
                            AlphaAnimation alpha = new AlphaAnimation(1, 0.2f);
                            alpha.setDuration(duration);
                            set.addAnimation(scale);
                            set.addAnimation(alpha);
                            set.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    imageView.setVisibility(View.GONE);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            imageView.startAnimation(set);
                        }
                    }
                    isOpen = false;
                }
            });
        }
    }

    /**
     * 菜单是否已经打开
     * @return 菜单状态
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * 切换菜单
     * @param duration 菜单主按钮动画持续时间
     */
    public void toggleMenu(long duration) {
        for (int i = 1; i < getChildCount(); i++) {
            final ImageView subMenuItem = (ImageView) getChildAt(i);
            subMenuItem.setVisibility(View.VISIBLE);
            AnimationSet set = new AnimationSet(true);
            ScaleAnimation scale;//伸缩动画
            if(!isOpen) {
                scale = new ScaleAnimation(0.3f, 1, 0.3f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            } else {
                scale = new ScaleAnimation(1, 0.3f, 1, 0.3f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            }
            scale.setDuration(duration);

            //旋转动画
            RotateAnimation rotate = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(duration);

            TranslateAnimation translate;//位移动画
            if(!isOpen) {
                translate = new TranslateAnimation(mainBtnLeft - subMenuItem.getLeft(),
                        0, mainBtnTop - subMenuItem.getTop(), 0);
            } else {
                translate = new TranslateAnimation(0, mainBtnLeft - subMenuItem.getLeft(),
                        0, mainBtnTop - subMenuItem.getTop());
            }
            translate.setDuration(duration);

            set.addAnimation(scale);
            set.addAnimation(rotate);
            set.addAnimation(translate);
            set.setStartOffset((i - 1) * 100);
            set.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mainBtn.setClickable(false);
                    subMenuItem.setClickable(false);
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    if(!isOpen) {
                        subMenuItem.setVisibility(View.GONE);
                    }
                    mainBtn.setClickable(true);
                    subMenuItem.setClickable(true);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            subMenuItem.startAnimation(set);
        }
        changeMenuStatus();
    }

    /**
     * 旋转主按钮
     * @param startAngle 起始角度
     * @param endAngle 结束角度
     * @param duration 持续时间
     */
    private void rotateMainButton(int startAngle, int endAngle, long duration) {
        RotateAnimation rotateAnimation = new RotateAnimation(startAngle, endAngle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(duration);
        rotateAnimation.setFillAfter(true);
        mainBtn.startAnimation(rotateAnimation);
    }
}
