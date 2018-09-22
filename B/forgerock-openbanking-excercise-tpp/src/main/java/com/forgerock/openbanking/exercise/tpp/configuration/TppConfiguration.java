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
package com.forgerock.openbanking.exercise.tpp.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@ConfigurationProperties(prefix = "tpp")
public class TppConfiguration {
    private RedirectUris redirectUris;

    private OpenBanking openBanking;

    private String jwkms;

    private DirectoryConfig directory;

    private ASPSPConfig aspsp;


    public String getJwkms() {
        return jwkms;
    }

    public RedirectUris getRedirectUris() {
        return redirectUris;
    }

    public void setRedirectUris(RedirectUris redirectUris) {
        this.redirectUris = redirectUris;
    }

    public OpenBanking getOpenBanking() {
        return openBanking;
    }

    public void setOpenBanking(OpenBanking openBanking) {
        this.openBanking = openBanking;
    }

    public void setJwkms(String jwkms) {
        this.jwkms = jwkms;
    }

    public DirectoryConfig getDirectory() {
        return directory;
    }

    public void setDirectory(DirectoryConfig directory) {
        this.directory = directory;
    }

    public ASPSPConfig getAspsp() {
        return aspsp;
    }

    public void setAspsp(ASPSPConfig aspsp) {
        this.aspsp = aspsp;
    }

    public static class ASPSPConfig {
        private String financialId;
        private String asDiscoveryEndpoint;
        private String rsDiscoveryEndpoint;

        public String getFinancialId() {
            return financialId;
        }

        public void setFinancialId(String financialId) {
            this.financialId = financialId;
        }

        public String getAsDiscoveryEndpoint() {
            return asDiscoveryEndpoint;
        }

        public void setAsDiscoveryEndpoint(String asDiscoveryEndpoint) {
            this.asDiscoveryEndpoint = asDiscoveryEndpoint;
        }

        public String getRsDiscoveryEndpoint() {
            return rsDiscoveryEndpoint;
        }

        public void setRsDiscoveryEndpoint(String rsDiscoveryEndpoint) {
            this.rsDiscoveryEndpoint = rsDiscoveryEndpoint;
        }
    }

    public static class RedirectUris {
        private String pisp;
        private String aisp;

        public String getPisp() {
            return pisp;
        }

        public void setPisp(String pisp) {
            this.pisp = pisp;
        }

        public String getAisp() {
            return aisp;
        }

        public void setAisp(String aisp) {
            this.aisp = aisp;
        }
    }

    public static class OpenBanking {
        private String version;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }

    public static class DirectoryConfig {
        private String endpoint;
        private UserConfig user;

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public UserConfig getUser() {
            return user;
        }

        public void setUser(UserConfig directory) {
            this.user = directory;
        }
    }

    public static class UserConfig {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
