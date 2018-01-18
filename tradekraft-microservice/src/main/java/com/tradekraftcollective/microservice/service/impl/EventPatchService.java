package com.tradekraftcollective.microservice.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonpatch.*;
import com.tradekraftcollective.microservice.constants.PatchOperationConstants;
import com.tradekraftcollective.microservice.exception.ErrorCode;
import com.tradekraftcollective.microservice.exception.ServiceException;
import com.tradekraftcollective.microservice.persistence.entity.Event;
import com.tradekraftcollective.microservice.service.IEventPatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by brandonfeist on 10/8/17.
 */
@Slf4j
@Service
public class EventPatchService implements IEventPatchService {
    public static final String EVENT_NAME_PATH = "/name";
    public static final String EVENT_IMAGE_PATH = "/image";
    public static final String EVENT_DESCRIPTION_PATH = "/description";
    public static final String EVENT_TICKET_LINK_PATH = "/ticketLink";
    public static final String EVENT_ENTRY_AGE_PATH = "/entryAge";
    public static final String EVENT_ADDRESS_PATH = "/address";
    public static final String EVENT_CITY_PATH = "/city";
    public static final String EVENT_STATE_PATH = "/state";
    public static final String EVENT_ZIP_PATH = "/zip";

    @Inject
    ObjectMapper objectMapper;

    @Override
    public Event patchEvent(List<JsonPatchOperation> patchOperations, Event event) {
        if(event == null) {
            log.error("Event cannot be null");
            throw new ServiceException(ErrorCode.INVALID_ARTIST, "Event cannot be null");
        }

        Map<String, String> pathMap = new HashMap<>();
        pathMap.put(EVENT_NAME_PATH, EVENT_NAME_PATH);
        pathMap.put(EVENT_IMAGE_PATH, EVENT_IMAGE_PATH);

        for(JsonPatchOperation operation : patchOperations) {
            if(PatchOperationConstants.COPY.equals(operation.getOp())
                    || PatchOperationConstants.MOVE.equals(operation.getOp())) {
                DualPathOperation dualPathOperation = (DualPathOperation) operation;

            } else if(PatchOperationConstants.REMOVE.equals(operation.getOp())) {
                RemoveOperation removeOperation = (RemoveOperation) operation;

            } else {
                PathValueOperation pathValueOperation = (PathValueOperation) operation;

            }
        }

        JsonNode eventJsonNode = objectMapper.valueToTree(event);

        ArrayNode artistArray = ((ArrayNode) eventJsonNode.get("artists"));

        for(int artistIndex = 0; artistIndex < artistArray.size(); artistIndex++) {
            ((ObjectNode) artistArray.get(artistIndex)).remove("releases");
        }

        ((ObjectNode) eventJsonNode).replace("artists", artistArray);

        try {
            JsonPatch patcher = new JsonPatch(patchOperations);
            eventJsonNode = patcher.apply(eventJsonNode);
        } catch(JsonPatchException e) {
            e.printStackTrace();
        }

        Event patchedEvent = objectMapper.convertValue(eventJsonNode, Event.class);

        return patchedEvent;
    }
}
