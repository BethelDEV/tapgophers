package com.gamez.tap_gopher.collections;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.gamez.tap_gopher.GameSurfaceView;
import com.gamez.tap_gopher.objs.Star;

import java.util.LinkedList;
import java.util.List;

import funs.gamez.Logz;


public class StarCollection {
	public static final int TN_MAX=5;
	public static List<Star> collection;
	private final Bitmap star_bmp;
	
	private final int t_w,t_h;
	private final GameSurfaceView gsv;
//	private Random random = new Random();
	
	public StarCollection(Bitmap _bmp, GameSurfaceView gsv)
	{
		this.star_bmp=_bmp;
		this.gsv=gsv;
		
		t_w=_bmp.getWidth();
		t_h=_bmp.getHeight();
		
		collection=new LinkedList<Star>();
	}
	
	public void initialize()
	{
		int i = TN_MAX;
		int x=0,y=0;
		
		while(i>0)
		{
			collection.add(new Star(x, y, star_bmp,gsv,true));
			if(x+t_w>=GameSurfaceView.SCREEN_W)
			{
				y+=t_h;
			}
			else 
			{
				x+=t_w;
			}
			i--;
		}
	}
	
	public void reinitialize()
	{
		int i = TN_MAX;
		int x=0,y=0;
		if(collection.size()>0)
		{
			collection.clear();
		}
		while(i>0)
		{
			
			if(x+t_w>=GameSurfaceView.SCREEN_W)
			{
				y+=t_h;
				x=0;
			}
			collection.add(new Star(x, y, star_bmp,gsv,true));
			x+=t_w;
			i--;
		}
	}
	
	public void draw(Canvas canvas,Paint paint)
	{
		for(Star t:collection)
		{
			t.draw(canvas, paint);
		}
	}
	
	public void logic()
	{
		for(Star t:collection)
		{
			t.logic();
		}
		if(collection.size()<=0)
		{
			final int gameState = null!= gsv.getGameController() && gsv.getGameController().onGameOverConfirm() ?
					GameSurfaceView.GAME_PAUSE : GameSurfaceView.GAME_OVER;
			gsv.setGameState(gameState);
//			GameSurfaceView.gameState =GameSurfaceView.GAME_OVER;
			Logz.i("StarCollection","GAME_OVER or GAME_PAUSE : "+ gameState);
		}
	}
	
	public static void reduce()
	{
		int index = collection.size()-1;
		if (index<0) return;

		collection.remove(index);
		Star.play();
	}
}
