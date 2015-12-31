package per.yh.satellitemenu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by MoonLeo on 2015/12/29.
 * 卫星菜单View类
 */
public class SatelliteMenu extends ViewGroup{

    public SatelliteMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
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
            layoutMainButton();

        }
    }

    /**
     * 定位主按钮
     */
    private void layoutMainButton() {
        RelativeLayout mainBtn = (RelativeLayout) getChildAt(0);
        int l = 0;
        int t = 0;
        int width = mainBtn.getMeasuredWidth();
        int height = mainBtn.getMeasuredHeight();
        mainBtn.layout(l, t, l + width, t + height);
    }
}
