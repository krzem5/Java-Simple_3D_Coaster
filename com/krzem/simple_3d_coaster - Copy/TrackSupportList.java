package com.krzem.simple_3d_coaster - Copy;



import com.jogamp.opengl.GL2;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;



public class TrackSupportList extends Constants{
	public Main.Main_ cls;
	public Game game;
	public TrackList tl;
	public List<TrackSupport> tsl;



	public TrackSupportList(Main.Main_ cls,Game game,TrackList tl){
		this.cls=cls;
		this.game=game;
		this.tl=tl;
		this.tsl=null;
	}



	public void update(GL2 gl){
		this.tsl=this._regenerate();
		for (TrackSupport ts:this.tsl){
			ts.update(gl);
		}
	}



	public void draw(GL2 gl){
		for (TrackSupport ts:this.tsl){
			ts.draw(gl);
		}
	}



	private List<TrackSupport> _regenerate(){
		List<TrackSupport> data=new ArrayList<TrackSupport>();
		int i=0;
		double ld=0;
		for (TrackElement tp:this.tl.tpl){
			if (i==0){
				Object[] dt=this._support(tp.cv.get(0),tp.cv.get(1),tp.srot,ld);
				if (dt!=null){
					data.add((TrackSupport)dt[0]);
					ld=(double)dt[1];
				}
			}
			Object[] dt=this._support(tp.cv.get(tp.cv.size()-1),tp.cv.get(tp.cv.size()-2),-tp.erot,ld);
			if (dt!=null){
				data.add((TrackSupport)dt[0]);
				ld=(double)dt[1];
			}
			i++;
		}
		return data;
	}



	private Object[] _support(Vector3 o,Vector3 ob,double rot,double ld){
		double nld=ld+0;
		Vector3 ax=ob.sub(o).normalize();
		Vector3 p=new Vector3(-TRACK_SUPPORT_MIN_SUPPORT_LEN,0,0).rotate(new Vector3(0,1,0),-Math.atan2(ax.z,ax.x)+Math.PI/2).rotate(ax,Math.PI/2+rot);
		double a=p.angle().x/Math.PI*180;
		if (a<=45){
			Vector3 c=p.add(o);
			Vector3 ap=new Vector3(-OVER_TRACK_SUPPORT_TOP_BEAM_LEN,0,0).rotate(new Vector3(0,1,0),-Math.atan2(ax.z,ax.x)+Math.PI/2).rotate(ax,Math.PI/2+rot-Math.PI/2);
			Vector3 bp=new Vector3(-OVER_TRACK_SUPPORT_TOP_BEAM_LEN,0,0).rotate(new Vector3(0,1,0),-Math.atan2(ax.z,ax.x)+Math.PI/2).rotate(ax,Math.PI/2+rot+Math.PI/2);
			Vector3 aa=p.add(ap).add(o);
			Vector3 ba=p.add(bp).add(o);
			Vector3 g=o.clone();
			g.y=0;
			Vector3 ag=g.add(ap);
			ag.y=0;
			Vector3 bg=g.add(bp);
			bg.y=0;
			Vector3 ab=this._3d_line_line_intersection(aa,ap.add(o),g,ag);
			Vector3 bb=this._3d_line_line_intersection(ba,bp.add(o),g,bg);
			if (aa.y<c.y){
				ab=aa.clone();
				ab.y=0;
			}
			if (ba.y<c.y){
				bb=ba.clone();
				bb.y=0;
			}
			return new Object[]{new OverTrackSupport(this.cls,this.game,this,new Vector3[]{o,p.add(o)},new Vector3[]{ab,aa,ba,bb}),nld};
		}
		else{
			Vector3 g=p.add(o);
			g.y=0;
			Vector3 l=g.clone();
			Vector3 dr=(g.x==o.x&&g.z==o.z?new Vector3(Math.cos(ld),0,Math.sin(ld)):new Vector3(g.x-o.x,0,g.z-o.z).rotate(new Vector3(0,1,0),Math.PI/2).normalize());
			nld=dr.angle().y;
			Vector3 v=dr.rotate((g.x==o.x&&g.z==o.z?dr.rotate(new Vector3(0,1,0),-Math.PI/2):new Vector3(g.x-o.x,0,g.z-o.z)).normalize(),-Math.PI/2+UNDER_TRACK_SUPPORT_LEG_ANGLE);
			l=this._3d_line_line_intersection(p.add(o),v.add(p.add(o)),g,g.add(dr));
			Vector3 la=l.clone();
			Vector3 lb=l.clone();
			Vector3 lv=l.sub(p.add(o));
			double len=Math.sqrt(lv.x*lv.x+lv.y*lv.y+lv.z*lv.z);
			if (len>=UNDER_TRACK_SUPPORT_MIN_SIDE_SUPPORT_SPLIT_LEN){
				la=this._lerp(p.add(o),l,(double)UNDER_TRACK_SUPPORT_MAX_SIDE_SUPPORT_LEN/len);
				lb=la.clone();
				lb.y=0;
			}
			return new Object[]{new UnderTrackSupport(this.cls,this.game,this,p.add(o),new Vector3[]{o,g,la,lb}),nld};
		}
	}



	private Vector3 _3d_line_line_intersection(Vector3 a,Vector3 b,Vector3 c,Vector3 d){
		double n=((a.x-c.x)*(d.x-c.x)+(a.y-c.y)*(d.y-c.y)+(a.z-c.z)*(d.z-c.z))*((d.x-c.x)*(b.x-a.x)+(d.y-c.y)*(b.y-a.y)+(d.z-c.z)*(b.z-a.z))-((a.x-c.x)*(b.x-a.x)+(a.y-c.y)*(b.y-a.y)+(a.z-c.z)*(b.z-a.z))*((d.x-c.x)*(d.x-c.x)+(d.y-c.y)*(d.y-c.y)+(d.z-c.z)*(d.z-c.z));
		double dn=((b.x-a.x)*(b.x-a.x)+(b.y-a.y)*(b.y-a.y)+(b.z-a.z)*(b.z-a.z))*((d.x-c.x)*(d.x-c.x)+(d.y-c.y)*(d.y-c.y)+(d.z-c.z)*(d.z-c.z))-((d.x-c.x)*(b.x-a.x)+(d.y-c.y)*(b.y-a.y)+(d.z-c.z)*(b.z-a.z))*((d.x-c.x)*(b.x-a.x)+(d.y-c.y)*(b.y-a.y)+(d.z-c.z)*(b.z-a.z));
		double u=n/dn;
		double x=a.x+u*(b.x-a.x);
		double y=a.y+u*(b.y-a.y);
		double z=a.z+u*(b.z-a.z);
		return new Vector3(x,y,z);
	}



	private Vector3 _lerp(Vector3 a,Vector3 b,double t){
		return new Vector3(a.x+t*(b.x-a.x),a.y+t*(b.y-a.y),a.z+t*(b.z-a.z));
	}
}