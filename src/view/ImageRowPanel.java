package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@ SuppressWarnings ("serial")
public class ImageRowPanel extends JPanel {
	
	private final DebugFrame _parent;
	private float zoom = 60;
	
	public ImageRowPanel( DebugFrame frame) {
		_parent = frame;
	}
	
    /**
    Ponizsza metoda nie musi byc specjalnie optymalna
    */
    @Override
	protected void paintComponent( Graphics g) {
		Graphics2D g2 = ( Graphics2D) g;
		g2.setColor( this.getBackground( ));
		
		final BufferedImage draw = _parent.getResultImage( );
		if (draw != null) {
			int totalW = draw.getWidth( );
			int totalH = draw.getHeight( );
			
			Point Center = new Point( getWidth( ) / 2, getHeight( ) / 2);
			float z = zoom;
			if (totalW * zoom > getWidth( ) || totalH * zoom > getHeight( )) {
				float a = getWidth( ) * 1f / totalW;
				float b = getHeight( ) * 1f / totalH;
				z = Math.min( a, b);
			}
			
			totalW *= z;
			totalH *= z;
			g2.drawImage( draw, Center.x - totalW / 2, Center.y - totalH / 2,
					totalW, totalH, null);
		}
	}
	
}
