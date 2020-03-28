package com.krzem.simple_3d_coaster - Copy;



import com.jogamp.opengl.GL2;
import java.util.ArrayList;
import java.util.List;



public class OverTrackSupport extends TrackSupport{
	public Vector3[] tr;



	public OverTrackSupport(Main.Main_ cls,Game game,TrackSupportList tsl,Vector3[] tr,Vector3[] bm){
		this.cls=cls;
		this.game=game;
		this.tsl=tsl;
		this.tr=tr;
		this.bm=bm;
	}



	public void update(GL2 gl){
		this.ga=this.bm[0];
		this.gb=this.bm[1];
	}



	public void draw(GL2 gl){
		gl.glBegin(GL2.GL_TRIANGLES);
		gl.glColor3d(this.tsl.tl.sc[0],this.tsl.tl.sc[1],this.tsl.tl.sc[2]);
		this._draw_tube(gl,this.tr[0],this.tr[1]);
		this._draw_tube(gl,this.bm[0],this.bm[1],this.bm[2],this.bm[3]);
		gl.glEnd();
	}



	private void _draw_tube(GL2 gl,Vector3 ao,Vector3 bo){
		Vector3 aa=bo.sub(ao).normalize();
		Vector3 ap=new Vector3(TRACK_SUPPORT_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(aa.z,aa.x)+Math.PI/2).rotate(aa,Math.PI/2);
		Vector3 ba=bo.sub(ao).normalize();
		Vector3 bp=new Vector3(TRACK_SUPPORT_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(ba.z,ba.x)+Math.PI/2).rotate(ba,Math.PI/2);
		for (double i=0;i<Math.PI*2;i+=Math.PI*2/TRACK_SUPPORT_DETAIL){
			Vector3 va=ap.rotate(aa,i).add(ao);
			Vector3 vb=ap.rotate(aa,i+Math.PI*2/TRACK_SUPPORT_DETAIL).add(ao);
			Vector3 vc=bp.rotate(ba,i).add(bo);
			Vector3 vd=bp.rotate(ba,i+Math.PI*2/TRACK_SUPPORT_DETAIL).add(bo);
			va.vertex(gl);
			vb.vertex(gl);
			vc.vertex(gl);
			this.game._tn++;
			vb.vertex(gl);
			vc.vertex(gl);
			vd.vertex(gl);
			this.game._tn++;
		}
	}



	private void _draw_tube(GL2 gl,Vector3 ao,Vector3 bo,Vector3 co,Vector3 _do){
		Vector3 aa=bo.sub(ao).normalize();
		Vector3 ap=new Vector3(TRACK_SUPPORT_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(aa.z,aa.x)+Math.PI/2).rotate(aa,Math.PI/2);
		Vector3 ba=bo.sub(ao).center(co.sub(bo)).normalize();
		Vector3 bp=new Vector3(TRACK_SUPPORT_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(ba.z,ba.x)+Math.PI/2).rotate(ba,Math.PI/2);
		Vector3 ca=co.sub(bo).center(_do.sub(co)).normalize();
		Vector3 cp=new Vector3(TRACK_SUPPORT_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(ca.z,ca.x)+Math.PI/2).rotate(ca,Math.PI/2);
		Vector3 da=_do.sub(co).normalize();
		Vector3 dp=new Vector3(TRACK_SUPPORT_RADIUS,0,0).rotate(new Vector3(0,1,0),-Math.atan2(da.z,da.x)+Math.PI/2).rotate(da,Math.PI/2);
		for (double i=0;i<Math.PI*2;i+=Math.PI*2/TRACK_SUPPORT_DETAIL){
			Vector3 va=ap.rotate(aa,i).add(ao);
			Vector3 vb=ap.rotate(aa,i+Math.PI*2/TRACK_SUPPORT_DETAIL).add(ao);
			Vector3 vc=bp.rotate(ba,i).add(bo);
			Vector3 vd=bp.rotate(ba,i+Math.PI*2/TRACK_SUPPORT_DETAIL).add(bo);
			Vector3 ve=cp.rotate(ca,i).add(co);
			Vector3 vf=cp.rotate(ca,i+Math.PI*2/TRACK_SUPPORT_DETAIL).add(co);
			Vector3 vg=dp.rotate(da,i).add(_do);
			Vector3 vh=dp.rotate(da,i+Math.PI*2/TRACK_SUPPORT_DETAIL).add(_do);
			va.vertex(gl);
			vb.vertex(gl);
			vc.vertex(gl);
			this.game._tn++;
			vb.vertex(gl);
			vc.vertex(gl);
			vd.vertex(gl);
			this.game._tn++;
			vc.vertex(gl);
			vd.vertex(gl);
			ve.vertex(gl);
			this.game._tn++;
			vd.vertex(gl);
			ve.vertex(gl);
			vf.vertex(gl);
			this.game._tn++;
			ve.vertex(gl);
			vf.vertex(gl);
			vg.vertex(gl);
			this.game._tn++;
			vf.vertex(gl);
			vg.vertex(gl);
			vh.vertex(gl);
			this.game._tn++;
		}
	}
}