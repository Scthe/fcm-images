package main;

import DataStorage.PictureData;

public class V_CentersMatrix {

    /* jest to macierz srodkow klastrow */
    private float[][] _data;

    /**
    w tym konstruktorze odrazu wyliczane sa wartosci centroidow dla kazdego z klastrow 
    */
    public V_CentersMatrix( U_MembershipMatrix u, PictureData data, int c, int m ) {
        _data = new float[c][data.getValueCount()];
        int nElem = data.getSize();

        for ( int i = 0; i < data.getValueCount(); i++ )
            for ( int j = 0; j < c; j++ )
                _data[j][i] = centerCalc( j, i, nElem, m, u, data );
    }

    private float centerCalc( int row, int cecha, int elem, int m,
                       U_MembershipMatrix u, PictureData data ) {

        float down = 0, up = 0;             // 'up' -> divident (licznik) ; 'down' -> divisor(mianownik)
        for ( int i = 0; i < elem; i++ ) {
            float uV = 1;
            for ( int j = 0; j < m; j++ ) // podnosimy [row, i] do potegi m
                uV *= u.get( row, i );      // przy uzycu petli for, aby zaoszczedzic
            //czas wykorzystywany przez Math.pow 
            down += uV;                     // suma 'kwadratow' / (m) stopnia przynaleznosci
            int a = data.getValue( i, cecha );
            up += uV * a;                   // suma 'kwadratow' / (m) stopnia przynaleznosci
            //przemnozona przez wartosc okreslonej 'cechy'
        }
        return up / down;
    }

    public float get( int row, int col ) {
        return _data[row][col];
    }

    // ~
    @ Override
    public String toString() {
        StringBuilder sb = new StringBuilder( getClass().getSimpleName()
                + "[\n" );
        for ( int i = 0; i < _data.length; i++ ) {
            for ( int j = 0; j < _data[0].length; j++ )
                sb.append( String.format( "%8.1f__", _data[i][j] ) );
            sb.append( "\n" );
        }
        sb.append( "]" );
        return sb.toString();
    }
}
