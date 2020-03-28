package com.krzem.simple_3d_coaster - Copy;



import com.jogamp.opengl.GL2;
import java.util.ArrayList;
import java.util.List;



public class TrackRenderer extends Constants{
	public Main.Main_ cls;
	public Game game;
	public TrackList tl;



	public TrackRenderer(Main.Main_ cls,Game game,TrackList){
		this.cls=cls;
		this.game=game;
		this.tl=tl;
	}



	public void update(GL2 gl){

	}



	public void draw(GL2 gl){

	}



	public void _add(){

	}



	private void _track_slice(GL2 gl,Vector3 s,Vector3 e,Vector3 sp,Vector3 ep,Vector3 so,Vector3 eo,double soff,double eoff,boolean end){
		Vector3 sa=s.normalize();
		if (sp==null){
			sp=new Vector3(TRACK_PIECE_BIGGER_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(sa.z,sa.x)+Math.PI/2).rotate(sa,Math.PI/2+soff);
		}
		else{
			sp=sp.mag(TRACK_PIECE_BIGGER_RADIUS).rotate(sa,Math.PI+soff);
		}
		Vector3 ea=e.normalize();
		if (ep==null){
			ep=new Vector3(TRACK_PIECE_BIGGER_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(ea.z,ea.x)+Math.PI/2).rotate(ea,Math.PI/2+eoff);
		}
		else{
			ep=ep.mag(TRACK_PIECE_BIGGER_RADIUS).rotate(ea,Math.PI+eoff);
		}
		gl.glColor3d(this.tl.tcb[0],this.tl.tcb[1],this.tl.tcb[2]);
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
		if (end==true){
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



	private void _disc(GL2 gl,Vector3 ax,Vector3 o){
		Vector3 p=new Vector3(TRACK_PIECE_BIGGER_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(ax.z,ax.x)+Math.PI/2).rotate(ax,Math.PI/2);
		for (double i=0;i<Math.PI*2;i+=Math.PI*2/TRACK_PIECE_DETAIL){
			p.rotate(ax,i).add(o).vertex(gl);
			p.rotate(ax,i+Math.PI*2/TRACK_PIECE_DETAIL).add(o).vertex(gl);
			o.vertex(gl);
			this.game._tn++;
		}
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
}