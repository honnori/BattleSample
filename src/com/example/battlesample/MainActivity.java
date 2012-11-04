package com.example.battlesample;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

	private BattleLayout vgroup;

	// カードの表示部品（card.xml）
//	private View viewCard = null;
	private ArrayList<BattleCard> viewCardList = new ArrayList<BattleCard>();

	// カード詳細表示部品(carddetail.xml)
	private View viewCardDerail = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
		
		this.displayCards(38, 18);
		this.displayCards(36, 16);
		this.displayCards(34, 14);
		this.displayCards(32, 12);
		this.displayCards(30, 10);
		
	}
	
	/**
	 * 手札カードを表示する
	 * @param left
	 * @param top
	 */
	public void displayCards(int left, int top) {
		
		
		// CARD用View取得
		View viewCard = ((LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.card, null);
		// カードビュークラスにActivityを渡す
		((BattleCard) viewCard).setControlActivity(this);

		// カードインスタンスを変数として保持する
		this.viewCardList.add((BattleCard)viewCard);
		
		// カードを長押しした場合のイベントリスナ
		viewCard.setOnLongClickListener(new View.OnLongClickListener() {
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
		vgroup.addView(viewCard, cartParams);
		
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
	public void invisibleCardDetail() {
		
		if (vgroup == null) {
			return;
		}
		// 親グループからカード詳細を外す
		this.vgroup.removeView(this.viewCardDerail);
		// 画面再描画を要求
		this.vgroup.invalidate();
	}
	
	
	
	// カードが移動するアニメーションを作ってみる　利用したのは、ハンドラーの遅延でスレッド仕込むやつ。
	
	/**
	 * 配るボタン押下時に呼ばれる
	 * @param v
	 */
	public void onClickButton(View v) {
		
		// スレッド起動
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				try {
					// 5枚移動する的なやつにする。
					viewCardList.get(0).startMovingCard(30, 10, 2, 380);
					Thread.sleep(200);
					viewCardList.get(1).startMovingCard(30, 10, 67, 380);
					Thread.sleep(200);
					viewCardList.get(2).startMovingCard(30, 10, 132, 380);
					Thread.sleep(200);
					viewCardList.get(3).startMovingCard(30, 10, 197, 380);
					Thread.sleep(200);
					viewCardList.get(4).startMovingCard(30, 10, 262, 380);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}


}
