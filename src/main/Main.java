package main;

import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import javax.swing.JFrame;

import view.DebugFrame;

public class Main {

    // ~	
    private final static String out = "C:/programs"; // katalog, gdzie zostanie umieszczony rezultat algorytmu
    private final static String dir = "C:/programs"; // katalog, gdzie znajduja sie obrazy, ktore maja zostac poddane klasteryzacji
    private final static boolean directory = false; // true, gdy klasteryzacji ma zostac poddany kazdy obraz znajdujacu sie w katalogu 'dir'
    private final static String fileName = "uncle_sam.jpg"; // jesli 'directory' jest false,
    // klasteryzacji poddane zostanie tylko 1 zdjecie z katalogu 'dir' o nazwie podanej w tym parametrze. Pamietaj o wlasciwy rozszerzeniu pliku
    // ~
    private static final int c = 3; // ilosc klastrow, przy wiekszej ilosci nalezy zwiększyć wielkość tablicy 'colors'
    private static final int m = 2; // parametr rozmycia
    private static final float e = 0.001f; // dokladnosc algorytmu
    private static final boolean storeResult = true; // true jesli chcemy zapisac rezultat w katalogu o sciezce zapisanej w zmiennej 'out'
    private static final int[] colors = { 0xfff0f0f0, 0xff758090, 0xffa3a3a3,
        0xff202020, 0xfaf0d0d0 }; // kolory, ktore beda symbolizowaly poszczegolne klastry ( zapisane w formacie 0x[alfa][red][green][blue])
    private static final int maxIteration = 60; // maksymalna ilosc iteracji algorytmu
    // ~

    public static void main( String[] args ) {
        try {

            if ( directory ) {
                File f = new File( dir );
                File[] ph = f.listFiles( new FileFilter() { //lista obrazow

                    String[] ext = { "png", "jpg", "bmp" };

                    @Override
                    public boolean accept( File f ) {
                        boolean r = false;
                        for ( String extension : ext )
                            if ( f.getName().toLowerCase().endsWith( extension ) )
                                r = !r;
                        return r;
                    }
                } );

                for ( int i = 0; i < ph.length; i++ )
                    clusterize( ph[i], false );
            } else {
                File f = new File( dir + "/" + fileName );
                clusterize( f, true );
            }
        } catch ( Exception ex ) {
            ex.printStackTrace();
        }
        System.out.println( "FINISHED" );
    }

    private static void clusterize( File f, boolean showWindow ) throws IOException {
        FCM fcm = new FCM( f, c );

        long timer = System.currentTimeMillis();
        fcm.execute( c, m, e, maxIteration );
        timer = ( System.currentTimeMillis() - timer ) / 1000;
        final BufferedImage res = fcm.createFinalImage( out, f.getAbsolutePath(), storeResult, colors );

        System.out.println( String.format( "time: %ds = %5.1fmin\n", timer,
                                           ( timer / 60f ) ) );

        if ( showWindow )
            EventQueue.invokeLater( new Runnable() { // utworz nowe okno, gdzie zostanie wyswietlony rezultat dzialania algorytmu

                @Override
                public void run() {
                    DebugFrame frame = new DebugFrame( res );
                    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                    frame.setVisible( true );
                }
            } );
    }
}
