package view;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

@ SuppressWarnings ( "serial" )
public class DebugFrame extends JFrame {

    private final BufferedImage src;

    public DebugFrame( BufferedImage img ) {
        setTitle( "ImageView" );
        setSize( 600, 550 );
        setResizable( true );
        setMinimumSize( new Dimension( 400, 300 ) );

        src = img;
        ImageRowPanel MainPanel = new ImageRowPanel( this );
        add( MainPanel );
        this.addKeyListener( new KeyListener() {

            @ Override
            public void keyTyped( KeyEvent e ) {
            }

            @ Override
            public void keyReleased( KeyEvent e ) {
                if ( e.getKeyCode() == KeyEvent.VK_ESCAPE )
                    System.exit( 0 );
            }

            @ Override
            public void keyPressed( KeyEvent e ) {
            }
        } );
    }

    public BufferedImage getResultImage() {
        return src;
    }
}
