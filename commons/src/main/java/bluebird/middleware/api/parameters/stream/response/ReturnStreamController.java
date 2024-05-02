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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class ReturnStreamController {
/**
 *  {"status": 200, "data": {"threshold":"True", "trigger_dag":"bod_taxi","last_triggered":"2024-03-12T11:48:00Z"}}
 */
    
    @JsonProperty("status")
    private int status;

    @JsonProperty("data")
    private DataResult data;
    
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataResult getData() {
        return data;
    }


    public void setData(DataResult data) {
        this.data = data;
    }

}
