package com.krzem.simple_3d_coaster;



import com.jogamp.opengl.GL2;
import java.lang.Math;
import java.util.ArrayList;



public class Train extends Constants{
	public Main.Main_ cls;
	public Game game;
	public TrackList tl;
	private Vector3 pos;
	private Vector3 tpos;
	private Vector3 up;
	private Vector3 ax;
	private int vi;
	private double doff;
	private double yv;
	private double t;
	private long lt;



	public Train(Main.Main_ cls,Game game,TrackList tl){
		this.cls=cls;
		this.game=game;
		this.tl=tl;
		this._reset();
	}



	public void cam(GL2 gl){
		gl.glLoadIdentity();
		this.cls.glu.gluLookAt(this.pos.x,this.pos.y,this.pos.z,this.ax.x+this.pos.x,this.ax.y+this.pos.y,this.ax.z+this.pos.z,this.up.x,this.up.y,this.up.z);
		// gl.glLoadIdentity();
		// Vector3 r=this.ax.mag(4.5);
		// this.cls.glu.gluLookAt(this.pos.x-r.x,this.pos.y-r.y,this.pos.z-r.z,this.pos.x,this.pos.y,this.pos.z,0,1,0);
	}



	public void update(GL2 gl){
		long ntm=System.nanoTime();
		if (this.lt==-1){
			this.lt=ntm;
			return;
		}
		double dt=(double)(ntm-this.lt)*1e-9;
		double dtm=this._map(dt,0.1,0.2,1,0.5);
		this.lt=ntm;
		this.yv+=-TRAIN_GRAVITY*this.ax.y*dtm;
		this.yv*=(1-TRAIN_FRICTION*dtm);
		this.doff+=this.yv;
		if (this.doff<0){
			this.doff=0;
		}
		if (this.doff>this.tl.tdt.dstl[this.tl.tdt.vl.length-1]){
			this._reset();
			return;
		}
		this.vi=this._get_idx(this.doff);
		if (this.tl.tdt.tsl[this.vi]==1){
			this.yv-=(this.yv-BREAK_TRACK_PIECE_TARGET_SPEED)*BREAK_TRACK_PIECE_EASING_RATIO;
		}
		if (this.tl.tdt.tsl[this.vi]==2){
			this.yv-=(this.yv-LIFT_TRACK_PIECE_TARGET_SPEED)*LIFT_TRACK_PIECE_EASING_RATIO;
		}
		this.t=Math.min(Math.max(this._map(this.doff,this.tl.tdt.dstl[this.vi],this.tl.tdt.dstl[this.vi+1],0,1),0),1);
		this.pos=this._lerp(this.tl.tdt.vl[this.vi][0],this.tl.tdt.vl[this.vi+1][0],this.t);
		this.tpos=this._lerp(this.tl.tdt.vl[this.vi][1],this.tl.tdt.vl[this.vi+1][1],this.t);
		if (this.vi<this.tl.tdt.vl.length-3){
			this.ax=this._lerp(this.tl.tdt.vl[this.vi+1][0],this.tl.tdt.vl[this.vi+2][0],this.t).sub(this._lerp(this.tl.tdt.vl[this.vi][0],this.tl.tdt.vl[this.vi+1][0],this.t)).normalize();
		}
		this.up=this._lerp(this.tl.tdt.vl[this.vi][0],this.tl.tdt.vl[this.vi+1][0],this.t).sub(this._lerp(this.tl.tdt.vl[this.vi][1],this.tl.tdt.vl[this.vi+1][1],this.t)).normalize();
	}



	public void draw(GL2 gl){
		gl.glBegin(GL2.GL_LINES);
		gl.glColor3d(1,0,1);
		this.tpos.vertex(gl);
		this.pos.vertex(gl);
		gl.glEnd();
		gl.glBegin(GL2.GL_TRIANGLES);



		int VA=1;// n of cars
		double VB=1.75;// car sep dist



		for (int i=0;i<VA;i++){
			this._draw_wheel_pair(gl,this.doff-i*VB,i>1,i<VA-1);
		}
		gl.glEnd();
	}



	private void _draw_wheel_pair(GL2 gl,double doff,boolean sc,boolean ec){
		this._draw_wheels(gl,doff+0.5);
		this._draw_wheels(gl,doff-0.5);
	}



	private void _draw_wheels(GL2 gl,double doff){
		// double VA=0.05;// conn buff
		// double VB=0.075;// wheel sep dist
		// double VC=0.03;// depth
		// double VD=0.13;// center off




		// int fidx=this._get_idx(doff+TRACK_PIECE_SMALLER_RADIUS+VB);
		// double ft=Math.min(Math.max(this._map(doff+TRACK_PIECE_SMALLER_RADIUS+VB,this.tl.tdt.dstl[fidx],this.tl.tdt.dstl[fidx+1],0,1),0),1);
		// Vector3 fpos=this._lerp(this.tl.tdt.vl[fidx][1],this.tl.tdt.vl[fidx+1][1],ft);
		// Vector3 fax=this.tl.tdt.vl[fidx+1][0].sub(this.tl.tdt.vl[fidx][0]).normalize();
		// if (fidx<this.tl.tdt.vl.length-3){
		// 	fax=this._lerp(this.tl.tdt.vl[fidx+1][0],this.tl.tdt.vl[fidx+2][0],ft).sub(this._lerp(this.tl.tdt.vl[fidx][0],this.tl.tdt.vl[fidx+1][0],ft)).normalize();
		// }
		// double foff=this._lerp(this.tl.tdt.rzl[fidx],this.tl.tdt.rzl[fidx+1],ft);
		// int bidx=this._get_idx(doff-TRACK_PIECE_SMALLER_RADIUS-VB);
		// double bt=Math.min(Math.max(this._map(doff-TRACK_PIECE_SMALLER_RADIUS-VB,this.tl.tdt.dstl[bidx],this.tl.tdt.dstl[bidx+1],0,1),0),1);
		// Vector3 bpos=this._lerp(this.tl.tdt.vl[bidx][1],this.tl.tdt.vl[bidx+1][1],bt);
		// Vector3 bax=this.tl.tdt.vl[bidx+1][0].sub(this.tl.tdt.vl[bidx][0]).normalize();
		// if (bidx<this.tl.tdt.vl.length-3){
		// 	bax=this._lerp(this.tl.tdt.vl[bidx+1][0],this.tl.tdt.vl[bidx+2][0],bt).sub(this._lerp(this.tl.tdt.vl[bidx][0],this.tl.tdt.vl[bidx+1][0],bt)).normalize();
		// }
		// double boff=this._lerp(this.tl.tdt.rzl[bidx],this.tl.tdt.rzl[bidx+1],bt);
		// Vector3 flo=new Vector3(TRACK_PIECE_ATTACH_BIGGER_WIDTH,0,0).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,Math.PI/2+foff-TRACK_PIECE_ATTACH_TOP_ROT).add(fpos);
		// Vector3 fltop=new Vector3(TRACK_PIECE_SMALLER_RADIUS+TRAIN_WHEEL_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,Math.PI/2+foff);
		// Vector3 fltao=new Vector3(TRACK_PIECE_SMALLER_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(flo.add(fltop));
		// Vector3 fltbo=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(flo.add(fltop));
		// Vector3 flsop=new Vector3(TRACK_PIECE_SMALLER_RADIUS+TRAIN_WHEEL_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff);
		// Vector3 flsao=new Vector3(TRACK_PIECE_SMALLER_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,Math.PI/2+foff).add(flo.add(flsop));
		// Vector3 flsbo=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,Math.PI/2+foff).add(flo.add(flsop));
		// Vector3 flbop=new Vector3(TRACK_PIECE_SMALLER_RADIUS+TRAIN_WHEEL_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,Math.PI/2+foff).inv();
		// Vector3 flbao=new Vector3(TRACK_PIECE_SMALLER_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(flo.add(flbop));
		// Vector3 flbbo=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(flo.add(flbop));
		// Vector3 fro=new Vector3(TRACK_PIECE_ATTACH_BIGGER_WIDTH,0,0).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,Math.PI/2+foff+TRACK_PIECE_ATTACH_TOP_ROT).add(fpos);
		// Vector3 frtop=new Vector3(TRACK_PIECE_SMALLER_RADIUS+TRAIN_WHEEL_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,Math.PI/2+foff);
		// Vector3 frtao=new Vector3(TRACK_PIECE_SMALLER_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(fro.add(frtop));
		// Vector3 frtbo=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(fro.add(frtop));
		// Vector3 frsop=new Vector3(TRACK_PIECE_SMALLER_RADIUS+TRAIN_WHEEL_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff+Math.PI);
		// Vector3 frsao=new Vector3(TRACK_PIECE_SMALLER_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff-Math.PI/2).add(fro.add(frsop));
		// Vector3 frsbo=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff-Math.PI/2).add(fro.add(frsop));
		// Vector3 frbop=new Vector3(TRACK_PIECE_SMALLER_RADIUS+TRAIN_WHEEL_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,Math.PI/2+foff).inv();
		// Vector3 frbao=new Vector3(TRACK_PIECE_SMALLER_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(fro.add(frbop));
		// Vector3 frbbo=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(fro.add(frbop));
		// Vector3 blo=new Vector3(TRACK_PIECE_ATTACH_BIGGER_WIDTH,0,0).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,Math.PI/2+boff-TRACK_PIECE_ATTACH_TOP_ROT).add(bpos);
		// Vector3 bltop=new Vector3(TRACK_PIECE_SMALLER_RADIUS+TRAIN_WHEEL_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,Math.PI/2+boff);
		// Vector3 bltao=new Vector3(TRACK_PIECE_SMALLER_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(blo.add(bltop));
		// Vector3 bltbo=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(blo.add(bltop));
		// Vector3 blsop=new Vector3(TRACK_PIECE_SMALLER_RADIUS+TRAIN_WHEEL_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff);
		// Vector3 blsao=new Vector3(TRACK_PIECE_SMALLER_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,Math.PI/2+boff).add(blo.add(blsop));
		// Vector3 blsbo=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,Math.PI/2+boff).add(blo.add(blsop));
		// Vector3 blbop=new Vector3(TRACK_PIECE_SMALLER_RADIUS+TRAIN_WHEEL_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,Math.PI/2+boff).inv();
		// Vector3 blbao=new Vector3(TRACK_PIECE_SMALLER_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(blo.add(blbop));
		// Vector3 blbbo=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(blo.add(blbop));
		// Vector3 bro=new Vector3(TRACK_PIECE_ATTACH_BIGGER_WIDTH,0,0).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,Math.PI/2+boff+TRACK_PIECE_ATTACH_TOP_ROT).add(bpos);
		// Vector3 brtop=new Vector3(TRACK_PIECE_SMALLER_RADIUS+TRAIN_WHEEL_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,Math.PI/2+boff);
		// Vector3 brtao=new Vector3(TRACK_PIECE_SMALLER_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(bro.add(brtop));
		// Vector3 brtbo=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(bro.add(brtop));
		// Vector3 brsop=new Vector3(TRACK_PIECE_SMALLER_RADIUS+TRAIN_WHEEL_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff+Math.PI);
		// Vector3 brsao=new Vector3(TRACK_PIECE_SMALLER_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff-Math.PI/2).add(bro.add(brsop));
		// Vector3 brsbo=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff-Math.PI/2).add(bro.add(brsop));
		// Vector3 brbop=new Vector3(TRACK_PIECE_SMALLER_RADIUS+TRAIN_WHEEL_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,Math.PI/2+boff).inv();
		// Vector3 brbao=new Vector3(TRACK_PIECE_SMALLER_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(bro.add(brbop));
		// Vector3 brbbo=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(bro.add(brbop));
		// Vector3 flax=fltao.sub(fltbo).normalize();
		// Vector3 flp=new Vector3(TRAIN_WHEEL_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,Math.PI/2+foff);
		// Vector3 fsax=flsao.sub(flsbo).normalize();
		// Vector3 fsp=new Vector3(TRAIN_WHEEL_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff);
		// Vector3 frax=frtao.sub(frtbo).normalize();
		// Vector3 frp=new Vector3(TRAIN_WHEEL_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,Math.PI/2+foff);
		// Vector3 blax=bltao.sub(bltbo).normalize();
		// Vector3 blp=new Vector3(TRAIN_WHEEL_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,Math.PI/2+boff);
		// Vector3 bsax=blsao.sub(blsbo).normalize();
		// Vector3 bsp=new Vector3(TRAIN_WHEEL_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff);
		// Vector3 brax=brtao.sub(brtbo).normalize();
		// Vector3 brp=new Vector3(TRAIN_WHEEL_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,Math.PI/2+boff);
		// gl.glColor3d(0.2,0.2,0.2);
		// this._draw_cylinder(gl,flp,fltao,fltbo,flax,TRAIN_WHEEL_DETAIL);
		// this._draw_cylinder(gl,fsp,flsao,flsbo,fsax,TRAIN_WHEEL_DETAIL);
		// this._draw_cylinder(gl,flp,flbao,flbbo,flax,TRAIN_WHEEL_DETAIL);
		// this._draw_cylinder(gl,frp,frtao,frtbo,frax,TRAIN_WHEEL_DETAIL);
		// this._draw_cylinder(gl,fsp,frsao,frsbo,fsax,TRAIN_WHEEL_DETAIL);
		// this._draw_cylinder(gl,frp,frbao,frbbo,frax,TRAIN_WHEEL_DETAIL);
		// this._draw_cylinder(gl,blp,bltao,bltbo,blax,TRAIN_WHEEL_DETAIL);
		// this._draw_cylinder(gl,bsp,blsao,blsbo,bsax,TRAIN_WHEEL_DETAIL);
		// this._draw_cylinder(gl,blp,blbao,blbbo,blax,TRAIN_WHEEL_DETAIL);
		// this._draw_cylinder(gl,brp,brtao,brtbo,brax,TRAIN_WHEEL_DETAIL);
		// this._draw_cylinder(gl,bsp,brsao,brsbo,bsax,TRAIN_WHEEL_DETAIL);
		// this._draw_cylinder(gl,brp,brbao,brbbo,brax,TRAIN_WHEEL_DETAIL);
		// gl.glColor3d(0.11,0.11,0.11);
		// Vector3 va=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,VA,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(flo.add(fltop));
		// Vector3 vb=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,-VA,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(flo.add(fltop));
		// Vector3 vc=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,-VA,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(blo.add(bltop));
		// Vector3 vd=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,VA,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(blo.add(bltop));
		// Vector3 ve=new Vector3(-TRACK_PIECE_SMALLER_RADIUS-VC,VA,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(flo.add(fltop));
		// Vector3 vf=new Vector3(-TRACK_PIECE_SMALLER_RADIUS-VC,-VA,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(flo.add(fltop));
		// Vector3 vg=new Vector3(-TRACK_PIECE_SMALLER_RADIUS-VC,-VA,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(blo.add(bltop));
		// Vector3 vh=new Vector3(-TRACK_PIECE_SMALLER_RADIUS-VC,VA,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(blo.add(bltop));
		// this._draw_box(gl,va,vb,vc,vd,ve,vf,vg,vh);
		// va=new Vector3(TRACK_PIECE_SMALLER_RADIUS,VA,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(flo.add(fltop));
		// vb=new Vector3(TRACK_PIECE_SMALLER_RADIUS,TRACK_PIECE_SMALLER_RADIUS,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(flo);
		// vc=new Vector3(TRACK_PIECE_SMALLER_RADIUS,TRACK_PIECE_SMALLER_RADIUS,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(blo);
		// vd=new Vector3(TRACK_PIECE_SMALLER_RADIUS,VA,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(blo.add(bltop));
		// ve=new Vector3(TRACK_PIECE_SMALLER_RADIUS+VC,VA,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(flo.add(fltop));
		// vf=new Vector3(TRACK_PIECE_SMALLER_RADIUS+VC,TRACK_PIECE_SMALLER_RADIUS,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(flo);
		// vg=new Vector3(TRACK_PIECE_SMALLER_RADIUS+VC,TRACK_PIECE_SMALLER_RADIUS,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(blo);
		// vh=new Vector3(TRACK_PIECE_SMALLER_RADIUS+VC,VA,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(blo.add(bltop));
		// this._draw_box(gl,va,vb,vc,vd,ve,vf,vg,vh);
		// va=new Vector3(TRACK_PIECE_SMALLER_RADIUS,-VA,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,Math.PI/2+foff).add(flo.add(flsop));
		// vb=new Vector3(TRACK_PIECE_SMALLER_RADIUS,TRACK_PIECE_SMALLER_RADIUS,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(flo);
		// vc=new Vector3(TRACK_PIECE_SMALLER_RADIUS,TRACK_PIECE_SMALLER_RADIUS,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(blo);
		// vd=new Vector3(TRACK_PIECE_SMALLER_RADIUS,-VA,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,Math.PI/2+boff).add(blo.add(blsop));
		// ve=new Vector3(TRACK_PIECE_SMALLER_RADIUS+VC,-VA,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,Math.PI/2+foff).add(flo.add(flsop));
		// vf=new Vector3(TRACK_PIECE_SMALLER_RADIUS,TRACK_PIECE_SMALLER_RADIUS+VC,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(flo);
		// vg=new Vector3(TRACK_PIECE_SMALLER_RADIUS,TRACK_PIECE_SMALLER_RADIUS+VC,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(blo);
		// vh=new Vector3(TRACK_PIECE_SMALLER_RADIUS+VC,-VA,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,Math.PI/2+boff).add(blo.add(blsop));
		// this._draw_box(gl,va,vb,vc,vd,ve,vf,vg,vh);
		// va=new Vector3(TRACK_PIECE_SMALLER_RADIUS,VA,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(fro.add(frtop));
		// vb=new Vector3(TRACK_PIECE_SMALLER_RADIUS,-VA,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(fro.add(frtop));
		// vc=new Vector3(TRACK_PIECE_SMALLER_RADIUS,-VA,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(bro.add(brtop));
		// vd=new Vector3(TRACK_PIECE_SMALLER_RADIUS,VA,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(bro.add(brtop));
		// ve=new Vector3(TRACK_PIECE_SMALLER_RADIUS+VC,VA,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(fro.add(frtop));
		// vf=new Vector3(TRACK_PIECE_SMALLER_RADIUS+VC,-VA,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(fro.add(frtop));
		// vg=new Vector3(TRACK_PIECE_SMALLER_RADIUS+VC,-VA,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(bro.add(brtop));
		// vh=new Vector3(TRACK_PIECE_SMALLER_RADIUS+VC,VA,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(bro.add(brtop));
		// this._draw_box(gl,va,vb,vc,vd,ve,vf,vg,vh);
		// va=new Vector3(TRACK_PIECE_SMALLER_RADIUS,-VA,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(flo.add(flbop));
		// vb=new Vector3(TRACK_PIECE_SMALLER_RADIUS,-TRACK_PIECE_SMALLER_RADIUS,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(flo);
		// vc=new Vector3(TRACK_PIECE_SMALLER_RADIUS,-TRACK_PIECE_SMALLER_RADIUS,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(blo);
		// vd=new Vector3(TRACK_PIECE_SMALLER_RADIUS,-VA,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(blo.add(blbop));
		// ve=new Vector3(TRACK_PIECE_SMALLER_RADIUS+VC,-VA,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(flo.add(flbop));
		// vf=new Vector3(TRACK_PIECE_SMALLER_RADIUS+VC,-TRACK_PIECE_SMALLER_RADIUS,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(flo);
		// vg=new Vector3(TRACK_PIECE_SMALLER_RADIUS+VC,-TRACK_PIECE_SMALLER_RADIUS,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(blo);
		// vh=new Vector3(TRACK_PIECE_SMALLER_RADIUS+VC,-VA,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(blo.add(blbop));
		// this._draw_box(gl,va,vb,vc,vd,ve,vf,vg,vh);
		// va=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,-VA,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,Math.PI/2+foff).add(flo.add(flsop));
		// vb=new Vector3(TRACK_PIECE_SMALLER_RADIUS,-TRACK_PIECE_SMALLER_RADIUS,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(flo);
		// vc=new Vector3(TRACK_PIECE_SMALLER_RADIUS,-TRACK_PIECE_SMALLER_RADIUS,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(blo);
		// vd=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,-VA,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,Math.PI/2+boff).add(blo.add(blsop));
		// ve=new Vector3(-TRACK_PIECE_SMALLER_RADIUS-VC,-VA,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,Math.PI/2+foff).add(flo.add(flsop));
		// vf=new Vector3(TRACK_PIECE_SMALLER_RADIUS,-TRACK_PIECE_SMALLER_RADIUS-VC,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(flo);
		// vg=new Vector3(TRACK_PIECE_SMALLER_RADIUS,-TRACK_PIECE_SMALLER_RADIUS-VC,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(blo);
		// vh=new Vector3(-TRACK_PIECE_SMALLER_RADIUS-VC,-VA,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,Math.PI/2+boff).add(blo.add(blsop));
		// this._draw_box(gl,va,vb,vc,vd,ve,vf,vg,vh);
		// va=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,VA,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(fro.add(frtop));
		// vb=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,TRACK_PIECE_SMALLER_RADIUS,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(fro);
		// vc=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,TRACK_PIECE_SMALLER_RADIUS,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(bro);
		// vd=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,VA,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(bro.add(brtop));
		// ve=new Vector3(-TRACK_PIECE_SMALLER_RADIUS-VC,VA,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(fro.add(frtop));
		// vf=new Vector3(-TRACK_PIECE_SMALLER_RADIUS-VC,TRACK_PIECE_SMALLER_RADIUS,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(fro);
		// vg=new Vector3(-TRACK_PIECE_SMALLER_RADIUS-VC,TRACK_PIECE_SMALLER_RADIUS,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(bro);
		// vh=new Vector3(-TRACK_PIECE_SMALLER_RADIUS-VC,VA,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(bro.add(brtop));
		// this._draw_box(gl,va,vb,vc,vd,ve,vf,vg,vh);
		// va=new Vector3(TRACK_PIECE_SMALLER_RADIUS,VA,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,Math.PI/2+foff).add(fro.add(frsop));
		// vb=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,TRACK_PIECE_SMALLER_RADIUS,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(fro);
		// vc=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,TRACK_PIECE_SMALLER_RADIUS,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(bro);
		// vd=new Vector3(TRACK_PIECE_SMALLER_RADIUS,VA,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,Math.PI/2+boff).add(bro.add(brsop));
		// ve=new Vector3(TRACK_PIECE_SMALLER_RADIUS+VC,VA,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,Math.PI/2+foff).add(fro.add(frsop));
		// vf=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,TRACK_PIECE_SMALLER_RADIUS+VC,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(fro);
		// vg=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,TRACK_PIECE_SMALLER_RADIUS+VC,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(bro);
		// vh=new Vector3(TRACK_PIECE_SMALLER_RADIUS+VC,VA,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,Math.PI/2+boff).add(bro.add(brsop));
		// this._draw_box(gl,va,vb,vc,vd,ve,vf,vg,vh);
		// va=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,-VA,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(fro.add(frbop));
		// vb=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,-TRACK_PIECE_SMALLER_RADIUS,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(fro);
		// vc=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,-TRACK_PIECE_SMALLER_RADIUS,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(bro);
		// vd=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,-VA,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(bro.add(brbop));
		// ve=new Vector3(-TRACK_PIECE_SMALLER_RADIUS-VC,-VA,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(fro.add(frbop));
		// vf=new Vector3(-TRACK_PIECE_SMALLER_RADIUS-VC,-TRACK_PIECE_SMALLER_RADIUS,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(fro);
		// vg=new Vector3(-TRACK_PIECE_SMALLER_RADIUS-VC,-TRACK_PIECE_SMALLER_RADIUS,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(bro);
		// vh=new Vector3(-TRACK_PIECE_SMALLER_RADIUS-VC,-VA,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(bro.add(brbop));
		// this._draw_box(gl,va,vb,vc,vd,ve,vf,vg,vh);
		// va=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,VA,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,Math.PI/2+foff).add(fro.add(frsop));
		// vb=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,-TRACK_PIECE_SMALLER_RADIUS,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(fro);
		// vc=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,-TRACK_PIECE_SMALLER_RADIUS,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(bro);
		// vd=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,VA,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,Math.PI/2+boff).add(bro.add(brsop));
		// ve=new Vector3(-TRACK_PIECE_SMALLER_RADIUS-VC,VA,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,Math.PI/2+foff).add(fro.add(frsop));
		// vf=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,-TRACK_PIECE_SMALLER_RADIUS-VC,VA).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(fro);
		// vg=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,-TRACK_PIECE_SMALLER_RADIUS-VC,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(bro);
		// vh=new Vector3(-TRACK_PIECE_SMALLER_RADIUS-VC,VA,-VA).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,Math.PI/2+boff).add(bro.add(brsop));
		// this._draw_box(gl,va,vb,vc,vd,ve,vf,vg,vh);
		// gl.glColor3d(0.145,0.145,0.145);
		// va=new Vector3(TRACK_PIECE_SMALLER_RADIUS,VA,-VD).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,Math.PI/2+foff).add(fro.add(frsop));
		// vb=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,TRACK_PIECE_SMALLER_RADIUS,-VD).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(fro);
		// vc=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,TRACK_PIECE_SMALLER_RADIUS,VD).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(bro);
		// vd=new Vector3(TRACK_PIECE_SMALLER_RADIUS,VA,VD).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,Math.PI/2+boff).add(bro.add(brsop));
		// ve=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,VA,-VD).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,Math.PI/2+foff).add(fro.add(frsop));
		// vf=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,-TRACK_PIECE_SMALLER_RADIUS,-VD).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(fro);
		// vg=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,-TRACK_PIECE_SMALLER_RADIUS,VD).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(bro);
		// vh=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,VA,VD).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,Math.PI/2+boff).add(bro.add(brsop));
		// this._draw_box(gl,va,vb,vc,vd,ve,vf,vg,vh);
		// va=new Vector3(TRACK_PIECE_SMALLER_RADIUS,-VA,-VD).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,Math.PI/2+foff).add(flo.add(flsop));
		// vb=new Vector3(TRACK_PIECE_SMALLER_RADIUS,TRACK_PIECE_SMALLER_RADIUS,-VD).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(flo);
		// vc=new Vector3(TRACK_PIECE_SMALLER_RADIUS,TRACK_PIECE_SMALLER_RADIUS,VD).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(blo);
		// vd=new Vector3(TRACK_PIECE_SMALLER_RADIUS,-VA,VD).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,Math.PI/2+boff).add(blo.add(blsop));
		// ve=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,-VA,-VD).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,Math.PI/2+foff).add(flo.add(flsop));
		// vf=new Vector3(TRACK_PIECE_SMALLER_RADIUS,-TRACK_PIECE_SMALLER_RADIUS,-VD).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(flo);
		// vg=new Vector3(TRACK_PIECE_SMALLER_RADIUS,-TRACK_PIECE_SMALLER_RADIUS,VD).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(blo);
		// vh=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,-VA,VD).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,Math.PI/2+boff).add(blo.add(blsop));
		// this._draw_box(gl,va,vb,vc,vd,ve,vf,vg,vh);
		// va=new Vector3(TRACK_PIECE_SMALLER_RADIUS,VA,-VD).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(flo.add(fltop));
		// vb=new Vector3(TRACK_PIECE_SMALLER_RADIUS,TRACK_PIECE_SMALLER_RADIUS+TRAIN_WHEEL_RADIUS-VA,-VD).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(flo);
		// vc=new Vector3(TRACK_PIECE_SMALLER_RADIUS,TRACK_PIECE_SMALLER_RADIUS+TRAIN_WHEEL_RADIUS-VA,VD).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(blo);
		// vd=new Vector3(TRACK_PIECE_SMALLER_RADIUS,VA,VD).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(blo.add(bltop));
		// ve=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,VA,-VD).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(fro.add(frtop));
		// vf=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,TRACK_PIECE_SMALLER_RADIUS+TRAIN_WHEEL_RADIUS-VA,-VD).rotate(new Vector3(0,1,0),-Math.atan2(fax.z,fax.x)+Math.PI/2).rotate(fax,foff).add(fro);
		// vg=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,TRACK_PIECE_SMALLER_RADIUS+TRAIN_WHEEL_RADIUS-VA,VD).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(bro);
		// vh=new Vector3(-TRACK_PIECE_SMALLER_RADIUS,VA,VD).rotate(new Vector3(0,1,0),-Math.atan2(bax.z,bax.x)+Math.PI/2).rotate(bax,boff).add(bro.add(brtop));
		// this._draw_box(gl,va,vb,vc,vd,ve,vf,vg,vh);
	}



	private void _draw_box(GL2 gl,Vector3 va,Vector3 vb,Vector3 vc,Vector3 vd,Vector3 ve,Vector3 vf,Vector3 vg,Vector3 vh){
		va.vertex(gl);
		vb.vertex(gl);
		vc.vertex(gl);
		this.game._tn++;
		va.vertex(gl);
		vc.vertex(gl);
		vd.vertex(gl);
		this.game._tn++;
		ve.vertex(gl);
		vf.vertex(gl);
		vg.vertex(gl);
		this.game._tn++;
		ve.vertex(gl);
		vg.vertex(gl);
		vh.vertex(gl);
		this.game._tn++;
		va.vertex(gl);
		vb.vertex(gl);
		vf.vertex(gl);
		this.game._tn++;
		va.vertex(gl);
		vf.vertex(gl);
		ve.vertex(gl);
		this.game._tn++;
		vc.vertex(gl);
		vd.vertex(gl);
		vh.vertex(gl);
		this.game._tn++;
		vc.vertex(gl);
		vh.vertex(gl);
		vg.vertex(gl);
		this.game._tn++;
		va.vertex(gl);
		vd.vertex(gl);
		vh.vertex(gl);
		this.game._tn++;
		va.vertex(gl);
		vh.vertex(gl);
		ve.vertex(gl);
		this.game._tn++;
		vb.vertex(gl);
		vc.vertex(gl);
		vg.vertex(gl);
		this.game._tn++;
		vb.vertex(gl);
		vg.vertex(gl);
		vf.vertex(gl);
		this.game._tn++;
	}



	private void _draw_cylinder(GL2 gl,Vector3 p,Vector3 ao,Vector3 bo,Vector3 ax,int dt){
		if (ax==null){
			ax=ao.sub(bo).normalize();
		}
		for (double i=0;i<Math.PI*2;i+=Math.PI*2/dt){
			Vector3 va=p.rotate(ax,i).add(ao);
			Vector3 vb=p.rotate(ax,i+Math.PI*2/dt).add(ao);
			Vector3 vc=p.rotate(ax,i).add(bo);
			Vector3 vd=p.rotate(ax,i+Math.PI*2/dt).add(bo);
			va.vertex(gl);
			vb.vertex(gl);
			vc.vertex(gl);
			this.game._tn++;
			vb.vertex(gl);
			vc.vertex(gl);
			vd.vertex(gl);
			this.game._tn++;
			va.vertex(gl);
			vb.vertex(gl);
			ao.vertex(gl);
			this.game._tn++;
			vc.vertex(gl);
			vd.vertex(gl);
			bo.vertex(gl);
			this.game._tn++;
		}
	}



	private void _reset(){
		this.doff=0;
		this.yv=0;
		this.lt=-1;
		this.t=0;
		this.pos=this.tl.tdt.vl[0][0].clone();
		this.tpos=this.tl.tdt.vl[0][1].clone();
		this.up=new Vector3(0,1,0);
		this.ax=this.tl.tdt.vl[1][0].sub(this.tl.tdt.vl[0][0]);
		this.vi=0;
	}



	private double _map(double v,double aa,double ab,double ba,double bb){
		return (v-aa)/(ab-aa)*(bb-ba)+ba;
	}



	private double _lerp(double a,double b,double t){
		return a+t*(b-a);
	}



	private Vector3 _lerp(Vector3 a,Vector3 b,double t){
		return new Vector3(a.x+t*(b.x-a.x),a.y+t*(b.y-a.y),a.z+t*(b.z-a.z));
	}



	private int _get_idx(double off){
		for (int i=0;i<this.tl.tdt.vl.length-1;i++){
			if (this.tl.tdt.dstl[i+1]>off){
				return i;
			}
		}
		return this.tl.tdt.vl.length-2;
	}
}