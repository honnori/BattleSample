package com.example.battlesample;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements OnTouchListener{

	private BattleLayout vgroup;

	// カードの表示部品（card.xml）
	private View viewCard = null;

	// カード詳細表示部品(carddetail.xml)
	private View viewCardDerail = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
		
		this.displayCards(30, 10);
		
		
	}
	
	/**
	 * 手札カードを表示する
	 * @param left
	 * @param top
	 */
	public void displayCards(int left, int top) {
		
		
		// CARD用View取得
		this.viewCard = ((LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.card, null);
		
		// OntouchListnerにActivityクラスを設定
		this.viewCard.setOnTouchListener(this);
		
		// カードを長押しした場合のイベントリスナ
		final Activity act = this;
		this.viewCard.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				
				// ボタン長押しでカード詳細画面を表示
				viewDetailCards(40, 10);
				
				return false;
			}
		});

		// Densityの値を取得
		float tmpDensity = this.getResources().getDisplayMetrics().density;
		
		BattleLayout.LayoutParams cartParams = new BattleLayout.LayoutParams(
				(int)(this.getResources().getDimensionPixelSize(R.dimen.card_width)),
				(int)(this.getResources().getDimensionPixelSize(R.dimen.card_height)));
		cartParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		cartParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		cartParams.setMargins((int)(left*tmpDensity), (int)(top*tmpDensity), 0, 0);
		
		// 戦闘画面のベース部品を取得
		BattleLayout vgroup = (BattleLayout)this.findViewById(R.id.battle_base_layout);

		// 戦闘ベース部品にcard追加する
		vgroup.addView(this.viewCard, cartParams);
		
	}
	
	
	/**
	 * カードを指定座標に配置する
	 * @param left
	 * @param top
	 */
	private void moveCard(int width, int height) {
		

		Log.d("★", "getAction()" + "■ACTION_MOVE" + " width:" + width + "  height:" + height);

		// Densityの値を取得
		float tmpDensity = this.getResources().getDisplayMetrics().density;

		// カードを押さえてカードより上の座標ずらしたら、カードを上にずらす。
		if (height <= 0) {
			BattleLayout.LayoutParams params = (BattleLayout.LayoutParams)this.viewCard.getLayoutParams();
			params.setMargins((int)(10*tmpDensity), (int)((380 - 20)* tmpDensity), 0, 0);
			this.viewCard.setLayoutParams(params);

		}

		// カードを押さえてカードより下の座標にずらしたらカードを元の位置にに戻す
		if (height >= viewCard.getHeight()) {
			BattleLayout.LayoutParams params = (BattleLayout.LayoutParams)this.viewCard.getLayoutParams();
			params.setMargins((int)(10*tmpDensity), (int)((380)* tmpDensity), 0, 0);
			this.viewCard.setLayoutParams(params);
		}
		this.viewCard.invalidate();

	}
	
	
	/**
	 * カード詳細を画面に表示する
	 * @param left
	 * @param top
	 */
	public void viewDetailCards(int left, int top) {
		

		// Densityの値を取得
		float tmpDensity = this.getResources().getDisplayMetrics().density;

		// 戦闘画面のベース部品を取得
		this.vgroup = (BattleLayout)this.findViewById(R.id.battle_base_layout);

		// CARD用View取得
		this.viewCardDerail = ((LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.carddetail, null);
		
		BattleLayout.LayoutParams cartParams = new BattleLayout.LayoutParams(
				BattleLayout.LayoutParams.WRAP_CONTENT,
				BattleLayout.LayoutParams.WRAP_CONTENT);
		cartParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		cartParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		cartParams.setMargins((int)(left*tmpDensity), (int)(top*tmpDensity), 0, 0);
		

		// 戦闘ベース部品にカード詳細を追加する
		this.vgroup.addView(this.viewCardDerail, cartParams);
		
	}
	
	/**
	 * カード詳細を消す
	 */
	private void invisibleCardDetail() {
		
		if (vgroup == null) {
			return;
		}
		// 親グループからカード詳細を外す
		this.vgroup.removeView(this.viewCardDerail);
		// 画面再描画を要求
		this.vgroup.invalidate();
	}



	@Override
	public boolean onTouch(View view, MotionEvent event) {
		
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
			this.invisibleCardDetail();
			
			break;
		case MotionEvent.ACTION_MOVE:
			
			// ボタンが押されている場所にカードを移動する。
			this.moveCard((int)(event.getX()), (int)(event.getY()));
			
			break;
		}
		return false;
	}

	
	// カードが移動するアニメーションを作ってみる　利用したのは、ハンドラーの遅延でスレッド仕込むやつ。
	
	private boolean status = false;
	
	// ハンドラーを取得
	private Handler mHandler = new Handler();
	
	// 移動の開始位置と終了位置を設定しておく
	private int startPosLeft = 0;
	private int startPosTop = 0;
	private int stopPosLeft = 0;
	private int stopPosTop = 0;

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

			BattleLayout.LayoutParams params = (BattleLayout.LayoutParams)viewCard.getLayoutParams();
			
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
			viewCard.setLayoutParams(params);
			viewCard.invalidate();
		}
	};

	/**
	 * カード移動開始
	 */
	private void startMovingCard(int startX, int startY, int stopX, int stopY){
		
		this.startPosLeft = startX;
		this.startPosTop = startY;
		this.stopPosLeft = stopX;
		this.stopPosTop = stopY;

		// 新たなハンドラを追加する前に、ハンドラにある既存のコールバックをすべて削除
		this.mHandler.removeCallbacks(this.mUpdateTimeTask);

		// Handler に対し、" 100 ms 後に mUpdateTimeTask() を呼び出す
		this.mHandler.postDelayed(this.mUpdateTimeTask, 10);
		
		
	}
	
	/**
	 * カード移動停止(途中も可能)
	 */
	private void stopMovingCard(){
		
		// キューに溜まって待ち状態のコールバックイベントをキャンセルする
		this.mHandler.removeCallbacks(this.mUpdateTimeTask);
		
		status = false;
		
	}
	
	/**
	 * 配るボタン押下時に呼ばれる
	 * @param v
	 */
	public void onClickButton(View v) {
		
		// 移動する的なやつにする。
		this.startMovingCard(30, 10, 10, 380);
		
	}


}
