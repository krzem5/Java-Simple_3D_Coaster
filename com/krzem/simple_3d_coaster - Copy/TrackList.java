package com.krzem.simple_3d_coaster - Copy;



import com.jogamp.opengl.GL2;
import java.util.ArrayList;
import java.util.List;



public class TrackList extends Constants{
	public Main.Main_ cls;
	public Game game;
	public List<TrackElement> tpl;
	public TrackSupportList tsl;
	public TrackData tdt;
	public TrackRenderer tr;
	public double[] tcb;
	public double[] tcr;
	public double[] tcm;
	public double[] sc;
	public int _t_sel=-1;
	public boolean _d_sel=false;
	public boolean _tl_sel=false;
	private int _old_t_sel=-1;
	private boolean _kd=false;
	private boolean _at=true;



	public TrackList(Main.Main_ cls,Game game){
		this.cls=cls;
		this.game=game;
		this.tpl=new ArrayList<TrackElement>();
		this.tsl=new TrackSupportList(this.cls,this.game,this);
		this.tdt=new TrackData(this.cls,this.game,this);
		this.tr=new TrackRenderer(this.cls,this.game,this);
	}



	public void update(GL2 gl){
		if (this._tl_sel==false){
			this._d_sel=false;
			this._t_sel=-1;
		}
		if (this._at==true){
			if (this._kd==false&&this.cls.KEYBOARD.pressed(37)){
				this._kd=true;
				this._d_sel=false;
				this._t_sel=Math.max(0,this._t_sel-1);
			}
			if (this._kd==false&&this.cls.KEYBOARD.pressed(39)){
				this._kd=true;
				this._d_sel=false;
				this._t_sel=Math.min(this.tpl.size()-1,this._t_sel+1);
			}
			if (this._kd==false&&this.cls.KEYBOARD.pressed(78)){
				this._kd=true;
				if (this.cls.KEYBOARD.pressed(49)){
					this._d_sel=false;
					this._t_sel=this.tpl.size();
					TrackElement tp=this.tpl.get(this.tpl.size()-1);
					Vector3 b=tp.cv.get(tp.cv.size()-1).sub(tp.cv.get(tp.cv.size()-2)).mag(TRACK_PIECE_NEW_TRACK_LENGTH).add(tp.b);
					this.tpl.add(new TrackPiece(this.cls,this.game,this,this.tpl.size(),tp.b,b,new Vector3(tp.b.x/2+b.x/2,tp.b.y/2+b.y/2,tp.b.z/2+b.z/2),tp.erot,Math.floor(tp.erot/(Math.PI*2)*Math.PI*2)));
					this.tdt._rg=true;
				}
				if (this.cls.KEYBOARD.pressed(50)){
					this._d_sel=false;
					this._t_sel=this.tpl.size();
					TrackElement tp=this.tpl.get(this.tpl.size()-1);
					Vector3 b=tp.cv.get(tp.cv.size()-1).sub(tp.cv.get(tp.cv.size()-2));
					b.y=tp.b.y+10;
					b=b.mag(TRACK_PIECE_NEW_TRACK_LENGTH).add(tp.b);
					this.tpl.add(new BreakTrackPiece(this.cls,this.game,this,this.tpl.size(),tp.b,b));
					this.tdt._rg=true;
				}
				if (this.cls.KEYBOARD.pressed(51)){
					this._d_sel=false;
					this._t_sel=this.tpl.size();
					TrackElement tp=this.tpl.get(this.tpl.size()-1);
					Vector3 b=tp.cv.get(tp.cv.size()-1).sub(tp.cv.get(tp.cv.size()-2));
					b.y=0;
					b=b.mag(TRACK_PIECE_NEW_TRACK_LENGTH).add(tp.b);
					this.tpl.add(new LiftTrackPiece(this.cls,this.game,this,this.tpl.size(),tp.b,b));
					this.tdt._rg=true;
				}
				if (this.cls.KEYBOARD.pressed(52)){
					this._d_sel=false;
					this._t_sel=this.tpl.size();
					TrackElement tp=this.tpl.get(this.tpl.size()-1);
					Vector3 b=tp.cv.get(tp.cv.size()-1).sub(tp.cv.get(tp.cv.size()-2));
					b.y=0;
					b=b.mag(LOOP_TRACK_PIECE_NEW_TRACK_LENGTH).add(tp.b);
					Vector3 anc=new Vector3(tp.b.x/2+b.x/2,tp.b.y+LOOP_TRACK_PIECE_NEW_TRACK_LENGTH,tp.b.z/2+b.z/2);
					this.tpl.add(new LoopTrackPiece(this.cls,this.game,this,this.tpl.size(),tp.b,b,anc));
					this.tdt._rg=true;
				}
			}
			if (this._kd==false&&this.cls.KEYBOARD.pressed(84)){
				this._kd=true;
				this._d_sel=false;
				this._old_t_sel=this._t_sel+0;;
				this._t_sel=-1;
				this._at=false;
				this.game.train=new Train(this.cls,this.game,this);
			}
			if (this._kd==false&&this.cls.KEYBOARD.pressed(127)){
				this._kd=true;
				if (this.tpl.size()>1&&this._t_sel==this.tpl.size()-1){
					this._d_sel=false;
					this._t_sel=this.tpl.size()-2;
					this.tpl.remove(this.tpl.get(this.tpl.size()-1));
					this.tdt._rg=true;
				}
			}
			for (int i=this.tpl.size()-1;i>=0;i--){
				if (i<this.tpl.size()-1){
					this.tpl.get(i).b.set(this.tpl.get(i+1).a);
					this.tpl.get(i).erot=this.tpl.get(i+1).srot+0;
				}
				this.tpl.get(i).update(gl);
			}
			this.tsl.update(gl);
		}
		else{
			if (this._kd==false&&this.cls.KEYBOARD.pressed(84)&&this.game.train!=null&&this._old_t_sel!=-1){
				this.game.train=null;
				this._kd=true;
			}
			if (this.game.train==null&&this._old_t_sel!=-1){
				this._at=true;
				this._t_sel=this._old_t_sel+0;
				this._old_t_sel=-1;
			}
		}
		if (this._kd==true&&!this.cls.KEYBOARD.pressed(37)&&!this.cls.KEYBOARD.pressed(39)&&!this.cls.KEYBOARD.pressed(84)&&!this.cls.KEYBOARD.pressed(78)&&!this.cls.KEYBOARD.pressed(127)){
			this._kd=false;
		}
		this.tdt.update(gl);
	}



	public void draw(GL2 gl){
		// for (TrackElement o:this.tpl){
		// 	o.draw(gl);
		// }
		this.tr.draw(gl);
		this.tsl.draw(gl);
		this.tdt.draw(gl);
	}
}