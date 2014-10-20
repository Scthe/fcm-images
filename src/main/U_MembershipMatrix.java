package main;

import DataStorage.PictureData;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class U_MembershipMatrix {

    /* Macierz przynaleznosci pikseli do poszczegolnych klastrow */
    private final int c;  // parametr symulacji
    private final int _nElem; // liczba elementow
    private float _error;  // blad ostatniej aktualizcji macierzy ( deltaU)
    private FloatBuffer _data; // wlasciwa macierz, zastosowanie FloatBuffer pozwala
    //programowi na operowanie na ilosci danych wieksza niz sterta ( heap) ogranicajaca wirtualna maszyne

    // ~
    public U_MembershipMatrix( int c, int nElem ) {
        this.c = c;
        _nElem = nElem;

//        if ( _data != null ) _data.clear();
        _data = ByteBuffer.allocateDirect( nElem * c * 4 ).order( ByteOrder.nativeOrder() ).asFloatBuffer();

        fillRandom();
    }

    // ~
    /**
     wyzerowanie zmiennej error przed aktualizacja nowych przynaleznosci
     */
    public void preUpdate() {
        _error = Float.MIN_VALUE;
    }

    /**
     uzywana przez by D_matrix, w celu natychmiastowej aktualizacji przynaleznosci jednego elementu ( piksela) oznaczonego jako col
     */
    public void updateColumn( int col, float[] distArr, int m ) {
        boolean zero = false;
        for ( int i = 0; i < distArr.length && !zero; i++ ) // sprawdzamy czy przypadkiem punkt nie znalazl sie idealnie w jednym z centroidow
            if ( distArr[i] == 0 ) { // ta mozliwosc jest w zasadzie czysto teoretyczna,
                zero = true; //         lecz mozliwe jest, ze faktycznie trafimy na taki przypadek
                for ( int j = 0; j < distArr.length; j++ )
                    setData( j, col, ( i == j ) ? 1 : 0 );
            }
        if ( !zero )
            for ( int i = 0; i < distArr.length; i++ ) {
                float u = newMembership( distArr, i, m ); // wylicz nowa wartosc
                float prev = get( i, col ); // poprzednia wartosc
                setData( i, col, u );
                float e = Math.abs( u - prev ); // ponizszy kod ma na celu aktualiacje wartosci zmiennej deltaU ( bledu)
                if ( e > _error )
                    _error = e;
            }
    }

    /* true oznacza, ze algorytm osiagnal wymagana dokladnosc danych i powinien sie zakonczyc */
    public boolean isAccurateEnough( float tolerance ) {
        System.out.println( "e: " + _error );
        return _error < tolerance;
    }

    /**
     metoda ta zmiena wartosci rozmyte w konkretne wartosci, oznaczajace przynaleznosc piksela do okrelsonej grupy
     */
    public void deffuzify( PictureData data ) {
        for ( int i = 0; i < _nElem; i++ ) {
            int maxInd = 0;
            for ( int j = 1; j < c; j++ ) //              znajdz najwieksza
                if ( get( j, i ) > get( maxInd, i ) ) // 'przynaleznosc' dla kazdego elemenu
                    maxInd = j;
            data.setGroup( i, maxInd ); // przydziel element do grupy
        }
    }

    public float get( int row, int col ) {
        return _data.get( _nElem * row + col );
    }

    // ~
    private void fillRandom() { //poczatkowe 'losowe' wypelnienie macierzy
        for ( int i = 0, j = 0; i < _nElem; i++, j++ ) { // i -> element ; j -> wiersz
            if ( j >= c )
                j = 0;
            setData( j, i, 1 );
        }
    }

    /**
     <p/>
     @param distArr tablica odleglosci piksela od centroidow
     @param setID   ktory ' rzad' macierzy ( ktory klaster )
     @param m       parametr symulacji
     @return
     */
    private float newMembership( float[] distArr, int setID, int m ) {
        float sum = 0;
        float base = distArr[setID];
        for ( int i = 0; i < distArr.length; i++ ) {
            float a = base / distArr[i];
            sum += Math.pow( a, 2 / ( m - 1 ) );
        }
        return 1 / sum;
    }

    private void setData( int row, int col, float data ) {
        _data.put( _nElem * row + col, data );
    }
}
