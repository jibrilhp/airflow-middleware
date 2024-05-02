package jibs.middleware.config;

import io.micronaut.context.annotation.ConfigurationProperties;

/*
 * Copyright 2024 jibs.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 *
 * @author jibrilhp
 */

@ConfigurationProperties("datahub")
public class DatahubConfig {

     private String hostFrontend;
     private String hostGms;
     private String token;
     
     
    public String getHostFrontend() {
        return hostFrontend;
    }

    public void setHostFrontend(String hostFrontend) {
        this.hostFrontend = hostFrontend;
    }

    public String getHostGms() {
        return hostGms;
    }

    public void setHostGms(String hostGms) {
        this.hostGms = hostGms;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
 
     
   
}
