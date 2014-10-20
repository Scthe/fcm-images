package DataStorage;

import java.awt.image.BufferedImage;

public abstract class PixelData {

    // ~
    private int[] _values; // tablica danych, gdzie kolejno
    // sa zapisane wszystkie wartosci na temat kazdego piksela
    // dostep jest realizowany przy uzyciu masek bitowych, kazda wartosc powinna miescic sie na 1 bajcie
    private byte[] _groups; // kazdy bajt to kolejno zapisane klastry
    // np [1,2,1,3, 2] ozacza, ze pierwszy piksel nalezy do klastra 1, 2'gi do klastra 2 itp.
    // oczywiscie tablica ta jest wieksz niz te 5 elementow
    private static int in, przes; // wykorzystywane przy zapisie kolejnych bajtow
    private int iloscCech; // ilosc 'cech' ktore charakteryzuja kazdy piksel,
    //moga to byc np. wartosci poszczegolnych kanalow dla kolorow czerwonego, niebieskiego, zielonego

    /**
     <p/>
     @param size      ilosc pikseli obrazu
     @param iloscCech ilosc cech, ktore badziemy rozpatrywac
     */
    public PixelData( int size, int iloscCech ) {
        int s = size * iloscCech / 4 + 3; // 4-> ukrywamy 4 wartosci w typie int,
        //                                   dostep do nich bedzie za pomoca masek bitowych.
        //                                   Do dyspozycji mamy wiec wartosci [ 0, 255]
        _values = new int[s];
        _groups = new byte[size];
        this.iloscCech = iloscCech;
        in = przes = 0;
    }

    /**
     Ta metoda pobiera ze zdjecia jakies ingormacje
     i z uzyciem metody setNextValue() ustawia kolejne wartosci
     dla kolejnych pikseli. Przestawione jest to w klasie przykladowej 'RGBToBlackAndWhite'
     */
    protected abstract void ReadImageInformation( BufferedImage srcImage );

    /**
     Wstaw kolejne wartosci do tablicy przechowujacej dane dla obrazu.
     <p/>
     @param val tablica o dlugosci rownej 'iloscCech',
     ktora przechowuje wartsci z zakresu [0,255] odpowiadajace stopniowi
     wystepowanie okreslonej cechy w danym pikselu
     */
    protected void setNextValue( int[] val ) {
        for ( int i = 0; i < iloscCech; i++ ) {
            if ( przes == 4 ) {
                przes = 0; // ktory bajt z wartosci typu int ma zostac zapisany
                _values[++in] = 0;
            }
            int v = val[i] & 0xff;// [ 0. 255]
            v = v << ( przes * 8 );   // ( i * 8 )
            _values[in] |= v;
            przes++;
        }
    }

    /**
     Pobierz wartosc cechy ( cecha ) dla piksela o numerze (index)
     */
    protected int value( int index, int cecha ) {

        int i = ( index ) * iloscCech + cecha; // ktory bajt, czytajac buffor od poczatku
        int a = _values[i / 4]; // wartosc int, jeden z bajtow tej wartosci
        //                         zawiera informacje, ktora poszukujemy

        int move = ( i % 4 ) * 8; // o ile nalezy przesunac 'int', liczone w bitach
        // tworzymy odpowiednia maske do operacji 'and'
        int mask = 0xff << move;
        a = a & mask;
        return a >> move;
    }

    protected int getValueCount() { // ilosc cech dla kazdego piksela
        return iloscCech;
    }

    protected void setGroup( int index, int val ) {
        _groups[index] = ( byte ) val;
    }

    protected int getGroup( int index ) {
        return _groups[index];
    }
}
