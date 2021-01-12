package com.krzem.simple_3d_coaster;



import com.jogamp.opengl.GL2;
import java.lang.Math;
import java.util.ArrayList;



public class LiftTrackPiece extends TrackElement{
	public LiftTrackPiece(Main.Main_ cls,Game game,TrackList tl,int tli,Vector3 a,Vector3 b){
		this.cls=cls;
		this.game=game;
		this.tl=tl;
		this.tli=tli;
		this.a=a;
		this.b=b;
		this.dpa=new DragPoint(this.cls,this.game,this,this.a);
		this.dpb=new DragPoint(this.cls,this.game,this,this.b);
	}



	public void update(GL2 gl){
		if (this.tli<this.tl.tpl.size()-1&&this.tl.tpl.get(this.tli+1).rpa!=null){
			this.tl.tpl.get(this.tli+1).rpa.enable(false);
		}
		this.dpb.enable(this.tli==this.tl.tpl.size()-1);
		this.dpa.update(gl);
		this.dpb.update(gl);
		this.b.y=Math.max(this.b.y,this.a.y+1);
	}



	public void draw(GL2 gl){
		this.dpa.draw(gl);
		this.dpb.draw(gl);
	}



	public Object[] _regenerate(){
		Vector3 v=this.a.sub(this.b);
		double l=Math.sqrt(v.x*v.x+v.y*v.y+v.z*v.z);
		ArrayList<Double> rt=new ArrayList<Double>();
		for (int i=0;i<=(int)Math.floor(l/TRACK_PIECE_TRACK_SEGMENT_LENGTH);i++){
			rt.add(0.0d);
		}
		return new Object[]{this._gen_line(this.a,this.b,(int)Math.floor(l/TRACK_PIECE_TRACK_SEGMENT_LENGTH)),rt};
	}



	private void _track_slice(GL2 gl,Vector3 s,Vector3 e,Vector3 so,Vector3 eo,int tp){
		gl.glColor3d(this.tl.tcb[0],this.tl.tcb[1],this.tl.tcb[2]);
		Vector3 sa=s.normalize();
		Vector3 sp=new Vector3(TRACK_PIECE_BIGGER_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(sa.z,sa.x)+Math.PI/2).rotate(sa,Math.PI/2);
		Vector3 ea=e.normalize();
		Vector3 ep=new Vector3(TRACK_PIECE_BIGGER_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(ea.z,ea.x)+Math.PI/2).rotate(ea,Math.PI/2);
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
		gl.glColor3d(0.89,0.89,0.89);
		gl.glColor3d(0.55,0.55,0.55);
		double y=sp.mag(TRACK_PIECE_ATTACH_BIGGER_WIDTH).rotate(sa,TRACK_PIECE_ATTACH_TOP_ROT).y;
		Vector3 a=new Vector3(-BREAK_TRACK_PIECE_BREAK_BIGGER_WIDTH,y,BREAK_TRACK_PIECE_BREAK_SMALLER_SIZE_OFF).rotate(new Vector3(0,1,0),-Math.atan2(sa.z,sa.x)+Math.PI/2).add(so);
		Vector3 b=new Vector3(-BREAK_TRACK_PIECE_BREAK_BIGGER_WIDTH,y,-BREAK_TRACK_PIECE_BREAK_SMALLER_SIZE_OFF).rotate(new Vector3(0,1,0),-Math.atan2(ea.z,ea.x)+Math.PI/2).add(eo);
		Vector3 c=new Vector3(-BREAK_TRACK_PIECE_BREAK_SMALLER_WIDTH,y,-BREAK_TRACK_PIECE_BREAK_BIGGER_SIZE_OFF).rotate(new Vector3(0,1,0),-Math.atan2(ea.z,ea.x)+Math.PI/2).add(eo);
		Vector3 d=new Vector3(-BREAK_TRACK_PIECE_BREAK_SMALLER_WIDTH,y,BREAK_TRACK_PIECE_BREAK_BIGGER_SIZE_OFF).rotate(new Vector3(0,1,0),-Math.atan2(sa.z,sa.x)+Math.PI/2).add(so);
		Vector3 _e=new Vector3(-BREAK_TRACK_PIECE_BREAK_BIGGER_WIDTH,y+BREAK_TRACK_PIECE_BREAK_HEIGHT,BREAK_TRACK_PIECE_BREAK_SMALLER_SIZE_OFF).rotate(new Vector3(0,1,0),-Math.atan2(sa.z,sa.x)+Math.PI/2).add(so);
		Vector3 f=new Vector3(-BREAK_TRACK_PIECE_BREAK_BIGGER_WIDTH,y+BREAK_TRACK_PIECE_BREAK_HEIGHT,-BREAK_TRACK_PIECE_BREAK_SMALLER_SIZE_OFF).rotate(new Vector3(0,1,0),-Math.atan2(ea.z,ea.x)+Math.PI/2).add(eo);
		Vector3 g=new Vector3(-BREAK_TRACK_PIECE_BREAK_SMALLER_WIDTH,y+BREAK_TRACK_PIECE_BREAK_HEIGHT,-BREAK_TRACK_PIECE_BREAK_BIGGER_SIZE_OFF).rotate(new Vector3(0,1,0),-Math.atan2(ea.z,ea.x)+Math.PI/2).add(eo);
		Vector3 h=new Vector3(-BREAK_TRACK_PIECE_BREAK_SMALLER_WIDTH,y+BREAK_TRACK_PIECE_BREAK_HEIGHT,BREAK_TRACK_PIECE_BREAK_BIGGER_SIZE_OFF).rotate(new Vector3(0,1,0),-Math.atan2(sa.z,sa.x)+Math.PI/2).add(so);
		a.vertex(gl);
		b.vertex(gl);
		c.vertex(gl);
		this.game._tn++;
		a.vertex(gl);
		c.vertex(gl);
		d.vertex(gl);
		this.game._tn++;
		_e.vertex(gl);
		f.vertex(gl);
		g.vertex(gl);
		this.game._tn++;
		_e.vertex(gl);
		g.vertex(gl);
		h.vertex(gl);
		this.game._tn++;
		a.vertex(gl);
		b.vertex(gl);
		f.vertex(gl);
		this.game._tn++;
		a.vertex(gl);
		f.vertex(gl);
		_e.vertex(gl);
		this.game._tn++;
		c.vertex(gl);
		d.vertex(gl);
		h.vertex(gl);
		this.game._tn++;
		c.vertex(gl);
		h.vertex(gl);
		g.vertex(gl);
		this.game._tn++;
		a.vertex(gl);
		d.vertex(gl);
		h.vertex(gl);
		this.game._tn++;
		a.vertex(gl);
		h.vertex(gl);
		_e.vertex(gl);
		this.game._tn++;
		b.vertex(gl);
		c.vertex(gl);
		g.vertex(gl);
		this.game._tn++;
		b.vertex(gl);
		g.vertex(gl);
		f.vertex(gl);
		this.game._tn++;
	}



	private ArrayList<Vector3> _gen_line(Vector3 a,Vector3 b,int n){
		ArrayList<Vector3> l=new ArrayList<Vector3>();
		l.add(a);
		for (double t=1d/n;t<1;t+=1d/n){
			double x=a.x+t*(b.x-a.x);
			double y=a.y+t*(b.y-a.y);
			double z=a.z+t*(b.z-a.z);
			l.add(new Vector3(x,y,z));
		}
		if (!l.get(l.size()-1).eq(b)){
			l.add(b);
		}
		return l;
	}
}