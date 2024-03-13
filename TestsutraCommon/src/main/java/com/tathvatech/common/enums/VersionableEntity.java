package com.tathvatech.common.enums;

public interface VersionableEntity {

	public int getEntityPk();
	public EntityType getEntityType();
	public String getBackupString();
}
