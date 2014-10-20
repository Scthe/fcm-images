package main;

import DataStorage.PictureData;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class FCM {

    private PictureData _data; // wszelkie dane dotyczace algorytmu
    private U_MembershipMatrix _uMatrix; // macierz przynaleznosci piksela do klastra
    private boolean _running = true; // wskazuje, czy algorytm w dalszym ciagu dziala
    private static InputThread _inputThread = new InputThread(); // watek, ktory umozliwia pobranie od uzytkownika informacji np. polecenia zatrzymania algorytmu

    /**
     <p/>
     @param f plik zdjecia
     @param c parametr klasteryzacji
     */
    public FCM( File f, int c ) throws IOException {
        _data = new PictureData( f );
        _uMatrix = new U_MembershipMatrix( c, _data.getSize() );

        if ( !_inputThread.isDaemon() ) _inputThread.setDaemon( true );
        if ( !_inputThread.isAlive() ) _inputThread.start();
    }

    /**
     Glowna metoda algorytmu, uruchamia ona watek 'komunikacji z uzytkownikiem',
     a nastepnie wykonuje petle aktualizacji centroidow i wyliczania nowych odleglosci
     obiektow od tychze srodkow oraz aktualizacji macierzy przynaleznosci.
     */
    public void execute( int c, int m, float e, int maxIteration ) {
        D_DistancesMatrix d = new D_DistancesMatrix();
        int iter = 0;
        _inputThread.setFCM( this );

        System.out.println( "> Write 'stop' in console to stop process and deffuzify result"
                + "\n> ( it still requries some time to finish current iteration)" );
        do {
            V_CentersMatrix v = new V_CentersMatrix( _uMatrix, _data, c, m );   // tablica zawierajace
            //                                                                     dane o wyznaczonych srodkach grup
            d.calculateDistances( v, _uMatrix, c, m, _data ); 	 // metoda ta aktualizuje takze macierz przynaleznosci
            //                                                      wiecej na ten temat w opisie klasy 'D_DistancesMatrix'
            System.out.print( "iteration: " + iter + "   " );
        } while ( !_uMatrix.isAccurateEnough( e ) && isRunning() && iter++ < maxIteration );
        setRunning( false );
    }

    /** utworz ostateczny obrazek i - jesli flaga 'save' jest true -zapisz go na dysku */
    public BufferedImage createFinalImage( String outPath, String orgFileName,
                                           boolean save, int[] colors ) {

        BufferedImage src = _data.getSourceImage();
        _uMatrix.deffuzify( _data );
        BufferedImage result = _data.createResultImage( colors ); // rezultat dzialania algorytmu
        int totalW = src.getWidth() * 2;
        int totalH = src.getHeight();

        //obraz, gdzie lewa polowe zajmuje oryginalny obraz, a prawa wynik dzialania algorytmu
        BufferedImage res = new BufferedImage( totalW, totalH, src.getType() );
        Graphics2D drawG = ( Graphics2D ) res.getGraphics();
        drawG.drawImage( src, 0, 0, src.getWidth(), src.getHeight(), null );
        drawG.drawImage( result, src.getWidth(), 0, src.getWidth(),
                         src.getHeight(), null );

        if ( save ) saveImage( outPath, orgFileName, res );
        return res;
    }

    // ~
    private void saveImage( String outPath, String orgFileName, BufferedImage res ) {

        String out = orgFileName.substring( orgFileName.lastIndexOf( '\\' ),
                                            orgFileName.length() - 4 );
        out = outPath + "/" + out;
        File file = new File( out + "_res.jpg" );
        try {
            ImageIO.write( res, "jpg", file );
        } catch ( IOException e ) {
            System.err.println( "Write error for " + file.getPath() + ": "
                    + e.getMessage() );
            e.printStackTrace();
        }
    }

    /* Ponizsze metody sluza do komunikacji miedzy watkiem
     ' zatrzymania klasteryzacji' -> _inputThread a watkiem glownym */
    public synchronized boolean isRunning() {
        return _running;
    }

    public synchronized void setRunning( boolean r ) {
        _running = r;
    }
}
