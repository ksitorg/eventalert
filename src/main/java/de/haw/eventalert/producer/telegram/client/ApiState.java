package de.haw.eventalert.producer.telegram.client;

import org.telegram.api.TLConfig;
import org.telegram.api.auth.TLAuthorization;
import org.telegram.api.engine.storage.AbsApiState;
import org.telegram.mtproto.state.AbsMTProtoState;
import org.telegram.mtproto.state.ConnectionInfo;

/**
 * Created by Tim on 06.09.2017.
 */
public class ApiState implements AbsApiState {
    @Override
    public int getPrimaryDc() {
        return 0;
    }

    @Override
    public void setPrimaryDc(int dc) {

    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public boolean isAuthenticated(int dcId) {
        return false;
    }

    @Override
    public void setAuthenticated(int dcId, boolean auth) {

    }

    @Override
    public void updateSettings(TLConfig config) {

    }

    @Override
    public byte[] getAuthKey(int dcId) {
        return new byte[0];
    }

    @Override
    public void putAuthKey(int dcId, byte[] key) {

    }

    @Override
    public ConnectionInfo[] getAvailableConnections(int dcId) {
        return new ConnectionInfo[0];
    }

    @Override
    public AbsMTProtoState getMtProtoState(int dcId) {
        return null;
    }

    @Override
    public void doAuth(TLAuthorization authorization) {

    }

    @Override
    public void resetAuth() {

    }

    @Override
    public void reset() {

    }

    @Override
    public int getUserId() {
        return 0;
    }
}
