package com.tathvatech.equipmentcalibration.entity;

import com.tathvatech.common.entity.AbstractEntity;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "equipment_calibration_authority")
public class EquipmentCalibrationAuthority  extends AbstractEntity implements Serializable
{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long pk;
    private String name;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String phone;
    private String fax;
    private String email;
    private int siteFk;

    private Integer estatus;
    private int createdBy;
    private Date createdDate;
    private Date lastUpdated;


    @Override
    public long getPk() {
        return pk;
    }

    public void setPk(long pk) {
        this.pk = pk;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAddressLine1()
    {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1)
    {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2()
    {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2)
    {
        this.addressLine2 = addressLine2;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getPostalCode()
    {
        return postalCode;
    }

    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getFax()
    {
        return fax;
    }

    public void setFax(String fax)
    {
        this.fax = fax;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public Integer getEstatus()
    {
        return estatus;
    }

    public void setEstatus(Integer estatus)
    {
        this.estatus = estatus;
    }

    public int getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(int createdBy)
    {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate)
    {
        this.createdDate = createdDate;
    }

    public int getSiteFk()
    {
        return siteFk;
    }

    public void setSiteFk(int siteFk)
    {
        this.siteFk = siteFk;
    }


    public Date getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated)
    {
        this.lastUpdated = lastUpdated;
    }


    public String getDisplayText()
    {
        return name;
    }

    @Override
    public int hashCode()
    {
        return (int) getPk();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof EquipmentCalibrationAuthority))
        {
            return false;
        }
        return (((EquipmentCalibrationAuthority) obj).getPk() == getPk());
    }

    @Override
    public String toString()
    {
        return getName();
    }

}
