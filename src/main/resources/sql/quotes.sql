-- https://dbdiagram.io/d/6123648f6dc2bb6073b6c46b

DROP TABLE IF EXISTS quotes_history;
DROP TABLE IF EXISTS quotes;

CREATE TABLE "quotes"
(
    "id"         BIGSERIAL PRIMARY KEY,
    "isin"       text UNIQUE NOT NULL,
    "ask"        numeric     NOT NULL,
    "bid"        numeric,
    "elvl"       numeric,
    "updated_at" timestamp with time zone DEFAULT (now())
);

CREATE TABLE "quotes_history"
(
    "id"         BIGSERIAL PRIMARY KEY,
    "isin"       text    NOT NULL,
    "ask"        numeric NOT NULL,
    "bid"        numeric,
    "elvl"       numeric,
    "created_at" timestamp with time zone DEFAULT (now())
);

ALTER TABLE "quotes_history"
    ADD FOREIGN KEY ("isin") REFERENCES "quotes" ("isin");

COMMENT ON COLUMN "quotes"."isin" IS 'Международный идентификационный код ценной бумаги';

COMMENT ON COLUMN "quotes"."ask" IS 'Цена продавца';

COMMENT ON COLUMN "quotes"."bid" IS 'Цена покупателя';

COMMENT ON COLUMN "quotes"."elvl" IS 'Energy level';

COMMENT ON COLUMN "quotes_history"."isin" IS 'Международный идентификационный код ценной бумаги';

COMMENT ON COLUMN "quotes_history"."ask" IS 'Цена продавца';

COMMENT ON COLUMN "quotes_history"."bid" IS 'Цена покупателя';

COMMENT ON COLUMN "quotes_history"."elvl" IS 'Energy level';

CREATE INDEX quotes_isin_hash_index ON quotes_history USING hash (isin);