package model.dao.mappers;

import model.entity.Ticket;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

public class TicketMapper implements ObjectMapper<Ticket> {
    @Override
    public Ticket extractFromResultSet(ResultSet resultSet, int... columnIndexes) throws SQLException {
        Ticket ticket = new Ticket();

        ticket.setId(resultSet.getInt(columnIndexes[0]));
        ticket.setPlace(resultSet.getInt(columnIndexes[1]));
        ticket.setUserID(resultSet.getInt(columnIndexes[2]));
        ticket.setSessionID(resultSet.getInt(columnIndexes[3]));


        Optional<Date> date = Optional.ofNullable(resultSet.getDate(columnIndexes[4]));

        LocalDate parseDate = date.map(Date::toLocalDate).orElse(null);

        ticket.setDate(parseDate);

        return ticket.notEmpty() ? ticket : null;
    }

    @Override
    public Ticket makeUnique(Map<Integer, Ticket> cache, Ticket entity) {
        cache.putIfAbsent(entity.getId(), entity);
        return cache.get(entity.getId());
    }
}
