package com.tathvatech.user.OID;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tathvatech.common.enums.EntityType;
import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.equipmentcalibration.oid.EquipmentOID;
import com.tathvatech.equipmentcalibration.oid.EquipmentTypeOID;
import com.tathvatech.equipmentcalibration.oid.LocationOID;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
    @JsonSubTypes.Type(value = UserOID.class, name = "UserOID"),
    @JsonSubTypes.Type(value = PersonOID.class, name = "PersonOID"),
		@JsonSubTypes.Type(value = EquipmentOID.class, name = "EquipmentOID"),
		@JsonSubTypes.Type(value = EquipmentTypeOID.class, name = "EquipmentTypeOID"),
		@JsonSubTypes.Type(value = LocationOID.class, name = "LocationOID"),
    @JsonSubTypes.Type(value = SiteOID.class, name = "SiteOID"),
    @JsonSubTypes.Type(value = PartOID.class, name = "PartOID"),
    @JsonSubTypes.Type(value = PartRevisionOID.class, name = "PartRevisionOID"),
    @JsonSubTypes.Type(value = SupplierOID.class, name = "SupplierOID"),
    @JsonSubTypes.Type(value = UnitOID.class, name = "UnitOID"),
    @JsonSubTypes.Type(value = ProjectOID.class, name = "ProjectOID"),
    @JsonSubTypes.Type(value = ProjectFormOID.class, name = "ProjectFormOID"),
    @JsonSubTypes.Type(value = WorkstationOID.class, name = "WorkstationOID"),
    @JsonSubTypes.Type(value = FormOID.class, name = "FormOID"),
    @JsonSubTypes.Type(value = FormSectionOID.class, name = "FormSectionOID"),
    @JsonSubTypes.Type(value = FormItemResponseOID.class, name = "FormItemResponseOID"),
    @JsonSubTypes.Type(value = LocationTypeOID.class, name = "LocationTypeOID"),
    @JsonSubTypes.Type(value = WhereFoundOID.class, name = "WhereFoundOID"),
    @JsonSubTypes.Type(value = ReworkOrderOID.class, name = "ReworkOrderOID"),
    @JsonSubTypes.Type(value = ImmediateActionRequiredOID.class, name = "ImmediateActionRequiredOID"),
    @JsonSubTypes.Type(value = InjuryAfterTreatmentOID.class, name = "InjuryAfterTreatmentOID"),
    @JsonSubTypes.Type(value = NearMissOID.class, name = "NearMissOID"),
    @JsonSubTypes.Type(value = NearMissAfterTreatmentOID.class, name = "NearMissAfterTreatmentOID"),
    @JsonSubTypes.Type(value = MRFOID.class, name = "MRFOID")
    })
public abstract class OID extends TSBeanBase implements Authorizable{
	long pk;
	EntityType entityType;
	String displayText;
	
	public OID()
	{
		
	}
	
	public OID(long pk, EntityType entityType, String displayText)
	{
		this.pk = pk;
		this.entityType = entityType;
		this.displayText = displayText;
	}
	
	public OID(long pk, int entityTypeEnumValue, String displayText)
	{
		this.pk = pk;
		this.entityType = EntityTypeEnum.fromValue(entityTypeEnumValue);
		this.displayText = displayText;
	}

	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public EntityType getEntityType() {
		return entityType;
	}
	public void setEntityType(EntityTypeEnum entityType) {
		this.entityType = entityType;
	}
	public String getDisplayText() {
		return displayText;
	}
	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}
	
	@Override
	@JsonIgnore
	public String toString()
	{
		return getDisplayText();
	}
	
	@Override
	@JsonIgnore
	public List<? extends Role> getSupportedRoles() 
	{
		return null;
	}

	@Override
	@JsonIgnore
	public List<? extends Action> getSupportedActions() 
	{
		return null;
	}
	
	@JsonIgnore
	public String getLoggingString()
	{
		return "pk:" + pk + ", name:" + displayText;
	}
	
}
