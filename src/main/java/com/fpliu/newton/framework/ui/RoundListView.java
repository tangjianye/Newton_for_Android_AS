package com.fpliu.newton.framework.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fpliu.newton.R;


public class RoundListView extends ListView {
    public RoundListView(Context context) {
        super(context);
    }

    public RoundListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RoundListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
        case MotionEvent.ACTION_DOWN:
                int x = (int) ev.getX();
                int y = (int) ev.getY();
                int itemnum = pointToPosition(x, y);

                if (itemnum == AdapterView.INVALID_POSITION)
                        break;                 
                else{
                	if(itemnum==0){
                        if(itemnum==(getAdapter().getCount()-1)){                                    
                            setSelector(R.drawable.top_round);
                        }else{
                            setSelector(R.drawable.top_round);
                        }
	                }else if(itemnum==(getAdapter().getCount()-1))
	                        setSelector(R.drawable.bottom_round);
	                else{                            
	                    setSelector(R.drawable.no_round);
	                }
                }

                break;
        case MotionEvent.ACTION_UP:
                break;
        }
        
        return super.onInterceptTouchEvent(ev);
    }
}