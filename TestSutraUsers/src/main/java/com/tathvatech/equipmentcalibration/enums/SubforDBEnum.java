package com.tathvatech.equipmentcalibration.enums;

import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.user.enums.DBEnum;
import org.springframework.stereotype.Component;

@Component
public class SubforDBEnum extends DBEnum {

  private long pk;
  private String Name;

    public SubforDBEnum(PersistWrapper persistWrapper) {
        super(persistWrapper);
    }

    @Override
    public String getName() {
        return Name;
    }

    @Override
    public void setName(String name) {
        Name = name;
    }

    @Override
    public long getPk() {
        return pk;
    }

    @Override
    public void setPk(long pk) {
        this.pk = pk;
    }






}
