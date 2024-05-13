package com.tathvatech.equipmentcalibration.enums;

import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.user.enums.DBEnum;
import org.springframework.stereotype.Component;

@Component
public class SubforDBEnum extends DBEnum {

    protected SubforDBEnum(PersistWrapper persistWrapper) {
        super(persistWrapper);
    }

    @Override
    public long getPk() {
        return 0;
    }

    @Override
    public void setPk(long val) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String string) {

    }
}
