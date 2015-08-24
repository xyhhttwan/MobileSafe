package com.xb.mobilesafe.ui.textview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 自定义有焦点的textView
 * @author baixb
 *
 */
public class FocusedTextView extends TextView {

	public FocusedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public FocusedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FocusedTextView(Context context) {
		super(context);
	}
	
	/**
	 * 当前并没有焦点,欺骗系统有焦点
	 */
	@Override
	public boolean isFocused() {
		return true;
	}
	
	
	
	
}
