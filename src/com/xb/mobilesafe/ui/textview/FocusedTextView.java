package com.xb.mobilesafe.ui.textview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * �Զ����н����textView
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
	 * ��ǰ��û�н���,��ƭϵͳ�н���
	 */
	@Override
	public boolean isFocused() {
		return true;
	}
	
	
	
	
}
