CREATE TABLE public.hashdata (
    "timestamp" int4 NOT NULL,
    minutehash text NULL,
    roothash text NULL,
    secondhash text NULL
    CONSTRAINT hashdata_pkey PRIMARY KEY ("timestamp")
);