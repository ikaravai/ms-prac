package com.solvd.ikaravai.organizationservice.event.source;

import com.solvd.ikaravai.organizationservice.event.model.ActionEnum;
import com.solvd.ikaravai.organizationservice.event.model.OrganizationChangeModel;
import com.solvd.ikaravai.organizationservice.utils.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class SimpleSourceBean {

    private final Source source;

    public void publishOrganizationChange(ActionEnum action, String organizationId) {
        log.info("Sending kafka message {} for organization ID: {}", action, organizationId);
        OrganizationChangeModel change = new OrganizationChangeModel(
                OrganizationChangeModel.class.getTypeName(),
                action.toString(),
                organizationId,
                UserContext.getCorrelationId()
        );
        source.output().send(
                MessageBuilder.withPayload(change).build()
        );
    }
}
