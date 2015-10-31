package com.marksoft.logger.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DhcpClient.
 */
@Entity
@Table(name = "dhcp_client")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DhcpClient implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    
    @Column(name = "mac_address")
    private String macAddress;
    
    @Column(name = "hostname")
    private String hostname;
    
    @Column(name = "comment")
    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DhcpClient dhcpClient = (DhcpClient) o;

        if ( ! Objects.equals(id, dhcpClient.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DhcpClient{" +
                "id=" + id +
                ", macAddress='" + macAddress + "'" +
                ", hostname='" + hostname + "'" +
                ", comment='" + comment + "'" +
                '}';
    }
}
