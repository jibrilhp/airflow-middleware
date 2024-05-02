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
package jibs.middleware.api.parameters.stream.response;

/**
 *
 * @author jibrilhp
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class DataResult {
    @JsonProperty("threshold")
    private String threshold;

    @JsonProperty("trigger_dag")
    private String triggerDag;

    private String lastTriggered;
    
    
    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

    public String getTriggerDag() {
        return triggerDag;
    }

    public void setTriggerDag(String triggerDag) {
        this.triggerDag = triggerDag;
    }

    public String getLastTriggered() {
        return lastTriggered;
    }

    public void setLastTriggered(String lastTriggered) {
        this.lastTriggered = lastTriggered;
    }
    
}
