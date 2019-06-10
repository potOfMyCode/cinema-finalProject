package model.service;

import model.dao.DaoFactory;
import model.dao.DayDao;
import model.dao.exceptions.DaoException;
import model.entity.Day;
import model.util.LogGen;
import org.apache.log4j.Logger;

import java.util.Locale;

import static model.util.LogMsg.*;

public class DayService {
    private Logger log = LogGen.getInstance();
    private DaoFactory daoFactory = DaoFactory.getInstance();

    public Day getDayById(int id) {
        Day day = new Day();

        try (DayDao dao = daoFactory.createDayDao()) {
            day = dao.getEntityById(id);
        } catch (DaoException e) {
            log.error(CANT_GET_DAY_BY_ID);
        }
        log.info(GOT_DAY_BY_ID_FROM_DB);
        return day;
    }

    public void setDaoLocale(Locale locale) {
        daoFactory.setDaoLocale(locale);
        log.info(DAO_LOCALE_IS_SET + " for " + daoFactory.getClass().getSimpleName() + " as " + locale.toString());
    }
}
