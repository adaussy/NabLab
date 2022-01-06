/*******************************************************************************
 * Copyright (c) 2022 CEA
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 * Contributors: see AUTHORS file
 *******************************************************************************/
package fr.cea.nablab.sirius.web.app.datafetchers.subscriptions;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;

import org.eclipse.sirius.web.annotations.graphql.GraphQLSubscriptionTypes;
import org.eclipse.sirius.web.annotations.spring.graphql.SubscriptionDataFetcher;
import org.eclipse.sirius.web.core.api.IPayload;
import org.eclipse.sirius.web.spring.collaborative.api.IEditingContextEventProcessorRegistry;
import org.eclipse.sirius.web.spring.collaborative.diagrams.api.DiagramConfiguration;
import org.eclipse.sirius.web.spring.collaborative.diagrams.api.IDiagramEventProcessor;
import org.eclipse.sirius.web.spring.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.web.spring.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.web.spring.collaborative.dto.SubscribersUpdatedEventPayload;
import org.eclipse.sirius.web.spring.graphql.api.IDataFetcherWithFieldCoordinates;
import org.reactivestreams.Publisher;

import graphql.schema.DataFetchingEnvironment;
import reactor.core.publisher.Flux;

/**
 * @author arichard
 */
//@formatter:off
@GraphQLSubscriptionTypes(
 input = DiagramEventInput.class,
 payloads = {
     DiagramRefreshedEventPayload.class,
     SubscribersUpdatedEventPayload.class,
 }
)
@SubscriptionDataFetcher(type = "Subscription", field = SubscriptionDiagramEventDataFetcher.DIAGRAM_EVENT_FIELD)
//@formatter:on
public class SubscriptionDiagramEventDataFetcher implements IDataFetcherWithFieldCoordinates<Publisher<IPayload>> {

    public static final String DIAGRAM_EVENT_FIELD = "diagramEvent"; //$NON-NLS-1$

    private final ObjectMapper objectMapper;

    private final IEditingContextEventProcessorRegistry editingContextEventProcessorRegistry;

    public SubscriptionDiagramEventDataFetcher(ObjectMapper objectMapper, IEditingContextEventProcessorRegistry editingContextEventProcessorRegistry) {
        this.objectMapper = Objects.requireNonNull(objectMapper);
        this.editingContextEventProcessorRegistry = Objects.requireNonNull(editingContextEventProcessorRegistry);
    }

    @Override
    public Publisher<IPayload> get(DataFetchingEnvironment environment) throws Exception {
        Object argument = environment.getArgument("input"); //$NON-NLS-1$
        var input = this.objectMapper.convertValue(argument, DiagramEventInput.class);
        var diagramConfiguration = new DiagramConfiguration(input.getDiagramId());

     // @formatter:off
     return this.editingContextEventProcessorRegistry.getOrCreateEditingContextEventProcessor(input.getEditingContextId())
             .flatMap(processor -> processor.acquireRepresentationEventProcessor(IDiagramEventProcessor.class, diagramConfiguration, input))
             .map(representationEventProcessor -> representationEventProcessor.getOutputEvents(input))
             .orElse(Flux.empty());
     // @formatter:on
    }
}
