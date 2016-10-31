package com.example.admin.wifihelper.Main.GridView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.admin.wifihelper.Main.mainActivity;

/**
 * Created by admin on 2016/10/17.
 */

public class DragGridView extends GridView{
    int                        x,y;// 按下时的坐标
    int                        moveX, moveY; //移动时手指的坐标
    int                        pointToItemTop, pointToItemLeft; //单击的点距离item上，左的距离

    int                        position_item; //单击的item在gridview里的position

    int                        statusHeight;



    View                       view_item;
    Bitmap                     bitmap;
    ImageView                  DragImageView;// 拖拽时随手指移动的图形对象

    WindowManager              windowManager;
    WindowManager.LayoutParams windowLayoutParams;



    public DragGridView(Context context) {
        this(context, null);
    }

    public DragGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        statusHeight = getStatusHeight(context);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case  MotionEvent.ACTION_DOWN:
                x = (int) event.getRawX();
                y = (int) event.getRawY();

                position_item = pointToPosition(x,y); //根据单击的坐标获得单击的item

                if(position_item == -1 ){// 单击到空白区域的时候停止分发事件
                    return false;
                }


                view_item = getChildAt(position_item - getFirstVisiblePosition()); //获取item的view

                pointToItemLeft = x - view_item.getLeft();
                pointToItemTop = y - view_item.getTop();

                view_item.setDrawingCacheEnabled(true); //开启绘图缓冲
                bitmap = Bitmap.createBitmap(view_item.getDrawingCache()); //获取缓存，绘制bitmap
                view_item.destroyDrawingCache();//释放缓存，避免重复镜像

                createDragImage(bitmap, x, y);

                view_item.setVisibility(INVISIBLE); //隐藏该item

                break;

            case MotionEvent.ACTION_MOVE :

                if(position_item == -1 ){// 单击到空白区域的时候停止分发事件
                    return false;
                }

            case MotionEvent.ACTION_UP:
                moveX = (int) event.getRawX();
                moveY = (int) event.getRawY();
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE :
                moveX = (int) event.getRawX();
                moveY = (int) event.getRawY();


                onDrag(moveX, moveY);
                break;

            case MotionEvent.ACTION_UP :

                stopDrag();
                break;
        }
        return true;
    }

    /*向windowManager中添加随手指移动的imageView对象*/ /**Done*/
    public void createDragImage(Bitmap bitmap, int x, int y){
        /*为将要添加进windowManager的对象描述属性*/
        windowLayoutParams = new WindowManager.LayoutParams();
        windowLayoutParams.format = PixelFormat.TRANSLUCENT; //图片之外的其他地方透明
        windowLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        windowLayoutParams.x = x - pointToItemLeft;
        windowLayoutParams.y = y - pointToItemTop;
        windowLayoutParams.alpha = 0.55f; //透明度
        windowLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        windowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        windowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
               | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE ;

        DragImageView = new ImageView(getContext());
        DragImageView.setImageBitmap(bitmap);

        windowManager.addView(DragImageView, windowLayoutParams); //向windowManager添加对象L
    }

    /*拖动时更新ImageView坐标*//**Done*/
    public void onDrag(int moveX, int moveY){
        windowLayoutParams.x = moveX - pointToItemLeft;
        windowLayoutParams.y = moveY - pointToItemTop;

        windowManager.updateViewLayout(DragImageView,windowLayoutParams);//更新拖动的Image的位置

        getPosition(moveX, moveY);
    }

    /*根据Image的位置判断拖动的控件该插入那个位置*/
    public int getPosition(int moveX, int moveY){
        int num = 0;
        int width = getColumnWidth();

        int[][] Gap_X = {
                {getChildAt(0 - getFirstVisiblePosition()).getLeft() + width, getChildAt(1 - getFirstVisiblePosition()).getLeft()},
                {getChildAt(1 - getFirstVisiblePosition()).getLeft() + width, getChildAt(2 - getFirstVisiblePosition()).getLeft()}
        };

        int[][] Gap_Y = {
                {getChildAt(0 - getFirstVisiblePosition()).getTop(), getChildAt(0 - getFirstVisiblePosition()).getTop() + getChildAt(0 - getFirstVisiblePosition()).getHeight()},
                {getChildAt(3 - getFirstVisiblePosition()).getTop(), getChildAt(3 - getFirstVisiblePosition()).getTop() + getChildAt(3 - getFirstVisiblePosition()).getHeight()},
                {getChildAt(6 - getFirstVisiblePosition()).getTop(), getChildAt(6 - getFirstVisiblePosition()).getTop() + getChildAt(6 - getFirstVisiblePosition()).getHeight()}
        };


        if(moveX > Gap_X[0][0] && moveX < Gap_X[0][1]){
            if(moveY > Gap_Y[0][0] && moveY < Gap_Y[0][1]){
                num = 1;
            }
        }

        if(moveX > Gap_X[1][0] && moveX < Gap_X[1][1]){
            if(moveY > Gap_Y[0][0] && moveY < Gap_Y[0][1]){
                num = 2;
            }
        }

        if(moveX > Gap_X[0][0] && moveX < Gap_X[0][1]){
            if(moveY > Gap_Y[1][0] && moveY < Gap_Y[1][1]){
                num = 3;
            }
        }

        if(moveX > Gap_X[1][0] && moveX < Gap_X[1][1]){
            if(moveY > Gap_Y[1][0] && moveY < Gap_Y[1][1]){
                num = 4;
            }
        }

        if(moveX > Gap_X[0][0] && moveX < Gap_X[0][1]){
            if(moveY > Gap_Y[2][0] && moveY < Gap_Y[2][1]){
                num = 5;
            }
        }

        if(moveX > Gap_X[1][0] && moveX < Gap_X[1][1]){
            if(moveY > Gap_Y[0][0] && moveY < Gap_Y[0][1]){
                num = 6;
            }
        }

        /*mainActivity.makeTaost(String.valueOf(num));*/
        return num;
    }


    /*手指脱离时停止拖动控件*/
    public void stopDrag(){
        removeImage();
        view_item.setVisibility(VISIBLE);
    }

    /*移除拖动的镜像*/
    public void removeImage(){
        if(DragImageView != null){
            windowManager.removeViewImmediate(DragImageView);
            DragImageView = null;
        }

    }

    /*根据新的位置来重新组成各个item的位置*/
    public void onSwapItem(){

    }


    /*获取状态栏高度*/ /**Done*/
    private static int getStatusHeight(Context context){
        int statusHeight = 0;
        Rect localRect = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight){
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = context.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }
}
