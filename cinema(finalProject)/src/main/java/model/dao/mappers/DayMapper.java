package model.dao.mappers;

import model.entity.Day;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

public class DayMapper implements ObjectMapper<Day> {

    @Override
    public Day extractFromResultSet(ResultSet resultSet, int... columnIndexes) throws SQLException {
        Day day = new Day();
        day.setId(resultSet.getInt(columnIndexes[0]));
        Optional.ofNullable(resultSet.getString(columnIndexes[1])).ifPresent(day::setName);
        Optional.ofNullable(resultSet.getString(columnIndexes[2])).ifPresent(day::setShortName);
        return day.notEmpty() ? day : null;
    }

    @Override
    public Day makeUnique(Map<Integer, Day> existing, Day entity) {
        existing.putIfAbsent(entity.getId(), entity);
        return existing.get(entity.getId());
    }
}