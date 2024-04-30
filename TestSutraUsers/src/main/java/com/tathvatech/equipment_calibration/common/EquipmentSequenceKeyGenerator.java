package com.tathvatech.equipment_calibration.common;
import com.tathvatech.common.utils.SequenceIdGenerator;
public class EquipmentSequenceKeyGenerator
{
    private static String SEQUENCE_KEY = "Equipment";
    private   SequenceIdGenerator sequenceIdGenerator;

    public EquipmentSequenceKeyGenerator() {

    }

    public String getNextSeq(String equipmentabbrev) throws Exception
    {
        if(sequenceIdGenerator!=null) {
            int seqNo = sequenceIdGenerator.getNextSequence(SEQUENCE_KEY, equipmentabbrev, null, null, null);
            return equipmentabbrev + String.format("%04d", seqNo);
        }
        return equipmentabbrev;
    }
}

