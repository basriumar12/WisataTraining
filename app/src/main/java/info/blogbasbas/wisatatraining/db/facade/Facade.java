package info.blogbasbas.wisatatraining.db.facade;

import info.blogbasbas.wisatatraining.db.model.DaoSession;
import info.blogbasbas.wisatatraining.db.model.WisataItem;

/**
 * Created by User on 24/03/2018.
 */

public class Facade {
    private static Facade instance;
   final DaoSession session;
   final ManageWisataTbl manageWisataTbl;

    public static void init(DaoSession daoSession) {
        instance = new Facade(daoSession);
    }
    public static Facade getInstance() {
        return instance;
    }

    public Facade(DaoSession daoSession) {
        this.session =daoSession;
        manageWisataTbl = new ManageWisataTbl(this);


    }

    public DaoSession getSession() {
        return session;
    }

    public ManageWisataTbl getManageWisataTbl() {
        return manageWisataTbl;
    }

}
