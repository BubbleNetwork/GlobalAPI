package com.thebubblenetwork.api.global.plugin.updater;

import com.thebubblenetwork.api.global.sql.SQLConnection;

import java.sql.SQLException;

public interface SQLUpdater {
    void update(SQLConnection connection) throws SQLException, ClassNotFoundException;

    String getName();
}
