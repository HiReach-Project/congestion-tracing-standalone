package com.hireach.congestiontracingstandalone.component;

import com.hireach.congestiontracingstandalone.entity.Company;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
public class CompanyWrapper {

    private Company company;

}
