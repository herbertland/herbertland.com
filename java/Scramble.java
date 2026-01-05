//******************************************************************************
// Scramble.java:	Applet
//
//******************************************************************************
import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.net.*;

//==============================================================================
// Main Class for applet Scramble
//
//==============================================================================

class CONST {
	static final String IMAGE= "caretaker32.jpg";
	static final int XLEN= 20;
	static final int YLEN= 20;
	static final int PREDELAY= 5000;
	static final int POSTDELAY= 5000;
	static final int INTERDELAY= 10;
}

public class Scramble extends Applet implements Runnable
{	
	private MediaTracker tracker;
	private Image images[];
	private int num_images;
	private Board board;
	Thread m_Scramble= null;
	private int xlen, ylen;
	private int predelay, postdelay, interdelay;
	private boolean movevert, movehoriz;

	public void init() {
		String param[]= new String[10];
		int last, count, which;		
		images= new Image[10];
		Random rand= new Random();

		xlen= getIntParam("Xlen", CONST.XLEN);
		ylen= getIntParam("Ylen", CONST.YLEN);
		movevert= getBoolParam("Vertical", true);
		movehoriz= getBoolParam("Horiz", true);
		predelay= getIntParam("Predelay", CONST.PREDELAY);
		postdelay= getIntParam("Postdelay", CONST.POSTDELAY);
		interdelay= getIntParam("Interdelay", CONST.INTERDELAY);
		
		for (num_images= 0; num_images < 10; num_images++) {
			param[num_images]= getParameter("Image" + num_images);
		
			if (param[num_images] == null) {
				break;
			}
		}
		if (num_images == 0) {
			param[num_images++]= CONST.IMAGE;
		}

		tracker= new MediaTracker(this);

		last= num_images;
		count= 0;
		while (last > 0) {
			which= Math.abs(rand.nextInt()) % last;
			load_image(param[which], count);
			last--;
			count++;
			param[which]= param[last];
		}
	}

	public String getStringParam(String name, String defvalue) {
		String param= getParameter(name);
		return ((param == null) ? defvalue : param);
	}

	public int getIntParam(String name, int defvalue) {
		String param= getParameter(name);
		return ((param == null) ? defvalue : Integer.parseInt(param));
	}

	public boolean getBoolParam(String name, boolean defvalue) {
		  String param= getParameter(name);
		  return ((param == null) ? defvalue : Boolean.valueOf(param).booleanValue());
		  }


	public void paint(Graphics g) {
	}

	public void start() {
		if (m_Scramble == null) {
			m_Scramble = new Thread(this);
			m_Scramble.start();
		}
	}

	public void make_board(Image image)
	{
		int imgWidth= image.getWidth(this);
		int imgHeight= image.getHeight(this);
		int nPixels[]= new int[imgWidth * imgHeight];
		int col, row, max_cols, max_rows;
		SubImage subimage;
		Dimension d;
		int basex, basey;

		PixelGrabber pg= new PixelGrabber(image, 0, 0, imgWidth, imgHeight, nPixels,
			0, imgWidth);
		try {
		   pg.grabPixels();
	   } catch (InterruptedException e) {
	   }
	  /*
	   if (imgWidth > xlen) {
		   xlen= imgWidth;
	   }
	   if (imgHeight > ylen) {
		   ylen= imgHeight;
	   }
	   */
	   max_cols= imgWidth / xlen; /* These round to nearest whole [XY]LEN */
	   max_rows= imgHeight / ylen;
	   
	   d= size();
	   basex= (d.width - max_cols * xlen) / 2;
	   basey= (d.height - max_rows * ylen) / 2;
	   
	   board= new Board(max_cols, max_rows, xlen, ylen);
		   
	   for (row= 0; row < max_rows; row++) {
		   for (col= 0; col < max_cols; col++) {		
			   subimage= new SubImage(this, nPixels, imgWidth,
				   col * xlen, row * ylen, xlen, ylen, basex, basey);

			   board.add_entry(subimage, col, row);
		   }
	   }
	}

	public void load_image(String urlstr, int ref)
	{	
		try {
			images[ref]= getImage(new URL(getCodeBase(), urlstr));
		} catch (MalformedURLException e) {
			return;
		}
		
		tracker.addImage(images[ref], 0);
	}

	public void delay(int d)
	{ 
		try	{
				Thread.sleep(d);
			} catch (InterruptedException e) {
				stop();
			}
	}

	public void run() {
		int which= 0;

		while (true) {
			try {
				tracker.waitForID(which);
			} catch (InterruptedException e) {
				which= (which + 1) % num_images;
				continue;
			}

			AudioClip sound;
			sound= getAudioClip(getCodeBase(), "bday_mon.au");
			sound.play();
			make_board(images[which]);
			board.digi_in();

			delay(predelay);

			while (board.swapper_move(getGraphics()) > 0) {
				delay(interdelay);
			}

			delay(postdelay);
			which= (which + 1) % num_images;
		} 
	}
}

class SubImage {
	public Image image;
	public int init_offx, init_offy, offx, offy, width, height, basex, basey;
	Graphics g;
	Component c;

	SubImage(Component _c, int pixels[],
			int scan, int _offx, int _offy, int _width, int _height,
			int _basex, int _basey) {
		
		image= _c.createImage(new MemoryImageSource(_width, _height,
			pixels, _offy * scan + _offx, scan));
		offx= -1;
		offy= -1;
		init_offx= _offx;
		init_offy= _offy;
		width= _width;
		height= _height;
		basex= _basex;
		basey= _basey;
		c= _c;
		g= _c.getGraphics();
	}

	public void set_coors(int _offx, int _offy, boolean clear_old) {
		if (clear_old && offx >= 0 && offy >= 0) {
			g.clearRect(offx + basex, offy + basey, width, height);
		}
		offx= _offx;
		offy= _offy;
		g.drawImage(image, offx + basex, offy + basey, null);
	}

	public void clear_coors(boolean clear_old) {
		if (clear_old && offx >= 0 && offy >= 0) {
			g.clearRect(offx + basex, offy + basey, width, height);
		}
		offx= -1;
		offy= -1;
	}
}

class BoardEnt {
	int row;
	int col;
	SubImage image;

	BoardEnt(SubImage _image, int _col, int _row) {
		image= _image;
		col= _col;
		row= _row;
	}
}

class Board {
	int max_cols;
	int max_rows;
	int xlen;
	int ylen;
	int width;
	int height;
	BoardEnt entries[];
	BoardEnt onboard[];
	int number;
	int max_num;
	Random rand;

	Board(int _max_cols, int _max_rows, int _xlen, int _ylen) {
		max_cols= _max_cols;
		max_rows= _max_rows;
		xlen= _xlen;
		ylen= _ylen;
		number= 0;
		onboard= new BoardEnt[max_cols * max_rows];
		entries= new BoardEnt[max_cols * max_rows];
		max_num= 0;
		number= 0;
		rand= new Random();
	}

	public int get_index(int col, int row) {
		return (col + row * max_cols);
	}

	public void update_entry(BoardEnt be, boolean clear_old) {
		int realx= be.col * xlen;
		int realy= be.row * ylen;
		be.image.set_coors(realx, realy, clear_old);
	}

	public void clear_entry(BoardEnt be, boolean clear_old) {
		be.image.clear_coors(clear_old);
	}

	public void add_entry(SubImage image, int col, int row) {
		BoardEnt be= new BoardEnt(image, col, row);
		entries[get_index(col, row)]= be;
		onboard[max_num++]= be;
		number++;
	}

	public void digi_in() {
		int anum= max_num;
		BoardEnt be;
		int i, which;

		while (anum > 0) {
			which= Math.abs(rand.nextInt()) % anum;
			be= onboard[which];

			update_entry(be, false);

			anum--;
			onboard[which]= onboard[anum];
			onboard[anum]= be;
		}
		number= max_num;
	}

	public void del_entry(int col, int row) {
		int index= get_index(col,row);
		BoardEnt be= entries[index];
		int i;

		if (be == null) {
			return;
		}
	
		entries[index]= null;
		for (i= 0; i < number; i++) {
			if (onboard[i] == be) {
				break;
			}
		}
		if (i < number) {
			number--;
			onboard[i]= onboard[number];
			onboard[number]= be;
		}

		clear_entry(be, true);
	}
		
	public int swapper_move(Graphics g) {
		int which= Math.abs(rand.nextInt()) % number;
		int direc= Math.abs(rand.nextInt()) % 4;
		BoardEnt be;
		int r1, c1, r2, c2;

		be= onboard[which];
		c1= be.col;
		r1= be.row;
		
		switch (direc) {
		case 0:
				c2= c1 + 1;
				r2= r1;
				break;
		case 1: 
				c2= c1 - 1;
				r2= r1;
				break;
		case 2:
				c2= c1;
				r2= r1 + 1;
				break;
		case 3:
				c2= c1;
				r2= r1 - 1;
				break;
		}
		if (c2 < 0 || c2 >= max_cols || r2 < 0 || r2 >= max_rows) {
			del_entry(c1, r1);
		} else {
			swap(c1, r1, c2, r2);
		}
		return (number);
	}

	public void swap(int c1, int r1, int c2, int r2) {
		int index1= get_index(c1, r1);
		int index2= get_index(c2, r2);
		
		BoardEnt e1= entries[index1];
		BoardEnt e2= entries[index2];

		if (e1 == null || e2 == null) {
			if (e1 == null && e2 == null) {
				
				return;
			}
			if (e2 == null) {
				e1.col= c2;
				e1.row= r2;
				entries[index1]= null;
				entries[index2]= e1;

				update_entry(e1, true);
			} else {
				e2.col= c1;
				e2.row= r1;
				entries[index1]= e2;
				entries[index2]= null;

				update_entry(e2, true);
			}
		} else {
			
			e1.col= c2;
			e1.row= r2;
			e2.col= c1;
			e2.row= r1;

			entries[index1]= e2;
			entries[index2]= e1;

			update_entry(e1, false);
			update_entry(e2, false);	
		}
	}
}


