package main;

import java.util.Scanner;

public class InputThread extends Thread {

    /*
    Klasa ta nie polega na java.util.concurrent ani nawet na wait(),
    Watek ten dziala przez caly czas, a jedyna komunikacja z watkiem glownym
    polega na zmianie 'currentWork' <- za kazdym razem gdy klasteryzowane
    jest kolejne zdjecie ten watek otrzymuje instancje klasy FCM, wspoldzielona
    miedzy tymi 2 watkami. Gdy z jakiegos powodu proces klasteryzacji sie zakonczy,
    referencja currentWork jest usuwana z pamieci przez zastapienie
    jej wartoscia null. Gdy watek glowny poprosi o wznowienie pracy, ustawi
    currentWork na inna wartosc a praca tego watku bedzie kontunuowana.
    Watek ten jest traktowany jako daemon, wiec nie przeszkadza
    przy zakonczaniu programu.
    
    Co ma na celu ta klasa ?
    Otoz przy wykorzystaniu osobnego watku dla kazdego obrazu,
    nawet jesli te watki bylyby daemonami, referencja do obiektu FCM
    nie bylaby czyszczona gdy watek pozornie skonczyl swoje dzialanie.
    Powodowalo to problemy z brakiem pamieci - istotnie, skoro kazdy obraz
    mial swoja referencje w prywatnym watku, to Garbage Collector
    zawsze uznaje, iz ktos posiada jeszcze mozliwosc skorzystania
    z tego obiektu i nigdy nie zwalnia przez to zaalokowanej pamieci.
    
    Rozwiazanie jest proste ale dziala calkiem niezle,
    warto jednak je poprawic przy wykorzystaniu np. java.util.concurrent.
    
    */
    
    private FCM currentWork;

    public synchronized void setFCM( FCM p ) {
        currentWork = p;
    }

    @Override
    public void run() {

        while ( true )
            try {
                if ( currentWork != null ) {
                    exec();
                    currentWork = null;
                } else currentWork = null;
                sleep( 1000 );
            } catch ( InterruptedException ex ) {
            }
    }

    private void exec() {
        Scanner sc = new Scanner( System.in );
        try {
            while ( currentWork.isRunning() )
                if ( sc.hasNext() && sc.next().equals( "stop" ) )
                    currentWork.setRunning( false );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        sc.close();
    }
}
