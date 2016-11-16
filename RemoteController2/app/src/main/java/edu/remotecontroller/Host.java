package edu.remotecontroller;

/**
 * Created by emazwok on 2016-10-28.
 */

public class Host {
    private int m_id;
    private String m_hostname;
    private String m_ipAddress;
    private int m_port;

    public void setId(int _id) {
        this.m_id = _id;
    }

    public void setHostName(String _hostname) {
        this.m_hostname = _hostname;
    }

    public void setIpAddress(String ipAddress) {
        this.m_ipAddress = ipAddress;
    }

    public void setPort(int port) {
        this.m_port = port;
    }

    public int getId() {
        return m_id;
    }

    public String getHostName() {
        return m_hostname;
    }

    public String getIpAddress() {
        return m_ipAddress;
    }

    public int getPort() {
        return m_port;
    }

    public Host(int _id, String _hostname, String ipAddress, int port) {
        this.m_id = _id;
        this.m_hostname = _hostname;
        this.m_ipAddress = ipAddress;
        this.m_port = port;
    }

    public String toString()
    {
        String retString = Integer.toString(this.m_id) + ">  " + this.m_hostname + " " + this.m_ipAddress + ":" + this.m_port;
        return retString;
    }

}
