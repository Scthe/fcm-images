package DataStorage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PictureData {

    /*
     klasa ta przechowuje dane na temat zdjecia
     m.in. wymiary jak i informace uzyskane z jego analizy
     */
    private PixelData _bw;
    private final int w, h, size; // wymiary zdjecia
    private BufferedImage _srcImage = null; // obraz zrodlowy ( oryginaln, nie poddany klasteryzacji)

    // ~
    public PictureData( File source ) throws IOException {
        System.out.println( "file: " + source.getAbsolutePath() );
        _srcImage = ImageIO.read( new File( source.getAbsolutePath() ) );

        System.out.println( "reading image data" );
        w = _srcImage.getWidth();
        h = _srcImage.getHeight();
        size = w * h;
        _bw = new RGBToBlackAndWhite( size ); // ! tutaj mozna uzyc wlasnego obiektu dziedziczacego po 'PixelData'
        _bw.ReadImageInformation( _srcImage ); // wtedy ta linijke mozna pozostawic bez zmian
        System.out.println( "image data stored" );

    }

    /**
     <p/>
     @param colors kolory, ktore maja zostac uzyte do malowania obrazka
     bedacego wynikiem dzialania algorytmu
     @return obraz o wymiarach rownych wymiarom obrazka zrodlowego
     z naniesionymi informacjami na temat klastrow
     */
    public BufferedImage createResultImage( int[] colors ) {
        BufferedImage resImage = new BufferedImage( w, h, _srcImage.getType() );

        int index = 0;
        for ( int i = 0; i < w; i++ )
            for ( int j = 0; j < h; j++, index++ ) {
                int gr = _bw.getGroup( index );
                resImage.setRGB( i, j, colors[gr] );
            }
        return resImage;
    }

    public void setGroup( int index, int val ) {
        _bw.setGroup( index, val );
    }

    public final BufferedImage getSourceImage() {
        return _srcImage;
    }

    public int getSize() {
        return size;
    }

    public int getValue( int index, int cecha ) {
        return _bw.value( index, cecha );
    }

    public int getValueCount() { // ilosc 'cech', ktore posiada kazdy piksel
        return _bw.getValueCount();
    }
}
