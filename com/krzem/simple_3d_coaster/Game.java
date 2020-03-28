package com.krzem.simple_3d_coaster;



import com.jogamp.opengl.GL2;
import java.util.ArrayList;
import java.util.List;



public class Game extends Constants{
	public Main.Main_ cls;
	public List<TrackList> tll;
	public Train train=null;
	public Ground ground;
	public LightList ll;
	public long _tn;
	private long _dt=-1;
	private double _ltm=0;



	public Game(Main.Main_ cls){
		this.cls=cls;
		this.ground=new Ground(this.cls,this);
		this.ll=new LightList(this.cls,this);
		this.tll=new ArrayList<TrackList>();
	}



	public void update(GL2 gl){
		long tm=System.nanoTime();
		if (this._dt==-1){
			this._dt=tm;
		}
		else{
			double dff=(double)(tm-this._dt);
			this._ltm=dff*1e-9;
			this._dt=tm+0;
		}
		this.cls.cam.draw(gl);
		for (TrackList tl:this.tll){
			tl.update(gl);
		}
		// this.ground.update(gl);
		if (this.train!=null){
			this.train.update(gl);
		}
		this._tn=0;
	}



	public void draw(GL2 gl){
		this.tll.get(1).draw(gl);
		// for (TrackList tl:this.tll){
		// 	tl.draw(gl);
		// }
		// this.ground.draw(gl);
		if (this.train!=null){
			this.train.draw(gl);
		}
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		this.cls.glu.gluOrtho2D(0,this.cls.WINDOW_SIZE.width,0,this.cls.WINDOW_SIZE.height);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3f(1,1,1);
		Vector c=new Vector(this.cls.WINDOW_SIZE.width/2,this.cls.WINDOW_SIZE.height/2);
		c.add(-CROSSHAIR_SIZE/2,-CROSSHAIR_SIZE/2/8).vertex(gl);
		c.add(-CROSSHAIR_SIZE/2,CROSSHAIR_SIZE/2/8).vertex(gl);
		c.add(CROSSHAIR_SIZE/2,CROSSHAIR_SIZE/2/8).vertex(gl);
		c.add(CROSSHAIR_SIZE/2,-CROSSHAIR_SIZE/2/8).vertex(gl);
		c.add(-CROSSHAIR_SIZE/2/8,-CROSSHAIR_SIZE/2).vertex(gl);
		c.add(-CROSSHAIR_SIZE/2/8,CROSSHAIR_SIZE/2).vertex(gl);
		c.add(CROSSHAIR_SIZE/2/8,CROSSHAIR_SIZE/2).vertex(gl);
		c.add(CROSSHAIR_SIZE/2/8,-CROSSHAIR_SIZE/2).vertex(gl);
		gl.glEnd();
	}
}