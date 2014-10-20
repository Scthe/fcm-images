package main;

import DataStorage.PictureData;

public class D_DistancesMatrix {

    /**
     jest to wlasciwa metoda macierzy odleglosci.
    Wbrew nazwie, obiekt tej klasy nie posiada zadnej tablicy.
    W celu zaoszczedzenia na pamieci wyliczane sa odleglosci dla
    kazdego piksela pojedynczo i zapisywane w malej tablicy pomocniczej tmpArray.
    Ta tablica jest natychmiastowo wysylana do macierzy przynaleznosci,
    ktora dysponujac tymi danymi moze bez przeszkod zaktualizowac swoj stan.
     */
    public void calculateDistances( V_CentersMatrix vMat, U_MembershipMatrix uMat,
                                    int c, int m, PictureData data ) {

        float[] tmpArr = new float[c];
        uMat.preUpdate();

        int elem = data.getSize();
        for ( int i = 0; i < elem; i++ ) {
            for ( int j = 0; j < c; j++ )
                // policz odleglosci od srodkow klastrow
                // do danego elementu
                tmpArr[j] = calculateDistance( j, i, vMat, data );
            /* tmpArr <-kolumna macierzy */
            uMat.updateColumn( i, tmpArr, m );
        }
    }

    /**
     liczy odleglosc miedzy centroidem a pikselem
     */
    private float calculateDistance( int row, int elemID, V_CentersMatrix centers,
                                     PictureData data ) {

        float sum = 0;
        for ( int i = 0; i < data.getValueCount(); i++ ) {
            float a = data.getValue( elemID, i );
            float b = centers.get( row, i );
            a = a - b;
            sum += a * a;
        }
        return sqrt( sum );
    }

    /**
     szybka metoda liczenia pierwiastka, wiecej informacji znajduje sie w intenecie pod haslem 'inverse square root quake 3 algorithm'
     */
    private float sqrt( float x ) {
        float xhalf = 0.5f * x;
        int i = Float.floatToIntBits( x ); // get bits for floating value
        i = 0x5f375a86 - ( i >> 1 ); // gives initial guess y0
        x = Float.intBitsToFloat( i ); // convert bits back to float
        x = x * ( 1.5f - xhalf * x * x ); // Newton step, repeating increases accuracy
        return 1 / x;

    }
}
