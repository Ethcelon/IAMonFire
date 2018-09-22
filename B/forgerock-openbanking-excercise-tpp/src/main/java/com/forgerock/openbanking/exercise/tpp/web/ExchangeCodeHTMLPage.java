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
package com.forgerock.openbanking.exercise.tpp.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExchangeCodeHTMLPage {

    private final String TPP_EXCHANGE_CODE_URI = "tppExchangeCodeUri";


    @GetMapping("/web/exchange_code/aisp")
    public String aispExchangeCode(Model model) {
        model.addAttribute(TPP_EXCHANGE_CODE_URI, "/api/open-banking/account-requests/exchange_code");
        return "exchangeCode";
    }

    @GetMapping("/web/exchange_code/pisp")
    public String pispExchangeCode(Model model) {
        model.addAttribute(TPP_EXCHANGE_CODE_URI, "/api/open-banking/payment-requests/exchange_code");
        return "exchangeCode";
    }
}
