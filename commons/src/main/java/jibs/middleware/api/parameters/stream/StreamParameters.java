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
package jibs.middleware.api.parameters.stream;

/**
 *
 * @author jibrilhp
 */

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Introspected
@Serdeable.Deserializable
public class StreamParameters {

    private String project_id;
    private String target_dataset;
    private String target_table;
    private String last_update;

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getTarget_dataset() {
        return target_dataset;
    }

    public void setTarget_dataset(String target_dataset) {
        this.target_dataset = target_dataset;
    }

    public String getTarget_table() {
        return target_table;
    }

    public void setTarget_table(String target_table) {
        this.target_table = target_table;
    }
    
    
    
    
}
