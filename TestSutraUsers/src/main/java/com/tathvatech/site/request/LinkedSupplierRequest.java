package com.tathvatech.site.request;

import com.tathvatech.user.OID.SiteOID;
import com.tathvatech.user.OID.SupplierOID;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestBody;

@Data
public class LinkedSupplierRequest {

    private SiteOID siteOID;
    private SupplierOID supplierOID;
}
