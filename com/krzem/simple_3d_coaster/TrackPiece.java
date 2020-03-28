package com.krzem.simple_3d_coaster;



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
	}



	public void draw(GL2 gl){
		this.dpa.draw(gl);
		this.dpb.draw(gl);
		this.dpanc.draw(gl);
		this.rpa.draw(gl);
		this.rpb.draw(gl);
	}



	public Object[] _regenerate(){
		ArrayList<Vector3> lc=this._gen_curve(this.a,this.b,this.anc,TRACK_PIECE_LENGTH_CURVE_DETAIL);
		double l=0;
		for (int i=0;i<lc.size()-1;i++){
			l+=Math.sqrt((lc.get(i).x-lc.get(i+1).x)*(lc.get(i).x-lc.get(i+1).x)+(lc.get(i).y-lc.get(i+1).y)*(lc.get(i).y-lc.get(i+1).y)+(lc.get(i).z-lc.get(i+1).z)*(lc.get(i).z-lc.get(i+1).z));
		}
		ArrayList<Vector3> cv=this._gen_curve(this.a,this.b,this.anc,(int)Math.floor(l/TRACK_PIECE_TRACK_SEGMENT_LENGTH));
		this.rpa.setup(cv.get(0),cv.get(0).sub(cv.get(1)).normalize());
		this.rpb.setup(cv.get(cv.size()-1),cv.get(cv.size()-1).sub(cv.get(cv.size()-2)).normalize());
		ArrayList<Double> rt=new ArrayList<Double>();
		for (int i=0;i<=(int)Math.floor(l/TRACK_PIECE_TRACK_SEGMENT_LENGTH);i++){
			rt.add(this._lerp(this.srot,this.erot,((double)i)/Math.floor(l/TRACK_PIECE_TRACK_SEGMENT_LENGTH)));
		}
		return new Object[]{cv,rt};
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



	private double _lerp(double a,double b,double t){
		return a+(b-a)*t;
	}
}