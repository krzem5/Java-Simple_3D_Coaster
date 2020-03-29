package com.krzem.simple_3d_coaster;



import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.Exception;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class IO extends Constants{
	private static String fp="";



	public static void load_from_file(Game game,String fp){
		IO.fp=fp;
		try{
			game.tll=new ArrayList<TrackList>();
			Document doc=DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fp);
			doc.getDocumentElement().normalize();
			Element root=doc.getDocumentElement();
			Element cam=IO._get_children_by_tag_name(root,"camera").get(0);
			Vector3 p=IO._xml_to_vector3(IO._get_children_by_tag_name(cam,"pos").get(0));
			Vector3 r=IO._xml_to_vector3(IO._get_children_by_tag_name(cam,"rot").get(0));
			double zm=Double.parseDouble(IO._get_children_by_tag_name(cam,"zoom").get(0).getTextContent());
			game.cls.cam.x=p.x;
			game.cls.cam.dx=p.x;
			game.cls.cam.y=p.y;
			game.cls.cam.dy=p.y;
			game.cls.cam.z=p.z;
			game.cls.cam.dz=p.z;
			game.cls.cam.rx=r.x;
			game.cls.cam.drx=r.x;
			game.cls.cam.ry=r.y;
			game.cls.cam.dry=r.y;
			game.cls.cam.rz=r.z;
			game.cls.cam.drz=r.z;
			game.cls.cam.zm=zm;
			game.cls.cam.dzm=zm;
			Element te=IO._get_children_by_tag_name(root,"tracks").get(0);
			int si=0;
			int i=0;
			for (Element tle:IO._get_children_by_tag_name(te,"track-list")){
				if (tle.getAttribute("selected").equals("true")){
					si=i+0;
				}
				TrackList tl=new TrackList(game.cls,game);
				Element se=IO._get_children_by_tag_name(tle,"settings").get(0);
				Element sce=IO._get_children_by_tag_name(se,"colors").get(0);
				tl.tcb=IO._xml_to_color(IO._get_children_by_tag_name(sce,"track-beam").get(0));
				tl.tcr=IO._xml_to_color(IO._get_children_by_tag_name(sce,"track-rail").get(0));
				tl.tcm=IO._xml_to_color(IO._get_children_by_tag_name(sce,"track-misc").get(0));
				tl.sc=IO._xml_to_color(IO._get_children_by_tag_name(sce,"supports").get(0));
				int sj=0;
				int j=0;
				for (Element tpe:IO._get_children_by_tag_name(tle,"track-piece")){
					if (tpe.getAttribute("selected").equals("true")){
						sj=j+0;
					}
					if (tpe.getAttribute("break").equals("true")){
						BreakTrackPiece tp=new BreakTrackPiece(game.cls,game,tl,j,IO._xml_to_vector3(IO._get_children_by_tag_name(tpe,"pos-a").get(0)),IO._xml_to_vector3(IO._get_children_by_tag_name(tpe,"pos-b").get(0)));
						tl.tpl.add(tp);
					}
					else if (tpe.getAttribute("lift").equals("true")){
						LiftTrackPiece tp=new LiftTrackPiece(game.cls,game,tl,j,IO._xml_to_vector3(IO._get_children_by_tag_name(tpe,"pos-a").get(0)),IO._xml_to_vector3(IO._get_children_by_tag_name(tpe,"pos-b").get(0)));
						tl.tpl.add(tp);
					}
					else if (tpe.getAttribute("loop").equals("true")){
						LoopTrackPiece tp=new LoopTrackPiece(game.cls,game,tl,j,IO._xml_to_vector3(IO._get_children_by_tag_name(tpe,"pos-a").get(0)),IO._xml_to_vector3(IO._get_children_by_tag_name(tpe,"pos-b").get(0)),IO._xml_to_vector3(IO._get_children_by_tag_name(tpe,"pos-anc").get(0)));
						tl.tpl.add(tp);
					}
					else{
						TrackPiece tp=new TrackPiece(game.cls,game,tl,j,IO._xml_to_vector3(IO._get_children_by_tag_name(tpe,"pos-a").get(0)),IO._xml_to_vector3(IO._get_children_by_tag_name(tpe,"pos-b").get(0)),IO._xml_to_vector3(IO._get_children_by_tag_name(tpe,"pos-anchor").get(0)),Double.parseDouble(IO._get_children_by_tag_name(tpe,"rot-a").get(0).getTextContent()),Double.parseDouble(IO._get_children_by_tag_name(tpe,"rot-b").get(0).getTextContent()));
						tl.tpl.add(tp);
					}
					j++;
				}
				tl._t_sel=sj;
				game.tll.add(tl);
				i++;
			}
			game.tll.get(si)._tl_sel=true;
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}



	public static void save_to_file(Game game,String fp){
		if (fp.length()==0){
			fp=IO.fp+"";
		}
		try{
			Document doc=DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element root=doc.createElement("simple-coaster");
			Element cam=doc.createElement("camera");
			cam.appendChild(IO._vector3_to_xml(new Vector3(game.cls.cam.x,game.cls.cam.y,game.cls.cam.z),"pos",doc));
			cam.appendChild(IO._vector3_to_xml(new Vector3(game.cls.cam.rx,game.cls.cam.ry%360,game.cls.cam.rz%360),"rot",doc));
			Element ze=doc.createElement("zoom");
			ze.appendChild(doc.createTextNode(Double.toString(game.cls.cam.zm)));
			cam.appendChild(ze);
			root.appendChild(cam);
			Element te=doc.createElement("tracks");
			for (TrackList tl:game.tll){
				Element tle=doc.createElement("track-list");
				Element se=doc.createElement("settings");
				Element sce=doc.createElement("colors");
				sce.appendChild(IO._color_to_xml(tl.tcb,"track-beam",doc));
				sce.appendChild(IO._color_to_xml(tl.tcr,"track-rail",doc));
				sce.appendChild(IO._color_to_xml(tl.tcm,"track-misc",doc));
				sce.appendChild(IO._color_to_xml(tl.sc,"supports",doc));
				se.appendChild(sce);
				tle.appendChild(se);
				tle.setAttribute("selected",(tl._tl_sel==true?"true":"false"));
				for (TrackElement t:tl.tpl){
					Element tpe=doc.createElement("track-piece");
					tpe.setAttribute("selected",(t.tli==tl._t_sel?"true":"false"));
					tpe.setAttribute("break","false");
					tpe.setAttribute("lift","false");
					tpe.setAttribute("loop","false");
					if (t.getClass().getName().equals("com.krzem.simple_3d_coaster.TrackPiece")){
						TrackPiece tp=(TrackPiece)t;
						tpe.appendChild(IO._vector3_to_xml(tp.a,"pos-a",doc));
						tpe.appendChild(IO._vector3_to_xml(tp.b,"pos-b",doc));
						tpe.appendChild(IO._vector3_to_xml(tp.anc,"pos-anchor",doc));
						Element ra=doc.createElement("rot-a");
						ra.appendChild(doc.createTextNode(Double.toString(tp.srot)));
						tpe.appendChild(ra);
						Element rb=doc.createElement("rot-b");
						rb.appendChild(doc.createTextNode(Double.toString(tp.erot)));
						tpe.appendChild(rb);
					}
					else if (t.getClass().getName().equals("com.krzem.simple_3d_coaster.BreakTrackPiece")){
						BreakTrackPiece tp=(BreakTrackPiece)t;
						tpe.setAttribute("break","true");
						tpe.appendChild(IO._vector3_to_xml(tp.a,"pos-a",doc));
						tpe.appendChild(IO._vector3_to_xml(tp.b,"pos-b",doc));
						tle.appendChild(tpe);
					}
					else if (t.getClass().getName().equals("com.krzem.simple_3d_coaster.LiftTrackPiece")){
						LiftTrackPiece tp=(LiftTrackPiece)t;
						tpe.setAttribute("lift","true");
						tpe.appendChild(IO._vector3_to_xml(tp.a,"pos-a",doc));
						tpe.appendChild(IO._vector3_to_xml(tp.b,"pos-b",doc));
						tle.appendChild(tpe);
					}
					else if (t.getClass().getName().equals("com.krzem.simple_3d_coaster.LoopTrackPiece")){
						LoopTrackPiece tp=(LoopTrackPiece)t;
						tpe.setAttribute("loop","true");
						tpe.appendChild(IO._vector3_to_xml(tp.a,"pos-a",doc));
						tpe.appendChild(IO._vector3_to_xml(tp.b,"pos-b",doc));
						tpe.appendChild(IO._vector3_to_xml(tp.anc,"pos-anc",doc));
						tle.appendChild(tpe);
					}
					tle.appendChild(tpe);
				}
				te.appendChild(tle);
			}
			root.appendChild(te);
			doc.appendChild(root);
			StreamResult o=new StreamResult(new FileWriter(new File(fp)));
			Transformer t=TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.INDENT,"yes");
			t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","4");
			t.setOutputProperty(OutputKeys.VERSION,"1.0");
			t.setOutputProperty(OutputKeys.ENCODING,"utf-8");
			t.transform(new DOMSource(doc),o);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}



	private static ArrayList<Element> _get_children_by_tag_name(Element p,String tn){
		ArrayList<Element> o=new ArrayList<Element>();
		NodeList cl=p.getChildNodes();
		for (int j=0;j<cl.getLength();j++){
			if (cl.item(j).getNodeType()!=Node.ELEMENT_NODE){
				continue;
			}
			Element e=(Element)cl.item(j);
			if (e.getTagName().equals(tn)){
				o.add(e);
			}
		}
		return o;
	}



	private static Vector3 _xml_to_vector3(Element e){
		return new Vector3(Double.parseDouble(IO._get_children_by_tag_name(e,"x").get(0).getTextContent()),Double.parseDouble(IO._get_children_by_tag_name(e,"y").get(0).getTextContent()),Double.parseDouble(IO._get_children_by_tag_name(e,"z").get(0).getTextContent()));
	}



	private static Element _vector3_to_xml(Vector3 v,String en,Document doc){
		Element e=doc.createElement(en);
		Element x=doc.createElement("x");
		x.appendChild(doc.createTextNode(Double.toString(v.x)));
		e.appendChild(x);
		Element y=doc.createElement("y");
		y.appendChild(doc.createTextNode(Double.toString(v.y)));
		e.appendChild(y);
		Element z=doc.createElement("z");
		z.appendChild(doc.createTextNode(Double.toString(v.z)));
		e.appendChild(z);
		return e;
	}



	private static double[] _xml_to_color(Element e){
		return new double[]{Double.parseDouble(IO._get_children_by_tag_name(e,"r").get(0).getTextContent())/255d,Double.parseDouble(IO._get_children_by_tag_name(e,"g").get(0).getTextContent())/255d,Double.parseDouble(IO._get_children_by_tag_name(e,"b").get(0).getTextContent())/255d};
	}



	private static Element _color_to_xml(double[] c,String en,Document doc){
		Element e=doc.createElement(en);
		Element r=doc.createElement("r");
		r.appendChild(doc.createTextNode(Double.toString(c[0]*255)));
		e.appendChild(r);
		Element g=doc.createElement("g");
		g.appendChild(doc.createTextNode(Double.toString(c[1]*255)));
		e.appendChild(g);
		Element b=doc.createElement("b");
		b.appendChild(doc.createTextNode(Double.toString(c[2]*255)));
		e.appendChild(b);
		return e;
	}
}