/*
 * The contents of this file are subject to the terms of the Common Development and
 *  Distribution License (the License). You may not use this file except in compliance with the
 *  License.
 *
 *  You can obtain a copy of the License at https://forgerock.org/license/CDDLv1.0.html. See the License for the
 *  specific language governing permission and limitations under the License.
 *
 *  When distributing Covered Software, include this CDDL Header Notice in each file and include
 *  the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 *  Header, with the fields enclosed by brackets [] replaced by your own identifying
 *  information: "Portions copyright [year] [name of copyright owner]".
 *
 *  Copyright 2018 ForgeRock AS.
 *
 */
package com.forgerock.openbanking.exercise.tpp.model.balance;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import uk.org.openbanking.datamodel.payment.OBInitiation1;
import uk.org.openbanking.datamodel.payment.OBRisk1;
import uk.org.openbanking.datamodel.payment.OBTransactionIndividualStatus1Code;

import java.util.Date;


/**
 * Representation of an account.
 */
public class  Balance {

    private String amount;

    @JsonProperty("Amount")
    public void setAmount(String amount) {
        this.amount = amount;
    }

    @JsonProperty("Amount")
    public String getAmount() {
        return amount;
    }
}
