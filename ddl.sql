CREATE TABLE public.middleware(
   trigger_id SERIAL PRIMARY KEY,
   trigger_name VARCHAR NULL,
   trigger_source_dag VARCHAR NULL,
   trigger_target_dag VARCHAR NOT NULL,
   trigger_description VARCHAR NULL,
   datahub_updated_at TIMESTAMPTZ NULL,
   last_trigger_at TIMESTAMPTZ NULL,
   created_at TIMESTAMPTZ NULL,
   updated_at TIMESTAMPTZ NULL
);