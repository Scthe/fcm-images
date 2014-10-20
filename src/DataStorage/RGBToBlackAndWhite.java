package DataStorage;

import java.awt.image.BufferedImage;

public class RGBToBlackAndWhite extends PixelData {

    /* color conversion array */
    private static float[] y = {   0.299f,  0.587f,   0.114f };
    private static float[] cb = { -0.169f, -0.331f,   0.5f };
    private static float[] cr = {  0.5f,   -0.419f,  -0.081f };
    private static float[] cg = { -0.169f,  0.5f,    -0.331f };

    /**
     <p/>
     @param size ilosc pikseli obrazu
     */
    public RGBToBlackAndWhite( int size ) {
        super( size, 4 ); //bedziemy wykorzystywac 4 informacje: Y, Cb, Cr, Cg
    }

    /**
     W tej metodzie na swoj sposob przetwarzamy obraz.
     <p/>
     W moim przypadku, kolejno przechodze przez wszystkie piksele,
     pobieram ich wartosc r,g,b i nastepnie za pomoca metody pomocniczej
     convertColor() uzyskuje pozadane wartosci, ktore zapisuje w tablicy.
     Ta tablice przekazuje do metody setNextValue() nadklasy.
     <p/>
     @param srcImage obraz do przetworzenia
     */
    @Override
    protected void ReadImageInformation( BufferedImage srcImage ) {
        int rMask = 0x00ff0000;
        int gMask = 0x0000ff00;
        int bMask = 0x000000ff;

        int w = srcImage.getWidth();
        int h = srcImage.getHeight();

        for ( int i = 0; i < w; i++ )
            for ( int j = 0; j < h; j++ ) {
                int rgb = srcImage.getRGB( i, j );
                int r = ( rgb & rMask ) >> 16;
                int g = ( rgb & gMask ) >> 8;
                int b = rgb & bMask;

                setNextValue( convertColor( r, g, b ) ); // wazne !
            }
    }

    // ~
    private int[] convertColor( int r, int g, int b ) {
        float Y = ( float ) ( y[0] * r + y[1] * g + y[2] * b );
        float Cb = ( float ) ( cb[0] * r + cb[1] * g + cb[2] * b );
        float Cr = ( float ) ( cr[0] * r + cr[1] * g + cr[2] * b );
        float Cg = ( float ) ( cg[0] * r + cg[1] * g + cg[2] * b );

        /*
         int d = ( ( int ) Math.max( 0, Cg ) << 24 )
         + ( ( int ) Math.max( 0, Cr ) << 16 ) // problem: when f.e Cb < 0:
         + ( ( int ) Math.max( 0, Cb ) << 8 ) // we cannot shift bits
         + ( ( int ) Math.max( 0, Y ) );
         */

        int[] re = {
            ( int ) Math.max( 0, Y ),
            ( int ) Math.max( 0, Cb ),
            ( int ) Math.max( 0, Cr ),
            ( int ) Math.max( 0, Cg ),
            ( int ) Math.random() * 255
        };
        return re;
    }
}
