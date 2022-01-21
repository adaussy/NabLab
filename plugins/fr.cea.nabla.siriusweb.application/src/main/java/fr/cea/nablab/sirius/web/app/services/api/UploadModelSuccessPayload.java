/*******************************************************************************
 * Copyright (c) 2022 CEA
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 * Contributors: see AUTHORS file
 *******************************************************************************/
package fr.cea.nablab.sirius.web.app.services.api;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.UUID;

import org.eclipse.sirius.web.annotations.graphql.GraphQLField;
import org.eclipse.sirius.web.annotations.graphql.GraphQLID;
import org.eclipse.sirius.web.annotations.graphql.GraphQLNonNull;
import org.eclipse.sirius.web.annotations.graphql.GraphQLObjectType;
import org.eclipse.sirius.web.core.api.IPayload;
import org.eclipse.sirius.web.representations.IRepresentation;

/**
 * @author arichard
 */
@GraphQLObjectType
public class UploadModelSuccessPayload implements IPayload {

    private final UUID id;

    private final IRepresentation representation;

    public UploadModelSuccessPayload(UUID id, IRepresentation representation) {
        this.id = Objects.requireNonNull(id);
        this.representation = Objects.requireNonNull(representation);
    }

    @Override
    @GraphQLID
    @GraphQLField
    @GraphQLNonNull
    public UUID getId() {
        return this.id;
    }

    @GraphQLField
    @GraphQLNonNull
    public IRepresentation getRepresentation() {
        return this.representation;
    }

    @Override
    public String toString() {
        String pattern = "{0} '{'id: {1}, representation: '{'id: {2}, type: {3}'}''}'"; //$NON-NLS-1$
        return MessageFormat.format(pattern, this.getClass().getSimpleName(), this.id, this.representation.getId(), this.representation.getClass().getSimpleName());
    }

}