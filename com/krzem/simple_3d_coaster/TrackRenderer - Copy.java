package com.krzem.simple_3d_coaster;



import com.jogamp.opengl.GL2;
import java.util.ArrayList;
import java.util.List;



public class TrackRenderer extends Constants{
	public Main.Main_ cls;
	public Game game;
	public TrackList tl;
	public ArrayList<Vector3> cv;
	public ArrayList<Double> rt;



	public TrackRenderer(Main.Main_ cls,Game game,TrackList tl){
		this.cls=cls;
		this.game=game;
		this.tl=tl;
	}



	public void update(GL2 gl){
		this.cv=new ArrayList<Vector3>();
		this.rt=new ArrayList<Double>();
		int i=0;
		for (TrackElement t:this.tl.tpl){
			Object[] _dt=t._regenerate();
			ArrayList<Vector3> cv=(ArrayList<Vector3>)_dt[0];
			ArrayList<Double> rt=(ArrayList<Double>)_dt[1];
			if (i<this.tl.tpl.size()-1){
				cv.remove(cv.size()-1);
				rt.remove(rt.size()-1);
			}
			this.cv.addAll(cv);
			this.rt.addAll(rt);
			i++;
		}
	}



	public void draw(GL2 gl){
		gl.glBegin(GL2.GL_TRIANGLES);
		gl.glColor3d(this.tl.tcb[0],this.tl.tcb[1],this.tl.tcb[2]);
		this._disc(gl,this.cv.get(1).sub(this.cv.get(0)).normalize(),this.cv.get(0),this.rt.get(0));
		this._track_slice(gl,
			this.cv.get(1).sub(this.cv.get(0)),
			this.cv.get(2).sub(this.cv.get(1)).center(this.cv.get(1).sub(this.cv.get(0))),
			null,
			null,
			this.cv.get(0),this.cv.get(1),(double)this.rt.get(0),(double)this.rt.get(1),false);
		for (int i=1;i<this.cv.size()-2;i++){
			this._track_slice(gl,
				this.cv.get(i+1).sub(this.cv.get(i)).center(this.cv.get(i).sub(this.cv.get(i-1))),
				this.cv.get(i+2).sub(this.cv.get(i+1)).center(this.cv.get(i+1).sub(this.cv.get(i))),
				null,
				null,
				this.cv.get(i),this.cv.get(i+1),(double)this.rt.get(i),(double)this.rt.get(i+1),false);
		}
		this._track_slice(gl,
			this.cv.get(this.cv.size()-1).sub(this.cv.get(this.cv.size()-2)).center(this.cv.get(this.cv.size()-2).sub(this.cv.get(this.cv.size()-3))),
			this.cv.get(this.cv.size()-1).sub(this.cv.get(this.cv.size()-2)),
			null,
			null,
			this.cv.get(this.cv.size()-2),this.cv.get(this.cv.size()-1),(double)this.rt.get(this.rt.size()-2),(double)this.rt.get(this.rt.size()-1),true);
		gl.glColor3d(this.tl.tcb[0],this.tl.tcb[1],this.tl.tcb[2]);
		this._disc(gl,this.cv.get(this.cv.size()-2).sub(this.cv.get(this.cv.size()-1)).normalize(),this.cv.get(this.cv.size()-1),this.rt.get(this.rt.size()-1));
		gl.glEnd();
	}



	private void _track_slice(GL2 gl,Vector3 s,Vector3 e,Vector3 sp,Vector3 ep,Vector3 so,Vector3 eo,double soff,double eoff,boolean end){
		Vector3 sa=s.normalize();
		Vector saa=sa.angle();
		sp=Vector3.sphere(TRACK_PIECE_BIGGER_RADIUS,saa.x-Math.PI/2,saa.y).rotate(sa,soff);
		Vector3 ea=e.normalize();
		Vector eaa=ea.angle();
		ep=Vector3.sphere(TRACK_PIECE_BIGGER_RADIUS,eaa.x-Math.PI/2,eaa.y).rotate(ea,eoff);
		gl.glEnd();
		gl.glBegin(GL2.GL_LINES);
		gl.glColor3d(1,0,1);
		so.vertex(gl);
		so.add(sa).vertex(gl);
		gl.glColor3d(1,1,0);
		so.vertex(gl);
		so.add(sp.normalize()).vertex(gl);
		gl.glEnd();
		gl.glBegin(GL2.GL_TRIANGLES);
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




	private void _disc(GL2 gl,Vector3 ax,Vector3 o,double off){
		Vector3 p=new Vector3(TRACK_PIECE_BIGGER_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(ax.z,ax.x)+Math.PI/2).rotate(ax,Math.PI/2+off);
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