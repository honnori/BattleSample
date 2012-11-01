package com.example.battlesample;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class BattleLayout extends RelativeLayout {

	public BattleLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public BattleLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BattleLayout(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		
		// ここでtrueを返せば親ViewのonTouchEvent
		// ここでfalseを返せば子ViewのonClickやらonLongClickやら
//		return false;
		return super.onInterceptTouchEvent(ev);
	}

}
