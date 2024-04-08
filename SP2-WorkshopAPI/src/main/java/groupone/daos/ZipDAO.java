package groupone.daos;

import groupone.model.Location;
import groupone.model.Zipcode;

public class ZipDAO extends DAO<Zipcode, Integer> {
    private static ZipDAO instance;

    public static ZipDAO getInstance(boolean isTesting){
        if(instance == null){
            instance = new ZipDAO(isTesting);
        }
        return instance;
    }
    public ZipDAO(boolean isTesting) {
        super(Zipcode.class, isTesting);
    }
}
