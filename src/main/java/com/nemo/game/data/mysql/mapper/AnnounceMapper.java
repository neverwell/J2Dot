package com.nemo.game.data.mysql.mapper;

import com.nemo.common.jdbc.RowMapper;
import com.nemo.game.back.constant.AnnounceField;
import com.nemo.game.back.entity.Announce;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AnnounceMapper implements RowMapper<Announce> {
    @Override
    public Announce mapping(ResultSet rs) throws SQLException {
        Announce announce = new Announce();
        announce.setId(rs.getLong(AnnounceField.ID));
        announce.setStarTime(rs.getInt(AnnounceField.START_TIME));
        announce.setEndTime(rs.getInt(AnnounceField.END_TIME));
        announce.setPeriod(rs.getInt(AnnounceField.PERIOD));
        announce.setType(rs.getInt(AnnounceField.TYPE));
        announce.setContent(rs.getString(AnnounceField.CONTENT));
        return announce;
    }

    @Override
    public void release() {
    }
}
