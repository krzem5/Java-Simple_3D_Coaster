package com.krzem.simple_3d_coaster - Copy;



import com.jogamp.opengl.GL2;
import java.lang.Math;
import java.util.ArrayList;



public class TrackPiece extends TrackElement{
	public TrackPiece(Main.Main_ cls,Game game,TrackList tl,int tli,Vector3 a,Vector3 b,Vector3 anc,double srot,double erot){
		this.cls=cls;
		this.game=game;
		this.tl=tl;
		this.tli=tli;
		this.a=a;
		this.b=b;
		this.anc=anc;
		this.srot=srot;
		this.erot=erot;
		this.dpa=new DragPoint(this.cls,this.game,this,this.a);
		this.dpb=new DragPoint(this.cls,this.game,this,this.b);
		this.dpanc=new DragPoint(this.cls,this.game,this,this.anc);
		this.rpa=new RotPoint(this.cls,this.game,this,null,null,0);
		this.rpb=new RotPoint(this.cls,this.game,this,null,null,1);
		this._regenerate();
	}



	public void update(GL2 gl){
		this.dpb.enable(this.tli==this.tl.tpl.size()-1);
		this.rpb.enable(this.tli==this.tl.tpl.size()-1);
		this.dpa.update(gl);
		this.dpb.update(gl);
		this.dpanc.update(gl);
		this.rpa.update(gl);
		this.rpb.update(gl);
		this._regenerate();
	}



	public void draw(GL2 gl){
		this._draw_track(gl);
		this.dpa.draw(gl);
		this.dpb.draw(gl);
		this.dpanc.draw(gl);
		this.rpa.draw(gl);
		this.rpb.draw(gl);
	}



	private void _draw_track(GL2 gl){
		gl.glBegin(GL2.GL_TRIANGLES);
		double off=this.srot+0;
		if (this.tli==0){
			gl.glColor3d(this.tl.tcb[0],this.tl.tcb[1],this.tl.tcb[2]);
			this._disc(gl,this.cv.get(1).sub(this.cv.get(0)).normalize(),this.cv.get(0),-off);
		}
		if (this.tli==0){
			this._draw_track_slice(gl,this.cv.get(1).sub(this.cv.get(0)),this.cv.get(2).sub(this.cv.get(1)).center(this.cv.get(1).sub(this.cv.get(0))),this.cv.get(0),this.cv.get(1),off,off+((this.erot-this.srot)/this.cv.size()),0);
		}
		else{
			TrackElement tp=this.tl.tpl.get(this.tli-1);
			this._draw_track_slice(gl,tp.cv.get(tp.cv.size()-1).sub(tp.cv.get(tp.cv.size()-2)).center(this.cv.get(1).sub(this.cv.get(0))),this.cv.get(2).sub(this.cv.get(1)).center(this.cv.get(1).sub(this.cv.get(0))),this.cv.get(0),this.cv.get(1),off,off+((this.erot-this.srot)/this.cv.size()),0);
		}
		for (int i=1;i<this.cv.size()-2;i++){
			off+=((this.erot-this.srot)/this.cv.size());
			this._draw_track_slice(gl,this.cv.get(i+1).sub(this.cv.get(i)).center(this.cv.get(i).sub(this.cv.get(i-1))),this.cv.get(i+2).sub(this.cv.get(i+1)).center(this.cv.get(i+1).sub(this.cv.get(i))),this.cv.get(i),this.cv.get(i+1),off,off+((this.erot-this.srot)/this.cv.size()),0);
		}
		if (this.tli==this.tl.tpl.size()-1){
			this._draw_track_slice(gl,this.cv.get(this.cv.size()-1).sub(this.cv.get(this.cv.size()-2)).center(this.cv.get(this.cv.size()-2).sub(this.cv.get(this.cv.size()-3))),this.cv.get(this.cv.size()-1).sub(this.cv.get(this.cv.size()-2)),this.cv.get(this.cv.size()-2),this.cv.get(this.cv.size()-1),off+((this.erot-this.srot)/this.cv.size()),this.erot,1);
		}
		else{
			TrackElement tp=this.tl.tpl.get(this.tli+1);
			this._draw_track_slice(gl,this.cv.get(this.cv.size()-1).sub(this.cv.get(this.cv.size()-2)).center(this.cv.get(this.cv.size()-2).sub(this.cv.get(this.cv.size()-3))),this.cv.get(this.cv.size()-1).sub(this.cv.get(this.cv.size()-2)).center(tp.cv.get(1).sub(tp.cv.get(0))),this.cv.get(this.cv.size()-2),this.cv.get(this.cv.size()-1),off+((this.erot-this.srot)/this.cv.size()),this.erot,1);
		}
		if (this.tli==this.tl.tpl.size()-1){
			gl.glColor3d(this.tl.tcb[0],this.tl.tcb[1],this.tl.tcb[2]);
			this._disc(gl,this.cv.get(this.cv.size()-2).sub(this.cv.get(this.cv.size()-1)).normalize(),this.cv.get(this.cv.size()-1),this.erot);
		}
		gl.glEnd();
	}



	private void _draw_track_slice(GL2 gl,Vector3 s,Vector3 e,Vector3 so,Vector3 eo,double soff,double eoff,int tp){
		gl.glColor3d(this.tl.tcb[0],this.tl.tcb[1],this.tl.tcb[2]);
		Vector3 sa=s.normalize();
		Vector3 sp=new Vector3(TRACK_PIECE_BIGGER_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(sa.z,sa.x)+Math.PI/2).rotate(sa,Math.PI/2+soff);
		Vector3 ea=e.normalize();
		Vector3 ep=new Vector3(TRACK_PIECE_BIGGER_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(ea.z,ea.x)+Math.PI/2).rotate(ea,Math.PI/2+eoff);
		for (double i=0;i<Math.PI*2;i+=Math.PI*2/TRACK_PIECE_DETAIL){
			Vector3 a=sp.rotate(sa,i).add(so);
			Vector3 b=sp.rotate(sa,i+Math.PI*2/TRACK_PIECE_DETAIL).add(so);
			Vector3 c=ep.rotate(ea,i).add(eo);
			Vector3 d=ep.rotate(ea,i+Math.PI*2/TRACK_PIECE_DETAIL).add(eo);
			a.vertex(gl);
			b.vertex(gl);
			c.vertex(gl);
			this.game._tn++;
			b.vertex(gl);
			c.vertex(gl);
			d.vertex(gl);
			this.game._tn++;
		}
		gl.glColor3d(this.tl.tcr[0],this.tl.tcr[1],this.tl.tcr[2]);
		Vector3 slp=sp.mag(TRACK_PIECE_SMALLER_RADIUS);
		Vector3 slo=sp.mag(TRACK_PIECE_ATTACH_BIGGER_WIDTH).rotate(sa,-TRACK_PIECE_ATTACH_TOP_ROT).add(so);
		Vector3 elp=ep.mag(TRACK_PIECE_SMALLER_RADIUS);
		Vector3 elo=ep.mag(TRACK_PIECE_ATTACH_BIGGER_WIDTH).rotate(ea,-TRACK_PIECE_ATTACH_TOP_ROT).add(eo);
		for (double i=0;i<Math.PI*2;i+=Math.PI*2/TRACK_PIECE_SMALLER_DETAIL){
			Vector3 a=slp.rotate(sa,i).add(slo);
			Vector3 b=slp.rotate(sa,i+Math.PI*2/TRACK_PIECE_SMALLER_DETAIL).add(slo);
			Vector3 c=elp.rotate(ea,i).add(elo);
			Vector3 d=elp.rotate(ea,i+Math.PI*2/TRACK_PIECE_SMALLER_DETAIL).add(elo);
			a.vertex(gl);
			b.vertex(gl);
			c.vertex(gl);
			this.game._tn++;
			b.vertex(gl);
			c.vertex(gl);
			d.vertex(gl);
			this.game._tn++;
		}
		Vector3 srp=sp.mag(TRACK_PIECE_SMALLER_RADIUS);
		Vector3 sro=sp.mag(TRACK_PIECE_ATTACH_BIGGER_WIDTH).rotate(sa,TRACK_PIECE_ATTACH_TOP_ROT).add(so);
		Vector3 erp=ep.mag(TRACK_PIECE_SMALLER_RADIUS);
		Vector3 ero=ep.mag(TRACK_PIECE_ATTACH_BIGGER_WIDTH).rotate(ea,TRACK_PIECE_ATTACH_TOP_ROT).add(eo);
		for (double i=0;i<Math.PI*2;i+=Math.PI*2/TRACK_PIECE_SMALLER_DETAIL){
			Vector3 a=srp.rotate(sa,i).add(sro);
			Vector3 b=srp.rotate(sa,i+Math.PI*2/TRACK_PIECE_SMALLER_DETAIL).add(sro);
			Vector3 c=erp.rotate(ea,i).add(ero);
			Vector3 d=erp.rotate(ea,i+Math.PI*2/TRACK_PIECE_SMALLER_DETAIL).add(ero);
			a.vertex(gl);
			b.vertex(gl);
			c.vertex(gl);
			this.game._tn++;
			b.vertex(gl);
			c.vertex(gl);
			d.vertex(gl);
			this.game._tn++;
		}
		gl.glColor3d(this.tl.tcm[0],this.tl.tcm[1],this.tl.tcm[2]);
		sp.mag(TRACK_PIECE_ATTACH_SMALLER_WIDTH).rotate(sa,TRACK_PIECE_ATTACH_BOTTOM_ROT).add(so).vertex(gl);
		sp.mag(TRACK_PIECE_ATTACH_SMALLER_WIDTH).rotate(sa,-TRACK_PIECE_ATTACH_BOTTOM_ROT).add(so).vertex(gl);
		sp.mag(TRACK_PIECE_ATTACH_BIGGER_WIDTH).rotate(sa,-TRACK_PIECE_ATTACH_TOP_ROT).add(so).vertex(gl);
		this.game._tn++;
		sp.mag(TRACK_PIECE_ATTACH_SMALLER_WIDTH).rotate(sa,TRACK_PIECE_ATTACH_BOTTOM_ROT).add(so).vertex(gl);
		sp.mag(TRACK_PIECE_ATTACH_BIGGER_WIDTH).rotate(sa,-TRACK_PIECE_ATTACH_TOP_ROT).add(so).vertex(gl);
		sp.mag(TRACK_PIECE_ATTACH_BIGGER_WIDTH).rotate(sa,TRACK_PIECE_ATTACH_TOP_ROT).add(so).vertex(gl);
		this.game._tn++;
		if (tp==1){
			ep.mag(TRACK_PIECE_ATTACH_SMALLER_WIDTH).rotate(ea,TRACK_PIECE_ATTACH_BOTTOM_ROT).add(eo).vertex(gl);
			ep.mag(TRACK_PIECE_ATTACH_SMALLER_WIDTH).rotate(ea,-TRACK_PIECE_ATTACH_BOTTOM_ROT).add(eo).vertex(gl);
			ep.mag(TRACK_PIECE_ATTACH_BIGGER_WIDTH).rotate(ea,-TRACK_PIECE_ATTACH_TOP_ROT).add(eo).vertex(gl);
			this.game._tn++;
			ep.mag(TRACK_PIECE_ATTACH_SMALLER_WIDTH).rotate(ea,TRACK_PIECE_ATTACH_BOTTOM_ROT).add(eo).vertex(gl);
			ep.mag(TRACK_PIECE_ATTACH_BIGGER_WIDTH).rotate(ea,-TRACK_PIECE_ATTACH_TOP_ROT).add(eo).vertex(gl);
			ep.mag(TRACK_PIECE_ATTACH_BIGGER_WIDTH).rotate(ea,TRACK_PIECE_ATTACH_TOP_ROT).add(eo).vertex(gl);
			this.game._tn++;
		}
	}



	private void _regenerate(){
		ArrayList<Vector3> lc=this._gen_curve(this.a,this.b,this.anc,TRACK_PIECE_LENGTH_CURVE_DETAIL);
		double l=0;
		for (int i=0;i<lc.size()-1;i++){
			l+=Math.sqrt((lc.get(i).x-lc.get(i+1).x)*(lc.get(i).x-lc.get(i+1).x)+(lc.get(i).y-lc.get(i+1).y)*(lc.get(i).y-lc.get(i+1).y)+(lc.get(i).z-lc.get(i+1).z)*(lc.get(i).z-lc.get(i+1).z));
		}
		this.cv=this._gen_curve(this.a,this.b,this.anc,(int)Math.floor(l/TRACK_PIECE_TRACK_SEGMENT_LENGTH));
		this.rpa.setup(this.cv.get(0),this.cv.get(0).sub(this.cv.get(1)).normalize());
		this.rpb.setup(this.cv.get(this.cv.size()-1),this.cv.get(this.cv.size()-1).sub(this.cv.get(this.cv.size()-2)).normalize());
	}



	private void _disc(GL2 gl,Vector3 ax,Vector3 o,double off){
		Vector3 p=new Vector3(TRACK_PIECE_BIGGER_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(ax.z,ax.x)+Math.PI/2).rotate(ax,Math.PI/2-off);
		for (double i=0;i<Math.PI*2;i+=Math.PI*2/TRACK_PIECE_DETAIL){
			p.rotate(ax,i+off).add(o).vertex(gl);
			p.rotate(ax,i+Math.PI*2/TRACK_PIECE_DETAIL+off).add(o).vertex(gl);
			o.vertex(gl);
			this.game._tn++;
		}
		gl.glColor3d(this.tl.tcr[0],this.tl.tcr[1],this.tl.tcr[2]);
		Vector3 lo=p.mag(TRACK_PIECE_ATTACH_BIGGER_WIDTH).rotate(ax,-TRACK_PIECE_ATTACH_TOP_ROT).add(o);
		Vector3 lp=p.mag(TRACK_PIECE_SMALLER_RADIUS);
		for (double i=0;i<Math.PI*2;i+=Math.PI*2/TRACK_PIECE_SMALLER_DETAIL){
			lp.rotate(ax,i).add(lo).vertex(gl);
			lp.rotate(ax,i+Math.PI*2/TRACK_PIECE_SMALLER_DETAIL).add(lo).vertex(gl);
			lo.vertex(gl);
			this.game._tn++;
		}
		Vector3 ro=p.mag(TRACK_PIECE_ATTACH_BIGGER_WIDTH).rotate(ax,TRACK_PIECE_ATTACH_TOP_ROT).add(o);
		Vector3 rp=p.mag(TRACK_PIECE_SMALLER_RADIUS);
		for (double i=0;i<Math.PI*2;i+=Math.PI*2/TRACK_PIECE_SMALLER_DETAIL){
			rp.rotate(ax,i).add(ro).vertex(gl);
			rp.rotate(ax,i+Math.PI*2/TRACK_PIECE_SMALLER_DETAIL).add(ro).vertex(gl);
			ro.vertex(gl);
			this.game._tn++;
		}
	}



	private ArrayList<Vector3> _gen_curve(Vector3 a,Vector3 b,Vector3 anc,int n){
		ArrayList<Vector3> l=new ArrayList<Vector3>();
		l.add(a);
		for (double t=1d/n;t<1;t+=1d/n){
			double x=(1-t)*(1-t)*a.x+2*(1-t)*t*anc.x+t*t*b.x;
			double y=(1-t)*(1-t)*a.y+2*(1-t)*t*anc.y+t*t*b.y;
			double z=(1-t)*(1-t)*a.z+2*(1-t)*t*anc.z+t*t*b.z;
			l.add(new Vector3(x,y,z));
		}
		if (!l.get(l.size()-1).eq(b)){
			l.add(b);
		}
		return l;
	}
}