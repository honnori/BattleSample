package com.example.battlesample;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class BattleCard extends LinearLayout {

	public BattleCard(Context context) {
		super(context);
	}

	public BattleCard(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		
		int action = event.getAction();

		switch(action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN:
			Log.d("TouchEvent", "getAction()" + "■ACTION_DOWN");
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			Log.d("TouchEvent", "getAction()" + "■ACTION_UP");
			// 詳細画面表示を消す
			((MainActivity)this.activity).invisibleCardDetail();
			
			break;
		case MotionEvent.ACTION_MOVE:
			
			// ボタンが押されている場所にカードを移動する。
			this.moveCard((int)(event.getX()), (int)(event.getY()), this);
			
			break;
		}
		return true;
	}
	
	private Activity activity = null;
	public void setControlActivity(Activity activity) {
		this.activity = activity;
	}
	
	// カードの定位置
	private int basePosLeft = 10;
	private int basePosTop = 380;
	
	/**
	 * カードを指定座標に配置する
	 * @param left
	 * @param top
	 */
	private void moveCard(int width, int height, View view) {
		

		Log.d("★", "getAction()" + "■ACTION_MOVE" + " width:" + width + "  height:" + height);

		// Densityの値を取得
		float tmpDensity = this.getResources().getDisplayMetrics().density;

		// カードを押さえてカードより上の座標ずらしたら、カードを上にずらす。
		if (height <= 0) {
			BattleLayout.LayoutParams params = (BattleLayout.LayoutParams)view.getLayoutParams();
			params.setMargins((int)(this.basePosLeft*tmpDensity), (int)((this.basePosTop - 20)* tmpDensity), 0, 0);
			view.setLayoutParams(params);

		}

		// カードを押さえてカードより下の座標にずらしたらカードを元の位置にに戻す
		if (height >= view.getHeight()) {
			BattleLayout.LayoutParams params = (BattleLayout.LayoutParams)view.getLayoutParams();
			params.setMargins((int)(this.basePosLeft*tmpDensity), (int)((this.basePosTop)* tmpDensity), 0, 0);
			view.setLayoutParams(params);
		}
		view.invalidate();

	}

	// --------------------------------------------------------
	// カード移動する動きを実装する的なやつ
	// --------------------------------------------------------

	private boolean status = false;
	
	// ハンドラーを取得
	private Handler mHandler = new Handler();
	
	// 移動の開始位置と終了位置を設定しておく
	private int startPosLeft = 0;
	private int startPosTop = 0;
	private int stopPosLeft = 0;
	private int stopPosTop = 0;
	
	private View moveView = null;

	private int counter = 0;
	private int baseLeft = 0;
	private int baseTop = 0;
	
	// 定期的に呼び出されるためのRunnnableのインナークラス定義
	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			
			// 移動中の表示回数
			int time = 5;
			
			// 座標移動
			// 状態
			if (status == false) {
				// 最初だけ実施する
				// 座標の差分を算出
				int sabunLeft = stopPosLeft - startPosLeft;
				int sabunTop = stopPosTop - startPosTop;
				baseLeft = sabunLeft/time;
				baseTop = sabunTop/time;
				
				// 状態を（移動）に設定
				status = true;
			}
			
			int posLeft = startPosLeft + counter * baseLeft;
			int posTop = startPosTop + counter * baseTop;

			Log.d("★★★★", "status:" + status + " posLeft:" + posLeft + " posTop:" + posTop);

			// Densityの値を取得
			float tmpDensity = getResources().getDisplayMetrics().density;

			BattleLayout.LayoutParams params = (BattleLayout.LayoutParams)moveView.getLayoutParams();
			
			if (counter <= time) {
				params.setMargins((int)(posLeft*tmpDensity), (int)(posTop*tmpDensity), 0, 0);
				
				// 100ms後に、コールバックメソッド（自メソッド）を実行するように設定
				mHandler.postDelayed(mUpdateTimeTask, 10);
				
				counter++;
			}
			else {
				posLeft = stopPosLeft;
				posTop = stopPosTop;
				params.setMargins((int)(posLeft*tmpDensity), (int)(posTop*tmpDensity), 0, 0);
				
				// 停止処理
				stopMovingCard();
				counter = 0;
			}
			
			// カードの位置確定と表示
			moveView.setLayoutParams(params);
			moveView.invalidate();
		}
	};

	/**
	 * カード移動開始
	 */
	public void startMovingCard(int startX, int startY, int stopX, int stopY){
		
		this.startPosLeft = startX;
		this.startPosTop = startY;
		this.stopPosLeft = stopX;
		this.stopPosTop = stopY;
		this.moveView = this;

		// 新たなハンドラを追加する前に、ハンドラにある既存のコールバックをすべて削除
//		this.mHandler.removeCallbacks(this.mUpdateTimeTask);

		// Handler に対し、" 100 ms 後に mUpdateTimeTask() を呼び出す
		this.mHandler.postDelayed(this.mUpdateTimeTask, 10);
		
		this.basePosLeft = stopX;
		this.basePosTop = stopY;
	}
	
	/**
	 * カード移動停止(途中も可能)
	 */
	private void stopMovingCard(){
		
		// キューに溜まって待ち状態のコールバックイベントをキャンセルする
		this.mHandler.removeCallbacks(this.mUpdateTimeTask);
		
		status = false;
	}

}
